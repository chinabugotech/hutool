package org.dromara.hutool.ai.model.deepseek;


import org.dromara.hutool.ai.core.AIConfig;
import org.dromara.hutool.ai.core.AIServiceProvider;

/**
 * 创建DeepSeek服务实现类
 *
 * @author elichow
 * @since 6.0.0
 */
public class DeepSeekProvider implements AIServiceProvider {

	@Override
	public String getServiceName() {
		return "deepSeek";
	}

	@Override
	public DeepSeekService create(AIConfig config) {
		return new DeepSeekServiceImpl(config);
	}
}
