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

package cn.hutool.v7.db.ds;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import cn.hutool.v7.core.collection.CollUtil;
import cn.hutool.v7.db.Db;
import cn.hutool.v7.db.Entity;
import cn.hutool.v7.db.ds.bee.BeeDSFactory;
import cn.hutool.v7.db.ds.c3p0.C3p0DSFactory;
import cn.hutool.v7.db.ds.dbcp.DbcpDSFactory;
import cn.hutool.v7.db.ds.druid.DruidDSFactory;
import cn.hutool.v7.db.ds.hikari.HikariDSFactory;
import cn.hutool.v7.db.ds.pooled.PooledDSFactory;
import cn.hutool.v7.db.ds.tomcat.TomcatDSFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.util.List;

/**
 * 数据源单元测试
 *
 * @author Looly
 *
 */
public class DsTest {

	@Test
	public void defaultDsTest() {
		final DataSource ds = DSUtil.getDS("test");
		final Db db = Db.of(ds);
		final List<Entity> all = db.findAll("user");
		Assertions.assertTrue(CollUtil.isNotEmpty(all));
	}

	@Test
	public void hikariDsTest() {
		DSUtil.setGlobalDSFactory(new HikariDSFactory());
		final DataSource ds = DSUtil.getDS("test");
		final Db db = Db.of(ds);
		final List<Entity> all = db.findAll("user");
		Assertions.assertTrue(CollUtil.isNotEmpty(all));
	}

	@Test
	public void druidDsTest() {
		DSUtil.setGlobalDSFactory(new DruidDSFactory());
		final DataSource ds = DSUtil.getDS("test");

		final Db db = Db.of(ds);
		final List<Entity> all = db.findAll("user");
		Assertions.assertTrue(CollUtil.isNotEmpty(all));
	}

	@Test
	public void tomcatDsTest() {
		DSUtil.setGlobalDSFactory(new TomcatDSFactory());
		final DataSource ds = DSUtil.getDS("test");
		final Db db = Db.of(ds);
		final List<Entity> all = db.findAll("user");
		Assertions.assertTrue(CollUtil.isNotEmpty(all));
	}

	@Test
	public void beeCPDsTest() {
		DSUtil.setGlobalDSFactory(new BeeDSFactory());
		final DataSource ds = DSUtil.getDS("test");
		final Db db = Db.of(ds);
		final List<Entity> all = db.findAll("user");
		Assertions.assertTrue(CollUtil.isNotEmpty(all));
	}

	@Test
	public void dbcpDsTest() {
		DSUtil.setGlobalDSFactory(new DbcpDSFactory());
		final DataSource ds = DSUtil.getDS("test");
		final Db db = Db.of(ds);
		final List<Entity> all = db.findAll("user");
		Assertions.assertTrue(CollUtil.isNotEmpty(all));
	}

	@Test
	public void c3p0DsTest() {
		DSUtil.setGlobalDSFactory(new C3p0DSFactory());
		final DataSource ds = DSUtil.getDS("test");
		final Db db = Db.of(ds);
		final List<Entity> all = db.findAll("user");
		Assertions.assertTrue(CollUtil.isNotEmpty(all));
	}

	@Test
	public void c3p0DsuserAndPassTest() {
		// https://gitee.com/chinabugotech/hutool/issues/I4T7XZ
		DSUtil.setGlobalDSFactory(new C3p0DSFactory());
		final ComboPooledDataSource ds = (ComboPooledDataSource) DSUtil.getDS("mysql").getRaw();
		Assertions.assertEquals("root", ds.getUser());
		Assertions.assertEquals("123456", ds.getPassword());
	}

	@Test
	public void hutoolPoolTest() {
		DSUtil.setGlobalDSFactory(new PooledDSFactory());
		final DataSource ds = DSUtil.getDS("test");
		final Db db = Db.of(ds);
		final List<Entity> all = db.findAll("user");
		Assertions.assertTrue(CollUtil.isNotEmpty(all));
	}
}
