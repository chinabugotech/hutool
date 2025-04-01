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

package org.dromara.hutool.ai.model.deepseek;

import org.dromara.hutool.ai.core.AIConfig;
import org.dromara.hutool.ai.core.BaseAIService;
import org.dromara.hutool.ai.core.Message;
import org.dromara.hutool.http.client.Response;
import org.dromara.hutool.json.JSONUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DeepSeek服务，AI具体功能的实现
 *
 * @author elichow
 * @since 6.0.0
 */
public class DeepSeekServiceImpl extends BaseAIService implements DeepSeekService {

	//对话补全
	private final String CHAT_ENDPOINT = "/chat/completions";
	//FIM补全（beta）
	private final String BETA_ENDPOINT = "/beta/completions";
	//列出模型
	private final String MODELS_ENDPOINT = "/models";
	//余额查询
	private final String BALANCE_ENDPOINT = "/user/balance";

	/**
	 * 构造函数
	 *
	 * @param config AI配置
	 */
	public DeepSeekServiceImpl(final AIConfig config) {
		//初始化DeepSeek客户端
		super(config);
	}

	@Override
	public String chat(final String prompt) {
		// 定义消息结构
		final List<Message> messages = new ArrayList<>();
		messages.add(new Message("system", "You are a helpful assistant"));
		messages.add(new Message("user", prompt));
		return chat(messages);
	}

	@Override
	public String chat(final List<Message> messages) {
		final String paramJson = buildChatRequestBody(messages);
		final Response response = sendPost(CHAT_ENDPOINT, paramJson);
		return response.bodyStr();
	}

	@Override
	public String beta(final String prompt) {
		final String paramJson = buildBetaRequestBody(prompt);
		final Response response = sendPost(BETA_ENDPOINT, paramJson);
		return response.bodyStr();
	}

	@Override
	public String models() {
		final Response response = sendGet(MODELS_ENDPOINT);
		return response.bodyStr();
	}

	@Override
	public String balance() {
		final Response response = sendGet(BALANCE_ENDPOINT);
		return response.bodyStr();
	}

	// 构建chat请求体
	private String buildChatRequestBody(final List<Message> messages) {
		//使用JSON工具
		final Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("model", config.getModel());
		paramMap.put("messages", messages);
		//合并其他参数
		paramMap.putAll(config.getAdditionalConfigMap());

		return JSONUtil.toJsonStr(paramMap);
	}

	// 构建beta请求体
	private String buildBetaRequestBody(final String prompt) {
		// 定义消息结构
		//使用JSON工具
		final Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("model", config.getModel());
		paramMap.put("prompt", prompt);
//		//合并其他参数
		paramMap.putAll(config.getAdditionalConfigMap());

		return JSONUtil.toJsonStr(paramMap);
	}

}
