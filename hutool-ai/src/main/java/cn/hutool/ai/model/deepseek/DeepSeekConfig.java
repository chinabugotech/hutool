package cn.hutool.ai.model.deepseek;

import cn.hutool.ai.Models;
import cn.hutool.ai.core.BaseConfig;

/**
 * DeepSeek配置类，初始化API接口地址，设置默认的模型
 *
 * @author elichow
 * @since 5.8.37
 */
public class DeepSeekConfig extends BaseConfig {

	private final String API_URL = "https://api.deepseek.com";

	private final String DEFAULT_MODEL = Models.DeepSeek.DEEPSEEK_CHAT.getModel();

	public DeepSeekConfig() {
		setApiUrl(API_URL);
		setModel(DEFAULT_MODEL);
	}

	public DeepSeekConfig(String apiKey) {
		this();
		setApiKey(apiKey);
	}

	@Override
	public String getModelName() {
		return "deepSeek";
	}

}
