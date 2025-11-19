package cn.hutool.v7.db.config;

import cn.hutool.v7.core.array.ArrayUtil;
import cn.hutool.v7.core.convert.ConvertUtil;
import cn.hutool.v7.core.io.resource.NoResourceException;
import cn.hutool.v7.core.io.resource.ResourceUtil;
import cn.hutool.v7.core.text.StrUtil;
import cn.hutool.v7.core.util.ObjUtil;
import cn.hutool.v7.db.DbException;
import cn.hutool.v7.db.driver.DriverUtil;
import cn.hutool.v7.db.sql.SqlLog;
import cn.hutool.v7.db.sql.filter.SqlLogFilter;
import cn.hutool.v7.log.level.Level;
import cn.hutool.v7.setting.toml.TomlReader;

import java.util.HashMap;
import java.util.Map;

/**
 * 基于TOML类型的数据库配置解析器
 *
 * @author Looly
 * @since 7.0.0
 */
public class TomlConfigParser implements ConfigParser {

	private static final String CONNECTION_PREFIX = "connection.";

	/**
	 * 默认TOML配置文件路径
	 */
	private static final String[] DEFAULT_DB_TOML_PATHS = {"config/db.toml", "db.toml"};

	/**
	 * 创建默认配置解析器
	 *
	 * @return TomlConfigParser
	 */
	public static TomlConfigParser of() {
		return of(null);
	}

	/**
	 * 创建配置解析器
	 *
	 * @param tomlPath TOML配置文件路径
	 * @return TomlConfigParser
	 */
	public static TomlConfigParser of(final String tomlPath) {
		return new TomlConfigParser(tomlPath);
	}

	private final Map<String, Object> tomlData;

	/**
	 * 构造函数
	 *
	 * @param tomlPath 自定义TOML配置文件路径，{@code null}表示使用默认路径
	 */
	public TomlConfigParser(final String tomlPath) {
		String tomlContent = null;
		if (null != tomlPath) {
			// 读取指定TOML文件
			tomlContent = ResourceUtil.readUtf8Str(tomlPath);
		} else {
			// 读取默认TOML文件
			for (final String defaultDbTomlPath : DEFAULT_DB_TOML_PATHS) {
				try {
					tomlContent = ResourceUtil.readUtf8Str(defaultDbTomlPath);
				} catch (final NoResourceException e) {
					// ignore
				}
			}
		}

		if (null == tomlContent) {
			throw new NoResourceException("Default db toml [{}] in classpath not found !", ArrayUtil.join(DEFAULT_DB_TOML_PATHS, ","));
		}

		this.tomlData = new TomlReader(tomlContent, false).read();
	}

	@SuppressWarnings("unchecked")
	@Override
	public DbConfig parse(final String group) {
		final Map<String, Object> groupData;

		if (StrUtil.isEmpty(group)) {
			groupData = this.tomlData;
		} else {
			final Object groupObj = this.tomlData.get(group);
			if (groupObj instanceof Map) {
				// 新建一个Map，避免继承属性影响原数据
				groupData = new HashMap<>((Map<String, Object>) groupObj);
			} else {
				throw new DbException("No config for group: [{}]", group);
			}
		}

		// 继承属性
		copyPropertyIfAbsent(groupData, DSKeys.KEY_SHOW_SQL);
		copyPropertyIfAbsent(groupData, DSKeys.KEY_FORMAT_SQL);
		copyPropertyIfAbsent(groupData, DSKeys.KEY_SHOW_PARAMS);
		copyPropertyIfAbsent(groupData, DSKeys.KEY_SQL_LEVEL);

		return toDbConfig(groupData);
	}

	/**
	 * 如果目标map中没有指定键，则从全局配置复制
	 */
	private void copyPropertyIfAbsent(final Map<String, Object> target, final String key) {
		if (!target.containsKey(key) && this.tomlData.containsKey(key)) {
			target.put(key, this.tomlData.get(key));
		}
	}

	/**
	 * 将TOML数据转换为DbConfig对象
	 */
	private DbConfig toDbConfig(final Map<String, Object> data) {
		// 基本信息
		final String url = getAndRemoveString(data, DSKeys.KEY_ALIAS_URL);
		if (StrUtil.isBlank(url)) {
			throw new DbException("No JDBC URL!");
		}

		// 自动识别Driver
		String driver = getAndRemoveString(data, DSKeys.KEY_ALIAS_DRIVER);
		if (StrUtil.isBlank(driver)) {
			driver = DriverUtil.identifyDriver(url);
		}

		final DbConfig dbConfig = DbConfig.of()
			.setUrl(url)
			.setDriver(driver)
			.setUser(getAndRemoveString(data, DSKeys.KEY_ALIAS_USER))
			.setPass(getAndRemoveString(data, DSKeys.KEY_ALIAS_PASSWORD));

		// SQL日志
		final SqlLogFilter sqlLogFilter = getSqlLogFilter(data);
		if (sqlLogFilter != null) {
			dbConfig.addSqlFilter(sqlLogFilter);
		}

		// 大小写等配置
		final Boolean caseInsensitive = getAndRemoveBoolean(data, DSKeys.KEY_CASE_INSENSITIVE);
		if (null != caseInsensitive) {
			dbConfig.setCaseInsensitive(caseInsensitive);
		}

		// 连接配置
		for (final String key : DSKeys.KEY_CONN_PROPS) {
			final String connValue = getAndRemoveString(data, key);
			if (StrUtil.isNotBlank(connValue)) {
				dbConfig.addConnProps(key, connValue);
			}
		}

		// 自定义连接属性
		data.forEach((key, value) -> {
			if(key.startsWith(CONNECTION_PREFIX) && !(value instanceof Map)){
				dbConfig.addConnProps(key.substring(CONNECTION_PREFIX.length()), value.toString());
			}
		});

		// 池属性 - 移除已处理的属性后剩余的作为池属性
		data.forEach((key, value) -> {
			// 非Map的属性作为池属性
			if(!(value instanceof Map)){
				dbConfig.addPoolProps(key, StrUtil.toStringOrNull(value));
			}
		});

		return dbConfig;
	}

	/**
	 * 获取SQL日志过滤器
	 */
	private SqlLogFilter getSqlLogFilter(final Map<String, Object> data) {
		final Boolean isShowSql = getAndRemoveBoolean(data, DSKeys.KEY_SHOW_SQL);
		if (isShowSql == null || !isShowSql) {
			return null;
		}

		final boolean isFormatSql = ObjUtil.defaultIfNull(getAndRemoveBoolean(data, DSKeys.KEY_FORMAT_SQL), false);
		final boolean isShowParams = ObjUtil.defaultIfNull(getAndRemoveBoolean(data, DSKeys.KEY_SHOW_PARAMS), false);

		String sqlLevelStr = getAndRemoveString(data, DSKeys.KEY_SQL_LEVEL);
		if (sqlLevelStr != null) {
			sqlLevelStr = sqlLevelStr.toUpperCase();
		}
		final Level level = ConvertUtil.toEnum(Level.class, sqlLevelStr, Level.DEBUG);

		final SqlLog sqlLog = new SqlLog();
		sqlLog.init(true, isFormatSql, isShowParams, level);

		return new SqlLogFilter(sqlLog);
	}

	/**
	 * 从map中获取字符串值
	 *
	 * @param map  源map
	 * @param keys 多个键
	 * @return 字符串值
	 */
	private String getAndRemoveString(final Map<String, Object> map, final String... keys) {
		Object value = null;
		for (final String key : keys) {
			value = map.remove(key);
			if (null != value) {
				break;
			}
		}
		return StrUtil.toStringOrNull(value);
	}

	/**
	 * 从map中获取布尔值
	 *
	 * @param map  源map
	 * @param keys 多个键
	 * @return 布尔值
	 */
	private Boolean getAndRemoveBoolean(final Map<String, Object> map, final String... keys) {
		Object value = null;
		for (final String key : keys) {
			value = map.remove(key);
			if (null != value) {
				break;
			}
		}
		return ConvertUtil.toBoolean(value);
	}
}
