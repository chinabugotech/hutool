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

package cn.hutool.ai;

import cn.hutool.ai.core.AIConfig;
import cn.hutool.ai.core.AIService;
import cn.hutool.ai.core.AIServiceProvider;
import cn.hutool.core.util.ServiceLoaderUtil;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 创建AIModelService的工厂类
 *
 * @author elichow
 * @since 5.8.38
 */
public class AIServiceFactory {

	private static final Map<String, AIServiceProvider> providers = new ConcurrentHashMap<>();

	// 加载所有 AIModelProvider 实现类
	static {
		final ServiceLoader<AIServiceProvider> loader = ServiceLoaderUtil.load(AIServiceProvider.class);
		for (final AIServiceProvider provider : loader) {
			providers.put(provider.getServiceName().toLowerCase(), provider);
		}
	}

	/**
	 * 获取AI服务
	 *
	 * @param config AIConfig配置
	 * @return AI服务实例
	 * @since 5.8.38
	 */
	public static AIService getAIService(final AIConfig config) {
		return getAIService(config, AIService.class);
	}

	/**
	 * 获取AI服务
	 *
	 * @param config AIConfig配置
	 * @param clazz AI服务类
	 * @return clazz对应的AI服务类实例
	 * @since 5.8.38
	 * @param <T> AI服务类
	 */
	@SuppressWarnings("unchecked")
	public static <T extends AIService> T getAIService(final AIConfig config, final Class<T> clazz) {
		final AIServiceProvider provider = providers.get(config.getModelName().toLowerCase());
		if (provider == null) {
			throw new IllegalArgumentException("Unsupported model: " + config.getModelName());
		}

		final AIService service = provider.create(config);
		if (!clazz.isInstance(service)) {
			throw new AIException("Model service is not of type: " + clazz.getSimpleName());
		}

		return (T) service;
	}
}
