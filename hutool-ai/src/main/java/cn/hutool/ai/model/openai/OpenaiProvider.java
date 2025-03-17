package cn.hutool.ai.model.openai;

import cn.hutool.ai.core.AIConfig;
import cn.hutool.ai.core.AIServiceProvider;

/**
 * 创建Openai服务实现类
 *
 * @author elichow
 * @since 5.8.37
 */
public class OpenaiProvider implements AIServiceProvider {

	@Override
	public String getServiceName() {
		return "openai";
	}

	@Override
	public OpenaiService create(AIConfig config) {
		return new OpenaiServiceImpl(config);
	}
}
