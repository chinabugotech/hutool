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

package cn.hutool.v7.extra.aop.engine;

import cn.hutool.v7.core.lang.Singleton;
import cn.hutool.v7.core.spi.SpiUtil;
import cn.hutool.v7.core.text.StrUtil;
import cn.hutool.v7.log.LogUtil;

/**
 * 代理引擎简单工厂<br>
 * 根据用户引入代理库的不同，产生不同的代理引擎对象
 *
 * @author Looly
 */
public class ProxyEngineFactory {

	/**
	 * 获得单例的ProxyEngine
	 *
	 * @return 单例的ProxyEngine
	 */
	public static ProxyEngine getEngine() {
		final ProxyEngine engine = Singleton.get(ProxyEngine.class.getName(), ProxyEngineFactory::createEngine);
		LogUtil.debug("Use [{}] Engine As Default.", StrUtil.removeSuffix(engine.getClass().getSimpleName(), "Engine"));
		return engine;
	}

	/**
	 * 根据用户引入Cglib与否创建代理工厂
	 *
	 * @return 代理工厂
	 */
	public static ProxyEngine createEngine() {
		return SpiUtil.loadFirstAvailable(ProxyEngine.class);
	}
}
