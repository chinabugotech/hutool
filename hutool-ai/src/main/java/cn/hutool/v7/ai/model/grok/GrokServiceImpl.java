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

package cn.hutool.v7.ai.model.grok;

import cn.hutool.v7.ai.core.AIConfig;
import cn.hutool.v7.ai.core.BaseAIService;
import cn.hutool.v7.ai.core.Message;
import cn.hutool.v7.core.thread.ThreadUtil;
import cn.hutool.v7.http.client.Response;
import cn.hutool.v7.json.JSONUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Grok服务，AI具体功能的实现
 *
 * @author elichow
 * @since 7.0.0
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
	//文生图
	private final String IMAGES_GENERATIONS = "/images/generations";

	/**
	 * 构造函数
	 *
	 * @param config 配置
	 */
	public GrokServiceImpl(final AIConfig config) {
		//初始化grok客户端
		super(config);
	}

	@Override
	public String chat(final List<Message> messages) {
		final String paramJson = buildChatRequestBody(messages);
		final Response response = sendPost(CHAT_ENDPOINT, paramJson);
		return response.bodyStr();
	}

	@Override
	public void chat(final List<Message> messages, final Consumer<String> callback) {
		final Map<String, Object> paramMap = buildChatStreamRequestBody(messages);
		ThreadUtil.newThread(() -> sendPostStream(CHAT_ENDPOINT, paramMap, callback), "grok-chat-sse").start();
	}

	@Override
	public String message(final List<Message> messages, final int maxToken) {
		final String paramJson = buildMessageRequestBody(messages, maxToken);
		final Response response = sendPost(MESSAGES, paramJson);
		return response.bodyStr();
	}

	@Override
	public void message(final List<Message> messages, final int maxToken, final Consumer<String> callback) {
		final Map<String, Object> paramMap = buildMessageStreamRequestBody(messages, maxToken);
		ThreadUtil.newThread(() -> sendPostStream(MESSAGES, paramMap, callback), "grok-message-sse").start();
	}

	@Override
	public String chatVision(final String prompt, final List<String> images, final String detail) {
		final String paramJson = buildChatVisionRequestBody(prompt, images, detail);
		final Response response = sendPost(CHAT_ENDPOINT, paramJson);
		return response.bodyStr();
	}

	@Override
	public void chatVision(final String prompt, final List<String> images, final String detail, final Consumer<String> callback) {
		final Map<String, Object> paramMap = buildChatVisionStreamRequestBody(prompt, images, detail);
		ThreadUtil.newThread(() -> sendPostStream(CHAT_ENDPOINT, paramMap, callback), "grok-chatVision-sse").start();
	}

	@Override
	public String models() {
		final Response response = sendGet(MODELS_ENDPOINT);
		return response.bodyStr();
	}

	@Override
	public String getModel(final String modelId) {
		final Response response = sendGet(MODELS_ENDPOINT + "/" + modelId);
		return response.bodyStr();
	}

	@Override
	public String languageModels() {
		final Response response = sendGet(LANGUAGE_MODELS);
		return response.bodyStr();
	}

	@Override
	public String getLanguageModel(final String modelId) {
		final Response response = sendGet(LANGUAGE_MODELS + "/" + modelId);
		return response.bodyStr();
	}

	@Override
	public String tokenizeText(final String text) {
		final String paramJson = buildTokenizeRequestBody(text);
		final Response response = sendPost(TOKENIZE_TEXT, paramJson);
		return response.bodyStr();
	}

	@Override
	public String deferredCompletion(final String requestId) {
		final Response response = sendGet(DEFERRED_COMPLETION + "/" + requestId);
		return response.bodyStr();
	}

	@Override
	public String imagesGenerations(final String prompt) {
		final String paramJson = buildImagesGenerationsRequestBody(prompt);
		final Response response = sendPost(IMAGES_GENERATIONS, paramJson);
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

	private Map<String, Object> buildChatStreamRequestBody(final List<Message> messages) {
		//使用JSON工具
		final Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("stream", true);
		paramMap.put("model", config.getModel());
		paramMap.put("messages", messages);
		//合并其他参数
		paramMap.putAll(config.getAdditionalConfigMap());

		return paramMap;
	}

	//构建chatVision请求体
	private String buildChatVisionRequestBody(final String prompt, final List<String> images, final String detail) {
		// 定义消息结构
		final List<Message> messages = new ArrayList<>();
		final List<Object> content = new ArrayList<>();

		final Map<String, String> contentMap = new HashMap<>();
		contentMap.put("type", "text");
		contentMap.put("text", prompt);
		content.add(contentMap);
		for (final String img : images) {
			final HashMap<String, Object> imgUrlMap = new HashMap<>();
			imgUrlMap.put("type", "image_url");
			final HashMap<String, String> urlMap = new HashMap<>();
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

	private Map<String, Object> buildChatVisionStreamRequestBody(final String prompt, final List<String> images, final String detail) {
		// 定义消息结构
		final List<Message> messages = new ArrayList<>();
		final List<Object> content = new ArrayList<>();

		final Map<String, String> contentMap = new HashMap<>();
		contentMap.put("type", "text");
		contentMap.put("text", prompt);
		content.add(contentMap);
		for (final String img : images) {
			final HashMap<String, Object> imgUrlMap = new HashMap<>();
			imgUrlMap.put("type", "image_url");
			final HashMap<String, String> urlMap = new HashMap<>();
			urlMap.put("url", img);
			urlMap.put("detail", detail);
			imgUrlMap.put("image_url", urlMap);
			content.add(imgUrlMap);
		}

		messages.add(new Message("user", content));

		//使用JSON工具
		final Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("stream", true);
		paramMap.put("model", config.getModel());
		paramMap.put("messages", messages);
		//合并其他参数
		paramMap.putAll(config.getAdditionalConfigMap());
		return paramMap;
	}

	//构建消息回复请求体
	private String buildMessageRequestBody(final List<Message> messages, final int maxToken) {
		final Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("model", config.getModel());
		paramMap.put("messages", messages);
		paramMap.put("max_tokens", maxToken);
		//合并其他参数
		paramMap.putAll(config.getAdditionalConfigMap());

		return JSONUtil.toJsonStr(paramMap);
	}

	private Map<String, Object> buildMessageStreamRequestBody(final List<Message> messages, final int maxToken) {
		final Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("stream", true);
		paramMap.put("model", config.getModel());
		paramMap.put("messages", messages);
		paramMap.put("max_tokens", maxToken);
		//合并其他参数
		paramMap.putAll(config.getAdditionalConfigMap());

		return paramMap;
	}

	//构建分词请求体
	private String buildTokenizeRequestBody(final String text) {
		//使用JSON工具
		final Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("model", config.getModel());
		paramMap.put("text", text);
		//合并其他参数
		paramMap.putAll(config.getAdditionalConfigMap());

		return JSONUtil.toJsonStr(paramMap);
	}

	//构建文生图请求体
	private String buildImagesGenerationsRequestBody(final String prompt) {
		final Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("model", config.getModel());
		paramMap.put("prompt", prompt);
		//合并其他参数
		paramMap.putAll(config.getAdditionalConfigMap());

		return JSONUtil.toJsonStr(paramMap);
	}
}
