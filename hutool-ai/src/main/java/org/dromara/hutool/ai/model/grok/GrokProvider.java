package org.dromara.hutool.ai.model.grok;

import org.dromara.hutool.ai.core.AIConfig;
import org.dromara.hutool.ai.core.AIServiceProvider;

/**
 * 创建Grok服务实现类
 *
 * @author elichow
 * @since 6.0.0
 */
public class GrokProvider implements AIServiceProvider {

	@Override
	public String getServiceName() {
		return "grok";
	}

	@Override
	public GrokService create(AIConfig config) {
		return new GrokServiceImpl(config);
	}
}
