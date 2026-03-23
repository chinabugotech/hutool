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

package cn.hutool.ai.model.hutool;

import cn.hutool.ai.AIException;
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
 * Hutool服务，AI具体功能的实现
 *
 * @author elichow
 * @since 5.8.39
 */
public class HutoolServiceImpl extends BaseAIService implements HutoolService {

	//对话补全
	private final String CHAT_ENDPOINT = "/chat/completions";
	//分词
	private final String TOKENIZE_TEXT = "/tokenize/text";
	//文生图
	private final String IMAGES_GENERATIONS = "/images/generations";
	//图文向量化
	private final String EMBEDDING_VISION = "/embeddings/multimodal";
	//文本转语音
	private final String TTS = "/audio/tts";
	//语音转文本
	private final String STT = "/audio/stt";
	//创建视频生成任务
	private final String CREATE_VIDEO = "/video/generations";

	public HutoolServiceImpl(final AIConfig config) {
		//初始化hutool客户端
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
		ThreadUtil.newThread(() -> sendPostStream(CHAT_ENDPOINT, paramMap, callback::accept), "hutool-chat-sse").start();
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
		System.out.println(JSONUtil.toJsonStr(paramMap));
		ThreadUtil.newThread(() -> sendPostStream(CHAT_ENDPOINT, paramMap, callback::accept), "hutool-chatVision-sse").start();
	}

	@Override
	public String tokenizeText(String text) {
		String paramJson = buildTokenizeRequestBody(text);
		final HttpResponse response = sendPost(TOKENIZE_TEXT, paramJson);
		return response.body();
	}

	@Override
	public String imagesGenerations(String prompt) {
		String paramJson = buildImagesGenerationsRequestBody(prompt);
		final HttpResponse response = sendPost(IMAGES_GENERATIONS, paramJson);
		return response.body();
	}


	@Override
	public String embeddingVision(String text, String image) {
		String paramJson = buildEmbeddingVisionRequestBody(text, image);
		final HttpResponse response = sendPost(EMBEDDING_VISION, paramJson);
		return response.body();
	}

	@Override
	public InputStream tts(String input, final HutoolCommon.HutoolSpeech voice) {
		try {
			String paramJson = buildTTSRequestBody(input, voice.getVoice());
			final HttpResponse response = sendPost(TTS, paramJson);

			// 检查响应内容类型
			String contentType = response.header("Content-Type");
			if (contentType != null && contentType.startsWith("application/json")) {
				// 如果是JSON响应，说明有错误
				String errorBody = response.body();
				throw new AIException("TTS请求失败: " + errorBody);
			}
			// 默认返回音频流
			return response.bodyStream();
		} catch (Exception e) {
			throw new AIException("TTS处理失败: " + e.getMessage(), e);
		}
	}

	@Override
	public String stt(final File file) {
		final Map<String, Object> paramMap = buildSTTRequestBody(file);
		final HttpResponse response = sendFormData(STT, paramMap);
		return response.body();
	}


	@Override
	public String videoTasks(String text, String image, final List<HutoolCommon.HutoolVideo> videoParams) {
		String paramJson = buildGenerationsTasksRequestBody(text, image, videoParams);
		final HttpResponse response = sendPost(CREATE_VIDEO, paramJson);
		return response.body();
	}

	@Override
	public String getVideoTasksInfo(String taskId) {
		final HttpResponse response = sendGet(CREATE_VIDEO + "/" + taskId);
		return response.body();
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

	//构建文生图请求体
	private String buildImagesGenerationsRequestBody(String prompt) {
		final Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("model", config.getModel());
		paramMap.put("prompt", prompt);
		//合并其他参数
		paramMap.putAll(config.getAdditionalConfigMap());

		return JSONUtil.toJsonStr(paramMap);
	}

	//构建图文向量化请求体
	private String buildEmbeddingVisionRequestBody(String text, String image) {
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
		if (!StrUtil.isBlank(image)) {
			final Map<String, Object> imgUrlMap = new HashMap<>();
			imgUrlMap.put("type", "image_url");
			final Map<String, String> urlMap = new HashMap<>();
			urlMap.put("url", image);
			imgUrlMap.put("image_url", urlMap);
			input.add(imgUrlMap);
		}

		paramMap.put("input", input);
		//合并其他参数
		paramMap.putAll(config.getAdditionalConfigMap());
		System.out.println(JSONUtil.toJsonStr(paramMap));
		return JSONUtil.toJsonStr(paramMap);
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

	//构建创建视频任务请求体
	private String buildGenerationsTasksRequestBody(String text, String image, final List<HutoolCommon.HutoolVideo> videoParams) {
		//使用JSON工具
		final Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("model", config.getModel());

		final List<Object> content = new ArrayList<>();
		//添加文本参数
		final Map<String, String> textMap = new HashMap<>();
		if (!StrUtil.isBlank(text)) {
			textMap.put("type", "text");
			textMap.put("text", text);
			content.add(textMap);
		}
		//添加图片参数
		if (!StrUtil.isBlank(image)) {
			final Map<String, Object> imgUrlMap = new HashMap<>();
			imgUrlMap.put("type", "image_url");
			final Map<String, String> urlMap = new HashMap<>();
			urlMap.put("url", image);
			imgUrlMap.put("image_url", urlMap);
			content.add(imgUrlMap);
		}

		//添加视频参数
		if (videoParams != null && !videoParams.isEmpty()) {
			//如果有文本参数就加在后面
			if (textMap != null && !textMap.isEmpty()) {
				int textIndex = content.indexOf(textMap);
				StringBuilder textBuilder = new StringBuilder(text);
				for (HutoolCommon.HutoolVideo videoParam : videoParams) {
					textBuilder.append(" ").append(videoParam.getType()).append(" ").append(videoParam.getValue());
				}
				textMap.put("type", "text");
				textMap.put("text", textBuilder.toString());

				if (textIndex != -1) {
					content.set(textIndex, textMap);
				} else {
					content.add(textMap);
				}
			} else {
				//如果没有文本参数就重新增加
				StringBuilder textBuilder = new StringBuilder();
				for (HutoolCommon.HutoolVideo videoParam : videoParams) {
					textBuilder.append(videoParam.getType()).append(videoParam.getValue()).append(" ");
				}
				textMap.put("type", "text");
				textMap.put("text", textBuilder.toString());
				content.add(textMap);
			}
		}

		paramMap.put("content", content);
		//合并其他参数
		paramMap.putAll(config.getAdditionalConfigMap());
		System.out.println(JSONUtil.toJsonStr(paramMap));
		return JSONUtil.toJsonStr(paramMap);
	}

}
