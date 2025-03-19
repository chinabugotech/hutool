package org.dromara.hutool.ai.model.doubao;

import org.dromara.hutool.ai.core.AIConfig;
import org.dromara.hutool.ai.core.AIServiceProvider;

/**
 * 创建Doubap服务实现类
 *
 * @author elichow
 * @since 6.0.0
 */
public class DoubaoProvider implements AIServiceProvider {

	@Override
	public String getServiceName() {
		return "doubao";
	}

	@Override
	public DoubaoService create(AIConfig config) {
		return new DoubaoServiceImpl(config);
	}
}
