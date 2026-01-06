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

package cn.hutool.v7.db.ds.simple;

import cn.hutool.v7.db.config.ConnectionConfig;
import cn.hutool.v7.db.ds.DSFactory;

import javax.sql.DataSource;
import java.io.Serial;

/**
 * 简单数据源工厂类
 *
 * @author Looly
 *
 */
public class SimpleDSFactory implements DSFactory {
	@Serial
	private static final long serialVersionUID = 4738029988261034743L;

	@Override
	public String getDataSourceName() {
		return "Hutool-Simple-DataSource";
	}

	@Override
	public DataSource createDataSource(final ConnectionConfig<?> config) {
		return new SimpleDataSource(config);
	}
}
