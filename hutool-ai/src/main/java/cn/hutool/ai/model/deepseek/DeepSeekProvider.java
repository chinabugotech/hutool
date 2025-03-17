package cn.hutool.ai.model.deepseek;

import cn.hutool.ai.core.AIConfig;
import cn.hutool.ai.core.AIServiceProvider;

/**
 * 创建DeepSeek服务实现类
 *
 * @author elichow
 * @since 5.8.37
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
