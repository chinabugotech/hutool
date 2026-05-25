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

package cn.hutool.ai.model.openai;

import cn.hutool.ai.core.AIConfig;
import cn.hutool.ai.core.BaseAIService;
import cn.hutool.ai.core.Message;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * openai服务，AI具体功能的实现
 *
 * @author elichow
 * @since 5.8.38
 */
public class OpenaiServiceImpl extends BaseAIService implements OpenaiService {

	//对话
	private final String CHAT_ENDPOINT = "/chat/completions";
	//文生图
	private final String IMAGES_GENERATIONS = "/images/generations";
	//图片编辑
	private final String IMAGES_EDITS = "/images/edits";
	//图片变形
	private final String IMAGES_VARIATIONS = "/images/variations";
	//文本转语音
	private final String TTS = "/audio/speech";
	//语音转文本
	private final String STT = "/audio/transcriptions";
	//文本向量化
	private final String EMBEDDINGS = "/embeddings";
	//检查文本或图片
	private final String MODERATIONS = "/moderations";

	public OpenaiServiceImpl(final AIConfig config) {
		//初始化Openai客户端
		super(config);
	}

	@Override
	public String chat(final List<Message> messages) {
		String paramJson = buildChatRequestBody(messages);
		final HttpResponse response = sendPost(CHAT_ENDPOINT, paramJson);
		return response.body();
	}

	@Override
	public void chat(List<Message> messages,Consumer<String> callback) {
		Map<String, Object> paramMap = buildChatStreamRequestBody(messages);
		ThreadUtil.newThread(() -> sendPostStream(CHAT_ENDPOINT, paramMap, callback::accept), "openai-chat-sse").start();
	}

	@Override
	public String chatVision(String prompt, final List<String> images, String detail) {
		String paramJson = buildChatVisionRequestBody(prompt, images, detail);
		final HttpResponse response = sendPost(CHAT_ENDPOINT, paramJson);
		return response.body();
	}

	@Override
	public void chatVision(String prompt, List<String> images, String detail, Consumer<String> callback) {
		Map<String, Object> paramMap = buildChatVisionStreamRequestBody(prompt, images, detail);
		ThreadUtil.newThread(() -> sendPostStream(CHAT_ENDPOINT, paramMap, callback::accept), "openai-chatVision-sse").start();
	}

	@Override
	public String imagesGenerations(String prompt) {
		String paramJson = buildImagesGenerationsRequestBody(prompt);
		final HttpResponse response = sendPost(IMAGES_GENERATIONS, paramJson);
		return response.body();
	}

	@Override
	public String imagesEdits(String prompt, final File image, final File mask) {
		final Map<String, Object> paramMap = buildImagesEditsRequestBody(prompt, image, mask);
		final HttpResponse response = sendFormData(IMAGES_EDITS, paramMap);
		return response.body();
	}

	@Override
	public String imagesVariations(final File image) {
		final Map<String, Object> paramMap = buildImagesVariationsRequestBody(image);
		final HttpResponse response = sendFormData(IMAGES_VARIATIONS, paramMap);
		return response.body();
	}

	@Override
	public InputStream textToSpeech(String input, final OpenaiCommon.OpenaiSpeech voice) {
		String paramJson = buildTTSRequestBody(input, voice.getVoice());
		final HttpResponse response = sendPost(TTS, paramJson);
		return response.bodyStream();
	}

	@Override
	public String speechToText(final File file) {
		final Map<String, Object> paramMap = buildSTTRequestBody(file);
		final HttpResponse response = sendFormData(STT, paramMap);
		return response.body();
	}

	@Override
	public String embeddingText(String input) {
		String paramJson = buildEmbeddingTextRequestBody(input);
		final HttpResponse response = sendPost(EMBEDDINGS, paramJson);
		return response.body();
	}

	@Override
	public String moderations(String text, String imgUrl) {
		String paramJson = buileModerationsRequestBody(text, imgUrl);
		final HttpResponse response = sendPost(MODERATIONS, paramJson);
		return response.body();
	}

	@Override
	public String chatReasoning(final List<Message> messages, String reasoningEffort) {
		String paramJson = buildChatReasoningRequestBody(messages, reasoningEffort);
		final HttpResponse response = sendPost(CHAT_ENDPOINT, paramJson);
		return response.body();
	}

	@Override
	public void chatReasoning(List<Message> messages, String reasoningEffort, Consumer<String> callback) {
		Map<String, Object> paramMap = buildChatReasoningStreamRequestBody(messages, reasoningEffort);
		ThreadUtil.newThread(() -> sendPostStream(CHAT_ENDPOINT, paramMap, callback::accept), "openai-chatReasoning-sse").start();
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
	private String buildChatVisionRequestBody(String prompt, final List<String> images, String detail) {
		// 定义消息结构
		final List<Message> messages = new ArrayList<>();
		final List<Object> content = new ArrayList<>();

		final Map<String, String> contentMap = new HashMap<>();
		contentMap.put("type", "text");
		contentMap.put("text", prompt);
		content.add(contentMap);
		for (String img : images) {
			final Map<String, Object> imgUrlMap = new HashMap<>();
			imgUrlMap.put("type", "image_url");
			final Map<String, String> urlMap = new HashMap<>();
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

	private Map<String, Object> buildChatVisionStreamRequestBody(String prompt, final List<String> images, String detail) {
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
		paramMap.put("stream", true);
		paramMap.put("model", config.getModel());
		paramMap.put("messages", messages);
		//合并其他参数
		paramMap.putAll(config.getAdditionalConfigMap());
		return paramMap;
	}

	//构建文生图请求体
	private String buildImagesGenerationsRequestBody(String prompt) {
		final Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("model", config.getModel());
		paramMap.put("prompt", prompt);
		//合并其他参数
		paramMap.putAll(config.getAdditionalConfigMap());

		return JSONUtil.toJsonStr(paramMap);
	}

	//构建图片编辑请求体
	private Map<String, Object> buildImagesEditsRequestBody(String prompt, final File image, final File mask) {
		final Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("model", config.getModel());
		paramMap.put("prompt", prompt);
		paramMap.put("image", image);
		if (mask != null) {
			paramMap.put("mask", mask);
		}
		//合并其他参数
		paramMap.putAll(config.getAdditionalConfigMap());

		return paramMap;
	}

	//构建图片变形请求体
	private Map<String, Object> buildImagesVariationsRequestBody(final File image) {
		final Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("model", config.getModel());
		paramMap.put("image", image);
		//合并其他参数
		paramMap.putAll(config.getAdditionalConfigMap());

		return paramMap;
	}

	//构建TTS请求体
	private String buildTTSRequestBody(String input, String voice) {
		final Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("model", config.getModel());
		paramMap.put("input", input);
		paramMap.put("voice", voice);
		//合并其他参数
		paramMap.putAll(config.getAdditionalConfigMap());

		return JSONUtil.toJsonStr(paramMap);
	}

	//构建STT请求体
	private Map<String, Object> buildSTTRequestBody(final File file) {
		final Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("model", config.getModel());
		paramMap.put("file", file);
		//合并其他参数
		paramMap.putAll(config.getAdditionalConfigMap());

		return paramMap;
	}

	//构建文本向量化请求体
	private String buildEmbeddingTextRequestBody(String input) {
		//使用JSON工具
		final Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("model", config.getModel());
		paramMap.put("input", input);
		//合并其他参数
		paramMap.putAll(config.getAdditionalConfigMap());
		return JSONUtil.toJsonStr(paramMap);
	}

	//构建检查图片或文字请求体
	private String buileModerationsRequestBody(String text, String imgUrl) {
		//使用JSON工具
		final Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("model", config.getModel());

		final List<Object> input = new ArrayList<>();
		//添加文本参数
		if (!StrUtil.isBlank(text)) {
			final Map<String, String> textMap = new HashMap<>();
			textMap.put("type", "text");
			textMap.put("text", text);
			input.add(textMap);
		}
		//添加图片参数
		if (!StrUtil.isBlank(imgUrl)) {
			final Map<String, Object> imgUrlMap = new HashMap<>();
			imgUrlMap.put("type", "image_url");
			final Map<String, String> urlMap = new HashMap<>();
			urlMap.put("url", imgUrl);
			imgUrlMap.put("image_url", urlMap);
			input.add(imgUrlMap);
		}

		paramMap.put("input", input);
		//合并其他参数
		paramMap.putAll(config.getAdditionalConfigMap());

		return JSONUtil.toJsonStr(paramMap);
	}

	//构建推理请求体
	private String buildChatReasoningRequestBody(final List<Message> messages, String reasoningEffort) {
		final Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("model", config.getModel());
		paramMap.put("messages", messages);
		paramMap.put("reasoning_effort", reasoningEffort);
		//合并其他参数
		paramMap.putAll(config.getAdditionalConfigMap());

		return JSONUtil.toJsonStr(paramMap);
	}

	private Map<String, Object> buildChatReasoningStreamRequestBody(final List<Message> messages, String reasoningEffort) {
		final Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("stream", true);
		paramMap.put("model", config.getModel());
		paramMap.put("messages", messages);
		paramMap.put("reasoning_effort", reasoningEffort);
		//合并其他参数
		paramMap.putAll(config.getAdditionalConfigMap());

		return paramMap;
	}

}
