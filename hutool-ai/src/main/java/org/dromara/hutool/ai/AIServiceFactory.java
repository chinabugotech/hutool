package org.dromara.hutool.ai;

import org.dromara.hutool.ai.core.AIConfig;
import org.dromara.hutool.ai.core.AIService;
import org.dromara.hutool.ai.core.AIServiceProvider;
import org.dromara.hutool.core.map.concurrent.SafeConcurrentHashMap;

import java.util.Map;
import java.util.ServiceLoader;

/**
 * 创建AIModelService的工厂类
 *
 * @author elichow
 * @since 6.0.0
 */
public class AIServiceFactory {

	private static final Map<String, AIServiceProvider> providers = new SafeConcurrentHashMap<>();

	// 加载所有 AIModelProvider 实现类
	static {
		final ServiceLoader<AIServiceProvider> loader = ServiceLoader.load(AIServiceProvider.class);
		for (final AIServiceProvider provider : loader) {
			providers.put(provider.getServiceName().toLowerCase(), provider);
		}
	}

	/**
	 * 获取AI服务
	 *
	 * @param config AIConfig配置
	 * @return AI服务实例
	 * @since 6.0.0
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
	 * @since 6.0.0
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
