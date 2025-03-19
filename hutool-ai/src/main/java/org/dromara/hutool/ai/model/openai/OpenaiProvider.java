package org.dromara.hutool.ai.model.openai;


import org.dromara.hutool.ai.core.AIConfig;
import org.dromara.hutool.ai.core.AIServiceProvider;

/**
 * 创建Openai服务实现类
 *
 * @author elichow
 * @since 6.0.0
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
