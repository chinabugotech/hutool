package cn.hutool.ai;

import cn.hutool.ai.core.AIConfig;
import cn.hutool.ai.core.AIServiceProvider;
import cn.hutool.ai.core.AIService;
import cn.hutool.core.map.SafeConcurrentHashMap;

import java.util.Map;
import java.util.ServiceLoader;

/**
 * 创建AIModelService的工厂类
 *
 * @author elichow
 * @since 5.8.37
 */
public class AIServiceFactory {

	private static final Map<String, AIServiceProvider> providers = new SafeConcurrentHashMap<>();

	// 加载所有 AIModelProvider 实现类
	static {
		ServiceLoader<AIServiceProvider> loader = ServiceLoader.load(AIServiceProvider.class);
		for (AIServiceProvider provider : loader) {
			providers.put(provider.getServiceName().toLowerCase(), provider);
		}
	}

	/**
	 * 获取AI服务
	 *
	 * @param config AIConfig配置
	 * @return AI服务实例
	 * @since 5.8.37
	 */
	public static AIService getAIService(AIConfig config) {
		return getAIService(config, AIService.class);
	}

	/**
	 * 获取AI服务
	 *
	 * @param config AIConfig配置
	 * @param clazz AI服务类
	 * @return clazz对应的AI服务类实例
	 * @since 5.8.37
	 */
	public static <T extends AIService> T getAIService(AIConfig config, Class<T> clazz) {
		AIServiceProvider provider = providers.get(config.getModelName().toLowerCase());
		if (provider == null) {
			throw new IllegalArgumentException("Unsupported model: " + config.getModelName());
		}

		AIService service = provider.create(config);
		if (!clazz.isInstance(service)) {
			throw new AIException("Model service is not of type: " + clazz.getSimpleName());
		}

		return (T) service;
	}
}
