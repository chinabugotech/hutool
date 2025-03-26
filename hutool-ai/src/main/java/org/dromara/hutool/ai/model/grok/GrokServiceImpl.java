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

package org.dromara.hutool.ai.model.grok;

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
 * Grok服务，AI具体功能的实现
 *
 * @author elichow
 * @since 6.0.0
 */
public class GrokServiceImpl extends BaseAIService implements GrokService {

	//对话补全
	private final String CHAT_ENDPOINT = "/chat/completions";
	//创建消息回复
	private final String MESSAGES = "/messages";
	//列出模型
	private final String MODELS_ENDPOINT = "/models";
	//列出语言模型
	private final String LANGUAGE_MODELS = "/language-models";
	//分词
	private final String TOKENIZE_TEXT = "/tokenize-text";
	//获取延迟对话
	private final String DEFERRED_COMPLETION = "/chat/deferred-completion";

	public GrokServiceImpl(final AIConfig config) {
		//初始化grok客户端
		super(config);
	}

	@Override
	public String chat(String prompt) {
		// 定义消息结构
		final List<Message> messages = new ArrayList<>();
		messages.add(new Message("system", "You are a helpful assistant"));
		messages.add(new Message("user", prompt));
		return chat(messages);
	}

	@Override
	public String chat(final List<Message> messages) {
		String paramJson = buildChatRequestBody(messages);
		Response response = sendPost(CHAT_ENDPOINT, paramJson);
		return response.bodyStr();
	}

	@Override
	public String message(String prompt, int maxToken) {
		// 定义消息结构
		final List<Message> messages = new ArrayList<>();
		messages.add(new Message("system", "You are a helpful assistant"));
		messages.add(new Message("user", prompt));
		String paramJson = buildMessageRequestBody(messages, maxToken);
		Response response = sendPost(MESSAGES, paramJson);
		return response.bodyStr();
	}

	@Override
	public String chatVision(String prompt, final List<String> images, String detail) {
		String paramJson = buildChatVisionRequestBody(prompt, images, detail);
		Response response = sendPost(CHAT_ENDPOINT, paramJson);
		return response.bodyStr();
	}

	@Override
	public String models() {
		Response response = sendGet(MODELS_ENDPOINT);
		return response.bodyStr();
	}

	@Override
	public String getModel(String modelId) {
		Response response = sendGet(MODELS_ENDPOINT + "/" + modelId);
		return response.bodyStr();
	}

	@Override
	public String languageModels() {
		Response response = sendGet(LANGUAGE_MODELS);
		return response.bodyStr();
	}

	@Override
	public String getLanguageModel(String modelId) {
		Response response = sendGet(LANGUAGE_MODELS + "/" + modelId);
		return response.bodyStr();
	}

	@Override
	public String tokenizeText(String text) {
		String paramJson = buildTokenizeRequestBody(text);
		Response response = sendPost(TOKENIZE_TEXT, paramJson);
		return response.bodyStr();
	}

	@Override
	public String deferredCompletion(String requestId) {
		Response response = sendGet(DEFERRED_COMPLETION + "/" + requestId);
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

	//构建chatVision请求体
	private String buildChatVisionRequestBody(String prompt, final List<String> images, String detail) {
		// 定义消息结构
		final List<Message> messages = new ArrayList<>();
		final List<Object> content = new ArrayList<>();

		final Map<String, String> contentMap = new HashMap<>();
		contentMap.put("type", "text");
		contentMap.put("text", prompt);
		content.add(contentMap);
		for (String img : images) {
			HashMap<String, Object> imgUrlMap = new HashMap<>();
			imgUrlMap.put("type", "image_url");
			HashMap<String, String> urlMap = new HashMap<>();
			urlMap.put("url", img);
			urlMap.put("detail", detail);
			imgUrlMap.put("image_url", urlMap);
			content.add(imgUrlMap);
		}

		messages.add(new Message("user", content));

		//使用JSON工具
		final Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("model", config.getModel());
		paramMap.put("messages", messages);
		//合并其他参数
		paramMap.putAll(config.getAdditionalConfigMap());
		return JSONUtil.toJsonStr(paramMap);
	}

	//构建消息回复请求体
	private String buildMessageRequestBody(final List<Message> messages, int maxToken) {
		final Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("model", config.getModel());
		paramMap.put("messages", messages);
		paramMap.put("max_tokens", maxToken);
		//合并其他参数
		paramMap.putAll(config.getAdditionalConfigMap());

		return JSONUtil.toJsonStr(paramMap);
	}

	//构建分词请求体
	private String buildTokenizeRequestBody(String text) {
		//使用JSON工具
		final Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("model", config.getModel());
		paramMap.put("text", text);
		//合并其他参数
		paramMap.putAll(config.getAdditionalConfigMap());

		return JSONUtil.toJsonStr(paramMap);
	}
}
