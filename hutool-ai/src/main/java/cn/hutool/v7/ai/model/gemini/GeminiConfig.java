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

package cn.hutool.v7.ai.model.gemini;

import cn.hutool.v7.ai.Models;
import cn.hutool.v7.ai.core.BaseAIConfig;


/**
 * Gemini配置类，初始化API接口地址，设置默认的模型
 *
 * @author elichow
 * @since 7.0.0
 */
public class GeminiConfig extends BaseAIConfig {

	// Google Generative AI 的基础 URL
	private final String API_URL = "https://generativelanguage.googleapis.com/v1beta";

	// 默认模型
	private final String DEFAULT_MODEL = Models.Gemini.GEMINI_2_5_FLASH.getModel();

	public GeminiConfig() {
		setApiUrl(API_URL);
		setModel(DEFAULT_MODEL);
	}

	public GeminiConfig(final String apiKey) {
		this();
		setApiKey(apiKey);
	}

	@Override
	public String getModelName() {
		return "gemini";
	}

}
