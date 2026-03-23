package cn.hutool.db.dialect.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.db.Entity;
import cn.hutool.db.Page;
import cn.hutool.db.StatementUtil;
import cn.hutool.db.dialect.DialectName;
import cn.hutool.db.sql.SqlBuilder;
import cn.hutool.db.sql.Wrapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Hana数据库方言
 *
 * @author daoyou.dev
 */
public class HanaDialect extends AnsiSqlDialect {
	private static final long serialVersionUID = 1L;

	/**
	 * 构造
	 */
	public HanaDialect() {
		wrapper = new Wrapper('"');
	}

	@Override
	public String dialectName() {
		return DialectName.HANA.name();
	}

	@Override
	protected SqlBuilder wrapPageSql(SqlBuilder find, Page page) {
		// SAP HANA 使用 OFFSET LIMIT 分页
		return find.append(" LIMIT ").append(page.getPageSize())
			.append(" OFFSET ").append(page.getStartPosition());
	}

	/**
	 * 构建用于upsert的{@link PreparedStatement}。
	 * SAP HANA 使用 MERGE INTO 语法来实现 UPSERT 操作。
	 * <p>
	 * 生成 SQL 语法为：
	 * <pre>
	 *     MERGE INTO demo AS target
	 *     USING (SELECT ? AS a, ? AS b, ? AS c FROM DUMMY) AS source
	 *     ON target.id = source.id
	 *     WHEN MATCHED THEN
	 *         UPDATE SET target.a = source.a, target.b = source.b, target.c = source.c
	 *     WHEN NOT MATCHED THEN
	 *         INSERT (a, b, c) VALUES (source.a, source.b, source.c);
	 * </pre>
	 *
	 * @param conn   数据库连接对象
	 * @param entity 数据实体类（包含表名）
	 * @param keys   主键字段数组，通常用于确定匹配条件（联合主键）
	 * @return PreparedStatement
	 * @throws SQLException SQL 执行异常
	 */
	@Override
	public PreparedStatement psForUpsert(Connection conn, Entity entity, String... keys) throws SQLException {
		SqlBuilder.validateEntity(entity);
		final SqlBuilder builder = SqlBuilder.create(wrapper);

		final List<String> columns = new ArrayList<>();
		final List<Object> values = new ArrayList<>();

		// 构建字段部分和参数占位符部分
		entity.forEach((field, value) -> {
			if (StrUtil.isNotBlank(field)) {
				columns.add(wrapper != null ? wrapper.wrap(field) : field);
				values.add(value);
			}
		});

		String tableName = entity.getTableName();
		if (wrapper != null) {
			tableName = wrapper.wrap(tableName);
		}

		// 构建 UPSERT 语句
		builder.append("UPSERT ").append(tableName).append(" (");
		builder.append(String.join(", ", columns));
		builder.append(") VALUES (");
		builder.append(String.join(", ", Collections.nCopies(columns.size(), "?")));
		builder.append(") WITH PRIMARY KEY");

		return StatementUtil.prepareStatement(conn, builder.toString(), values);
	}
}

