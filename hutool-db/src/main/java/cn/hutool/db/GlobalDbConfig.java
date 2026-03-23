package cn.hutool.db;

import cn.hutool.core.io.resource.NoResourceException;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.db.sql.SqlLog;
import cn.hutool.log.level.Level;
import cn.hutool.setting.Setting;

/**
 * DB全局配置配置项
 *
 * @author looly
 * @since 5.3.10
 */
public class GlobalDbConfig {
	/**
	 * 数据库配置文件可选路径1
	 */
	private static final String DEFAULT_DB_SETTING_PATH = "config/db.setting";
	/**
	 * 数据库配置文件可选路径2
	 */
	private static final String DEFAULT_DB_SETTING_PATH2 = "db.setting";

	/**
	 * 是否大小写不敏感（默认大小写不敏感）
	 */
	protected static boolean caseInsensitive = true;
	/**
	 * 是否INSERT语句中默认返回主键（默认返回主键）
	 */
	protected static boolean returnGeneratedKey = true;
	/**
	 * 自定义数据库配置文件路径（绝对路径或相对classpath路径）
	 *
	 * @since 5.8.0
	 */
	private static String dbSettingPath = null;
	/**
	 * 自定义构造结果集时每次从数据库取的行数
	 */
	protected static Integer statementFetchSize = null;

	/**
	 * 设置全局是否在结果中忽略大小写<br>
	 * 如果忽略，则在Entity中调用getXXX时，字段值忽略大小写，默认忽略
	 *
	 * @param isCaseInsensitive 否在结果中忽略大小写
	 */
	public static void setCaseInsensitive(boolean isCaseInsensitive) {
		caseInsensitive = isCaseInsensitive;
	}

	/**
	 * 设置全局是否INSERT语句中默认返回主键（默认返回主键）<br>
	 * 如果false，则在Insert操作后，返回影响行数
	 * 主要用于某些数据库不支持返回主键的情况
	 *
	 * @param isReturnGeneratedKey 是否INSERT语句中默认返回主键
	 */
	public static void setReturnGeneratedKey(boolean isReturnGeneratedKey) {
		returnGeneratedKey = isReturnGeneratedKey;
	}

	/**
	 * 自定义数据库配置文件路径（绝对路径或相对classpath路径）
	 *
	 * @param customDbSettingPath 自定义数据库配置文件路径（绝对路径或相对classpath路径）
	 * @since 5.8.0
	 */
	public static void setDbSettingPath(String customDbSettingPath) {
		dbSettingPath = customDbSettingPath;
	}

	/**
	 * 获取自定义或默认位置数据库配置{@link Setting}
	 *
	 * @return 数据库配置
	 * @since 5.8.0
	 */
	public static Setting createDbSetting() {
		Setting setting;
		if (null != dbSettingPath) {
			// 自定义数据库配置文件位置
			try {
				setting = new Setting(dbSettingPath, false);
			} catch (NoResourceException e3) {
				throw new NoResourceException("Customize db setting file [{}] not found !", dbSettingPath);
			}
		} else {
			setting = tryDefaultDbSetting();
		}
		return setting;
	}

	/**
	 * 获取自定义或默认位置数据库配置{@link Setting}
	 *
	 * @return 数据库配置
	 * @since 5.8.36
	 */
	private static Setting tryDefaultDbSetting() {
		final String[] defaultDbSettingPaths = {"file:" + DEFAULT_DB_SETTING_PATH, "file:" + DEFAULT_DB_SETTING_PATH2, DEFAULT_DB_SETTING_PATH, DEFAULT_DB_SETTING_PATH2};
		for (final String settingPath : defaultDbSettingPaths) {
			try {
				return new Setting(settingPath, true);
			} catch (final NoResourceException e) {
				// ignore
			}
		}

		throw new NoResourceException("Default db settings [{}] in classpath not found !", ArrayUtil.join(defaultDbSettingPaths, ","));
	}

	/**
	 * 设置全局配置：是否通过debug日志显示SQL
	 *
	 * @param isShowSql    是否显示SQL
	 * @param isFormatSql  是否格式化显示的SQL
	 * @param isShowParams 是否打印参数
	 * @param level        SQL打印到的日志等级
	 */
	public static void setShowSql(boolean isShowSql, boolean isFormatSql, boolean isShowParams, Level level) {
		SqlLog.INSTANCE.init(isShowSql, isFormatSql, isShowParams, level);
	}

	/**
	 * 设置构造结果集时每次从数据库取到的行数
	 *
	 * @param statementFetchSize 每一轮网络请求获取的行数
	 * @since 5.8.39
	 */
	public static void setStatementFetchSize(Integer statementFetchSize) {
		GlobalDbConfig.statementFetchSize = statementFetchSize;
	}
}
