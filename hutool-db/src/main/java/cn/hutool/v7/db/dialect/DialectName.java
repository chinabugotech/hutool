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

package cn.hutool.v7.db.dialect;

import cn.hutool.v7.core.text.StrUtil;

/**
 * 方言名<br>
 * 方言枚举列出了Hutool支持的所有数据库方言
 *
 * @author Looly
 */
public enum DialectName {
	/**
	 * ANSI标准SQL数据库类型，代表使用ANSI SQL标准的数据库系统
	 */
	ANSI,

	/**
	 * MySQL数据库类型，代表MySQL数据库系统
	 */
	MYSQL,

	/**
	 * Oracle数据库类型，代表Oracle数据库系统
	 */
	ORACLE,

	/**
	 * PostgreSQL数据库类型，代表PostgreSQL数据库系统
	 */
	POSTGRESQL,

	/**
	 * SQLite3数据库类型，代表SQLite 3.x版本的轻量级嵌入式数据库
	 */
	SQLITE3,

	/**
	 * H2数据库类型，代表H2内存或磁盘数据库系统
	 */
	H2,

	/**
	 * SQL Server数据库类型，代表Microsoft SQL Server数据库系统
	 */
	SQLSERVER,

	/**
	 * SQL Server 2012数据库类型，代表Microsoft SQL Server 2012版本的数据库系统
	 */
	SQLSERVER2012,

	/**
	 * Phoenix数据库类型，代表Apache Phoenix数据库系统（基于HBase）
	 */
	PHOENIX,

	/**
	 * 达梦数据库类型，代表国产达梦（DM）数据库系统
	 */
	DM,

	/**
	 * SAP HANA数据库类型，代表SAP HANA实时内存数据库系统
	 */
	HANA;


	/**
	 * 是否为指定数据库方言，检查时不分区大小写
	 *
	 * @param dialectName 方言名
	 * @return 是否时Oracle数据库
	 * @since 5.7.2
	 */
	public boolean match(final String dialectName) {
		return StrUtil.equalsIgnoreCase(dialectName, name());
	}
}
