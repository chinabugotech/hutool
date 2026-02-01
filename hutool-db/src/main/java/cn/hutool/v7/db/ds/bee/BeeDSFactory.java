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

package cn.hutool.v7.db.ds.bee;

import cn.hutool.v7.core.map.MapUtil;
import cn.hutool.v7.db.config.ConnectionConfig;
import cn.hutool.v7.db.ds.AbstractDSFactory;
import cn.hutool.v7.setting.props.Props;
import org.stone.beecp.BeeDataSource;
import org.stone.beecp.BeeDataSourceConfig;

import javax.sql.DataSource;
import java.io.Serial;
import java.util.Properties;

/**
 * BeeCP数据源工厂类
 *
 * @author Looly
 */
public class BeeDSFactory extends AbstractDSFactory {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 构造
	 */
	public BeeDSFactory() {
		super(BeeDataSource.class, "BeeCP");
	}

	@Override
	public DataSource createDataSource(final ConnectionConfig<?> config) {
		final BeeDataSourceConfig beeConfig = new BeeDataSourceConfig(
			config.getDriver(), config.getUrl(), config.getUser(), config.getPass());

		// 连接池和其它选项
		Props.of(config.getPoolProps()).toBean(beeConfig);

		// 连接配置
		final Properties connProps = config.getConnProps();
		if(MapUtil.isNotEmpty(connProps)){
			connProps.forEach((key, value)->beeConfig.addConnectionFactoryProperty(key.toString(), value));
		}

		return new BeeDataSource(beeConfig);
	}
}
