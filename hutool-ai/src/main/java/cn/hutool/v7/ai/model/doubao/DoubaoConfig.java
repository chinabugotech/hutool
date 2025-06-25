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

package cn.hutool.v7.ai.model.doubao;

import cn.hutool.v7.ai.Models;
import cn.hutool.v7.ai.core.BaseConfig;

/**
 * Doubao配置类，初始化API接口地址，设置默认的模型
 *
 * @author elichow
 * @since 6.0.0
 */
public class DoubaoConfig extends BaseConfig {

	// 定义API的基础URL，用于和服务器通信
	private static final String API_URL = "https://ark.cn-beijing.volces.com/api/v3";

	// 定义默认的模型配置，用于初始化配置对象时设定
	private static final String DEFAULT_MODEL = Models.Doubao.DOUBAO_1_5_LITE_32K.getModel();

	/**
	 * 无参构造函数，用于创建DoubaoConfig对象
	 * 初始化时会设置API_URL和DEFAULT_MODEL
	 */
	public DoubaoConfig() {
		setApiUrl(API_URL);
		setModel(DEFAULT_MODEL);
	}

	/**
	 * 带有apiKey参数的构造函数，用于创建DoubaoConfig对象并设置API密钥
	 * 初始化时会设置API_URL、DEFAULT_MODEL以及传入的apiKey
	 *
	 * @param apiKey 用户的API密钥，用于验证用户身份
	 */
	public DoubaoConfig(final String apiKey) {
		this(); // 先调用无参构造函数初始化API_URL和DEFAULT_MODEL
		setApiKey(apiKey); // 设置用户的API密钥
	}


	@Override
	public String getModelName() {
		return "doubao";
	}

}
