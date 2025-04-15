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

import cn.hutool.v7.core.lang.Assert;

import javax.sql.DataSource;

/**
 * 抽象数据源工厂
 *
 * @author Looly
 */
public abstract class AbstractDSFactory implements DSFactory {
	private static final long serialVersionUID = 1L;

	private final String dataSourceName;

	/**
	 * 构造
	 *
	 * @param dataSourceClass 数据库连接池实现类，用于检测所提供的DataSource类是否存在，当传入的DataSource类不存在时抛出ClassNotFoundException<br>
	 *                        此参数的作用是在detectDSFactory方法自动检测所用连接池时，如果实现类不存在，调用此方法会自动抛出异常，从而切换到下一种连接池的检测。
	 * @param dataSourceName  数据源名称
	 */
	public AbstractDSFactory(final Class<? extends DataSource> dataSourceClass, final String dataSourceName) {
		//此参数的作用是在detectDSFactory方法自动检测所用连接池时，如果实现类不存在，调用此方法会自动抛出异常，从而切换到下一种连接池的检测。
		Assert.notNull(dataSourceClass);
		this.dataSourceName = dataSourceName;
	}

	@Override
	public String getDataSourceName() {
		return this.dataSourceName;
	}
}
