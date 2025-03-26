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
		ServiceLoader<AIConfig> loader = ServiceLoader.load(AIConfig.class);
		for (AIConfig config : loader) {
			configClasses.put(config.getModelName().toLowerCase(), config.getClass());
		}
	}

	public static Class<? extends AIConfig> getConfigClass(String modelName) {
		return configClasses.get(modelName.toLowerCase());
	}
}
