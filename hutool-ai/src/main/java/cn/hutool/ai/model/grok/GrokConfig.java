package cn.hutool.ai.model.grok;

import cn.hutool.ai.Models;
import cn.hutool.ai.core.BaseConfig;

/**
 * Grok配置类，初始化API接口地址，设置默认的模型
 *
 * @author elichow
 * @since 5.8.37
 */
public class GrokConfig extends BaseConfig {

	private final String API_URL = "https://api.x.ai/v1";

	private final String DEFAULT_MODEL = Models.Grok.GROK_2_1212.getModel();


	public GrokConfig() {
		setApiUrl(API_URL);
		setModel(DEFAULT_MODEL);
	}

	public GrokConfig(String apiKey) {
		this();
		setApiKey(apiKey);
	}

	@Override
	public String getModelName() {
		return "grok";
	}

}
