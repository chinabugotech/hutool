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

package cn.hutool.v7.db.ds;

import cn.hutool.v7.db.DbException;
import cn.hutool.v7.db.config.SettingConfigParser;
import cn.hutool.v7.db.ds.pooled.PooledDSFactory;
import cn.hutool.v7.setting.Setting;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class PooledDataSourceTest {
	@Test
	void getConnTest() {
		final DSPool dsPool = new DSPool(
			new SettingConfigParser(new Setting("config/db.setting")),
			new PooledDSFactory());

		final DSWrapper test = dsPool.getDataSource("test");
		Assertions.assertEquals("cn.hutool.v7.db.ds.pooled.PooledDataSource", test.getRaw().getClass().getName());
		for (int i = 0; i < 1000; i++) {
			try {
				test.getConnection().close();
			} catch (final SQLException e) {
				throw new DbException(e);
			}
		}

	}
}
