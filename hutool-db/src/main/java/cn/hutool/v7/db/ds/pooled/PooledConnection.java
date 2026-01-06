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

package cn.hutool.v7.db.ds.pooled;

import cn.hutool.v7.core.io.IoUtil;
import cn.hutool.v7.core.map.MapUtil;
import cn.hutool.v7.core.text.StrUtil;
import cn.hutool.v7.db.DbException;
import cn.hutool.v7.db.config.ConnectionConfig;
import cn.hutool.v7.setting.props.Props;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 池化
 *
 * @author Looly
 */
public class PooledConnection extends ConnectionWrapper {

	private final PooledDataSource dataSource;
	/**
	 * 仅用于记录连接在池中的状态，如归还到池中为close状态，从池中拿出需调用{@link #open()}变为可用状态
	 */
	private boolean isClosed = false;

	/**
	 * 构造
	 *
	 * @param config     数据库配置
	 * @param dataSource 数据源
	 */
	public PooledConnection(final ConnectionConfig<?> config, final PooledDataSource dataSource) {
		// issue#IA6EUQ 部分驱动无法自动加载，此处手动完成
		final String driver = config.getDriver();
		if (StrUtil.isNotBlank(driver)) {
			try {
				Class.forName(driver);
			} catch (final ClassNotFoundException e) {
				throw new DbException(e);
			}
		}

		final Props info = new Props();
		final String user = config.getUser();
		if (user != null) {
			info.setProperty("user", user);
		}
		final String password = config.getPass();
		if (password != null) {
			info.setProperty("password", password);
		}

		// 其它参数
		final Properties connProps = config.getConnProps();
		if (MapUtil.isNotEmpty(connProps)) {
			info.putAll(connProps);
		}

		try {
			if (null != dataSource.driver) {
				this.raw = dataSource.driver.connect(config.getUrl(), info);
			} else {
				this.raw = DriverManager.getConnection(config.getUrl(), info);
			}
		} catch (final SQLException e) {
			throw new DbException(e);
		}

		this.dataSource = dataSource;
	}

	/**
	 * 归还连接<br>
	 * 关闭操作在池中的意义为使用完毕，归还到池中<br>
	 * 如果想彻底关闭连接，请使用{@link #destroy()}方法
	 */
	@Override
	public void close() {
		this.isClosed = true;
		dataSource.returnObject(this);
	}

	@Override
	public boolean isClosed() {
		return this.isClosed;
	}

	/**
	 * 打开连接<br>
	 * 仅用于从连接池中拿出时调用，使链接变为可用。
	 *
	 * @return this
	 */
	PooledConnection open() {
		this.isClosed = false;
		return this;
	}

	/**
	 * 销毁连接，即彻底关闭并丢弃连接
	 */
	public void destroy() {
		this.isClosed = true;
		IoUtil.closeQuietly(this.raw);
	}
}
