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

package cn.hutool.v7.extra.mq.engine;

import cn.hutool.v7.core.reflect.ConstructorUtil;
import cn.hutool.v7.core.spi.SpiUtil;
import cn.hutool.v7.extra.mq.MQConfig;
import cn.hutool.v7.extra.mq.MQException;

/**
 * MQ引擎工厂类
 *
 * @author huangchengxing
 * @since 1.0.0
 */
public class MQEngineFactory {

	/**
	 * 根据用户引入的MQ引擎jar，自动创建对应的模板引擎对象<br>
	 * 推荐创建的引擎单例使用，此方法每次调用会返回新的引擎
	 *
	 * @param config MQ配置
	 * @return {@link MQEngine}
	 */
	public static MQEngine createEngine(final MQConfig config) {
		return doCreateEngine(config);
	}

	/**
	 * 根据用户引入的MQ引擎jar，自动创建对应的MQ引擎对象
	 *
	 * @param config MQ配置
	 * @return {@link MQEngine}
	 */
	private static MQEngine doCreateEngine(final MQConfig config) {
		final Class<? extends MQEngine> customEngineClass = config.getCustomEngine();
		final MQEngine engine;
		if (null != customEngineClass) {
			// 自定义模板引擎
			engine = ConstructorUtil.newInstance(customEngineClass);
		} else {
			// SPI引擎查找
			engine = SpiUtil.loadFirstAvailable(MQEngine.class);
		}
		if (null != engine) {
			return engine.init(config);
		}

		throw new MQException("No MQ implement found! Please add one of MQ jar to your project !");
	}
}
