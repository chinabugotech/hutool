package cn.hutool.ai.model.grok;

import cn.hutool.ai.core.AIConfig;
import cn.hutool.ai.core.AIServiceProvider;

/**
 * 创建Grok服务实现类
 *
 * @author elichow
 * @since 5.8.37
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
