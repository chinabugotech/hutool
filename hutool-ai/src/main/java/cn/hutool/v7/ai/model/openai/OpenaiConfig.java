/*
 * Copyright (c) 2013-2026 Hutool Team.
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

package cn.hutool.v7.ai.model.openai;

import cn.hutool.v7.ai.Models;
import cn.hutool.v7.ai.core.BaseAIConfig;

/**
 * openai配置类，初始化API接口地址，设置默认的模型
 *
 * @author elichow
 * @since 7.0.0
 */
public class OpenaiConfig extends BaseAIConfig {

	private final String API_URL = "https://api.openai.com/v1";

	private final String DEFAULT_MODEL = Models.Openai.GPT_4O.getModel();

	public OpenaiConfig() {
		setApiUrl(API_URL);
		setModel(DEFAULT_MODEL);
	}

	public OpenaiConfig(final String apiKey) {
		this();
		setApiKey(apiKey);
	}

	@Override
	public String getModelName() {
		return "openai";
	}

}
