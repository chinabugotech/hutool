package org.dromara.hutool.ai.model.doubao;

import org.dromara.hutool.ai.Models;
import org.dromara.hutool.ai.core.BaseConfig;

/**
 * Doubao配置类，初始化API接口地址，设置默认的模型
 *
 * @author elichow
 * @since 6.0.0
 */
public class DoubaoConfig extends BaseConfig {

	private final String API_URL = "https://ark.cn-beijing.volces.com/api/v3";

	private final String DEFAULT_MODEL = Models.Doubao.DOUBAO_1_5_LITE_32K.getModel();

	public DoubaoConfig() {
		setApiUrl(API_URL);
		setModel(DEFAULT_MODEL);
	}

	public DoubaoConfig(String apiKey) {
		this();
		setApiKey(apiKey);
	}

	@Override
	public String getModelName() {
		return "doubao";
	}

}
