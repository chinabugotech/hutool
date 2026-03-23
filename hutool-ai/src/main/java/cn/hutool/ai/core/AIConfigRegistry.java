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

package cn.hutool.ai.core;

import cn.hutool.core.util.ServiceLoaderUtil;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AIConfig实现类的加载器
 *
 * @author elichow
 * @since 5.8.38
 */
public class AIConfigRegistry {

	private static final Map<String, Class<? extends AIConfig>> configClasses = new ConcurrentHashMap<>();

	// 加载所有 AIConfig 实现类
	static {
		final ServiceLoader<AIConfig> loader = ServiceLoaderUtil.load(AIConfig.class);
		for (final AIConfig config : loader) {
			configClasses.put(config.getModelName().toLowerCase(), config.getClass());
		}
	}

	/**
	 * 根据模型名称获取AIConfig实现类
	 *
	 * @param modelName 模型名称
	 * @return AIConfig实现类
	 */
	public static Class<? extends AIConfig> getConfigClass(final String modelName) {
		return configClasses.get(modelName.toLowerCase());
	}
}
