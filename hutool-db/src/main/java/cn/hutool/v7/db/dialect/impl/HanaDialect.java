/*
 * Copyright (c) 2013-2026 Hutool Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.hutool.v7.db.dialect.impl;

import cn.hutool.v7.core.text.StrUtil;
import cn.hutool.v7.db.DbException;
import cn.hutool.v7.db.Entity;
import cn.hutool.v7.db.config.DbConfig;
import cn.hutool.v7.db.dialect.DialectName;
import cn.hutool.v7.db.sql.QuoteWrapper;
import cn.hutool.v7.db.sql.SqlBuilder;
import cn.hutool.v7.db.sql.StatementUtil;

import java.io.Serial;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Hana数据库方言
 *
 * @author daoyou.dev
 */
public class HanaDialect extends AnsiSqlDialect {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 构造
	 *
	 * @param config 数据库配置对象
	 */
	public HanaDialect(final DbConfig config) {
		super(config);
		quoteWrapper = new QuoteWrapper('"');
	}

	@Override
	public String dialectName() {
		return DialectName.HANA.name();
	}

	/**
	 * 构建用于upsert的{@link PreparedStatement}。
	 * SAP HANA 使用 MERGE INTO 语法来实现 UPSERT 操作。
	 * <p>
	 * 生成 SQL 语法为：
	 * <pre>{@code
	 *     MERGE INTO demo AS target
	 *     USING (SELECT ? AS a, ? AS b, ? AS c FROM DUMMY) AS source
	 *     ON target.id = source.id
	 *     WHEN MATCHED THEN
	 *         UPDATE SET target.a = source.a, target.b = source.b, target.c = source.c
	 *     WHEN NOT MATCHED THEN
	 *         INSERT (a, b, c) VALUES (source.a, source.b, source.c);
	 * }</pre>
	 *
	 * @param conn   数据库连接对象
	 * @param entity 数据实体类（包含表名）
	 * @param keys   主键字段数组，通常用于确定匹配条件（联合主键）
	 * @return PreparedStatement
	 * @throws DbException SQL 执行异常
	 */
	@Override
	public PreparedStatement psForUpsert(final Connection conn, final Entity entity, final String... keys) throws DbException {
		SqlBuilder.validateEntity(entity);
		final SqlBuilder builder = SqlBuilder.of(quoteWrapper);

		final List<String> columns = new ArrayList<>();

		// 构建字段部分和参数占位符部分
		entity.forEach((field, value) -> {
			if (StrUtil.isNotBlank(field)) {
				columns.add(quoteWrapper != null ? quoteWrapper.wrap(field) : field);
				builder.addParams(value);
			}
		});

		String tableName = entity.getTableName();
		if (quoteWrapper != null) {
			tableName = quoteWrapper.wrap(tableName);
		}

		// 构建 UPSERT 语句
		builder.append("UPSERT ").append(tableName).append(" (");
		builder.append(String.join(", ", columns));
		builder.append(") VALUES (");
		builder.append(String.join(", ", Collections.nCopies(columns.size(), "?")));
		builder.append(") WITH PRIMARY KEY");

		return StatementUtil.prepareStatement(false, this.dbConfig, conn, builder.build(), builder.getParamValueArray());
	}
}
