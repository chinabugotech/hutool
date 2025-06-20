/*
 * Copyright (c) 2025 Hutool Team and hutool.cn
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

/**
 *
 * @author choweli
 */
module hutool.db {

	exports cn.hutool.v7.db;
	exports cn.hutool.v7.db.config;
	exports cn.hutool.v7.db.handler;
	exports cn.hutool.v7.db.dialect;
	exports cn.hutool.v7.db.sql;
	exports cn.hutool.v7.db.sql.filter;
	exports cn.hutool.v7.db.ds;
	exports cn.hutool.v7.db.ds.bee;
	exports cn.hutool.v7.db.ds.c3p0;
	exports cn.hutool.v7.db.ds.dbcp;
	exports cn.hutool.v7.db.ds.druid;
	exports cn.hutool.v7.db.ds.hikari;
	exports cn.hutool.v7.db.ds.jndi;
	exports cn.hutool.v7.db.ds.pooled;
	exports cn.hutool.v7.db.ds.simple;
	exports cn.hutool.v7.db.ds.tomcat;

	requires hutool.setting;
	requires hutool.log;
	requires hutool.core;
	requires beecp;
	requires com.mchange.v2.c3p0;
	requires java.desktop;
	requires org.apache.commons.dbcp2;
	requires druid;
	requires com.zaxxer.hikari;
	requires java.sql;
	requires java.naming;
	requires tomcat.jdbc;

	opens cn.hutool.v7.db;
	opens cn.hutool.v7.db.config;
	opens cn.hutool.v7.db.pojo;

}
