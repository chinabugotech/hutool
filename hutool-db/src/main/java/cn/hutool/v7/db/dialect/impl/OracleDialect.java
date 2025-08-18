/*
 * Copyright (c) 2013-2025 Hutool Team and hutool.cn
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

import cn.hutool.v7.core.text.StrPool;
import cn.hutool.v7.core.text.StrUtil;
import cn.hutool.v7.db.Page;
import cn.hutool.v7.db.config.DbConfig;
import cn.hutool.v7.db.dialect.DialectName;
import cn.hutool.v7.db.sql.SqlBuilder;

import java.io.Serial;

/**
 * Oracle 方言
 *
 * @author loolly
 */
public class OracleDialect extends AnsiSqlDialect {
	@Serial
	private static final long serialVersionUID = 6122761762247483015L;

	private static final String DEFAULT_TABLE_ALIAS = "table_alias_";
	private static final String DEFAULT_ROW_ALIAS = "row_";
	private static final String DEFAULT_ROWNUM_ALIAS = "rownum_";

	/**
	 * 检查字段值是否为Oracle自增字段，自增字段以`.nextval`结尾
	 *
	 * @param value 检查的字段值
	 * @return 是否为Oracle自增字段
	 * @since 5.7.20
	 */
	public static boolean isNextVal(final Object value) {
		return (value instanceof CharSequence) && StrUtil.endWithIgnoreCase(value.toString(), ".nextval");
	}

	/**
	 * 构造
	 *
	 * @param dbConfig 数据库配置
	 */
	public OracleDialect(final DbConfig dbConfig) {
		super(dbConfig);
		//Oracle所有字段名用双引号包围，防止字段名或表名与系统关键字冲突
		//wrapper = new Wrapper('"');
	}

	@Override
	protected SqlBuilder wrapPageSql(final SqlBuilder find, final Page page) {
		final int[] startEnd = page.getStartEnd();

		// 检查别名，避免重名
		final String sql = find.toString();
		String tableAlias = DEFAULT_TABLE_ALIAS;
		while (sql.contains(tableAlias)) {
			tableAlias += StrPool.UNDERLINE;
		}
		String rowAlias = DEFAULT_ROW_ALIAS;
		while (sql.contains(rowAlias)) {
			rowAlias += StrPool.UNDERLINE;
		}
		String rownumAlias = DEFAULT_ROWNUM_ALIAS;
		while (sql.contains(rownumAlias)) {
			rownumAlias += StrPool.UNDERLINE;
		}

		return find
			.insertPreFragment("SELECT * FROM ( SELECT " + rowAlias + ".*, rownum " + rownumAlias + " from ( ")
			.append(" ) row_ where rownum <= ").append(startEnd[1])//
			.append(") ").append(tableAlias)//
			.append(" where ").append(tableAlias).append(".rownum_ > ").append(startEnd[0]);//
	}

	@Override
	public String dialectName() {
		return DialectName.ORACLE.name();
	}
}
