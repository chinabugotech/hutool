package org.dromara.hutool.ai.core;

import org.dromara.hutool.core.map.concurrent.SafeConcurrentHashMap;

import java.util.Map;
import java.util.ServiceLoader;

/**
 * AIConfig实现类的加载器
 *
 * @author elichow
 * @since 6.0.0
 */
public class AIConfigRegistry {

	private static final Map<String, Class<? extends AIConfig>> configClasses = new SafeConcurrentHashMap<>();

	// 加载所有 AIConfig 实现类
	static {
		final ServiceLoader<AIConfig> loader = ServiceLoader.load(AIConfig.class);
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
