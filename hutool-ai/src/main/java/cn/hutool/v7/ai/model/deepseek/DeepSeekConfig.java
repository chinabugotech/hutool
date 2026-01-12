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

package cn.hutool.v7.ai.model.deepseek;

import cn.hutool.v7.ai.Models;
import cn.hutool.v7.ai.core.BaseAIConfig;

/**
 * DeepSeek配置类，初始化API接口地址，设置默认的模型
 *
 * @author elichow
 * @since 7.0.0
 */
public class DeepSeekConfig extends BaseAIConfig {

	/**
	 * 定义API的基础URL，用于后续的所有API请求
	 */
	public final String API_URL = "https://api.deepseek.com";

	/**
	 * 定义默认的模型名称，用于在没有指定模型时使用
	 */
	public final String DEFAULT_MODEL = Models.DeepSeek.DEEPSEEK_CHAT.getModel();

	/**
	 * 默认构造函数，用于初始化DeepSeek配置对象
	 * 设置API的基础URL和默认的模型名称
	 */
	public DeepSeekConfig() {
		setApiUrl(API_URL);
		setModel(DEFAULT_MODEL);
	}

	/**
	 * 带API密钥参数的构造函数
	 * 用于初始化DeepSeek配置对象，并设置API密钥
	 *
	 * @param apiKey 用户的API密钥，用于认证和授权
	 */
	public DeepSeekConfig(final String apiKey) {
		this(); // 调用默认构造函数初始化API_URL和DEFAULT_MODEL
		setApiKey(apiKey);
	}


	@Override
	public String getModelName() {
		return "deepSeek";
	}

}
