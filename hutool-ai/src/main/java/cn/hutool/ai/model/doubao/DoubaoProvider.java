package cn.hutool.ai.model.doubao;

import cn.hutool.ai.core.AIConfig;
import cn.hutool.ai.core.AIServiceProvider;

/**
 * 创建Doubap服务实现类
 *
 * @author elichow
 * @since 5.8.37
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
