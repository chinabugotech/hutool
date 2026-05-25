/*
 * Copyright (c) 2025 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.hutool.ai.model.deepseek;

import cn.hutool.ai.Models;
import cn.hutool.ai.core.BaseConfig;

/**
 * DeepSeek配置类，初始化API接口地址，设置默认的模型
 *
 * @author elichow
 * @since 5.8.38
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
