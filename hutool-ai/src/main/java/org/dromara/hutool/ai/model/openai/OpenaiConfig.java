package org.dromara.hutool.ai.model.openai;


import org.dromara.hutool.ai.Models;
import org.dromara.hutool.ai.core.BaseConfig;

/**
 * openai配置类，初始化API接口地址，设置默认的模型
 *
 * @author elichow
 * @since 6.0.0
 */
public class OpenaiConfig extends BaseConfig {

	private final String API_URL = "https://api.openai.com/v1";

	private final String DEFAULT_MODEL = Models.Openai.GPT_4O.getModel();

	public OpenaiConfig() {
		setApiUrl(API_URL);
		setModel(DEFAULT_MODEL);
	}

	public OpenaiConfig(String apiKey) {
		this();
		setApiKey(apiKey);
	}

	@Override
	public String getModelName() {
		return "openai";
	}

}
