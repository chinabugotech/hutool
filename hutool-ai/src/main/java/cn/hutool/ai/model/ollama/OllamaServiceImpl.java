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

package cn.hutool.ai.model.ollama;

import cn.hutool.ai.AIException;
import cn.hutool.ai.core.AIConfig;
import cn.hutool.ai.core.BaseAIService;
import cn.hutool.ai.core.Message;
import cn.hutool.core.bean.BeanPath;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Ollama服务，AI具体功能的实现
 *
 * @author yangruoyu-yumeisoft
 * @since 5.8.40
 */
public class OllamaServiceImpl extends BaseAIService implements OllamaService {

	// 对话补全
	private static final String CHAT_ENDPOINT = "/api/chat";
	// 文本生成
	private static final String GENERATE_ENDPOINT = "/api/generate";
	// 文本嵌入
	private static final String EMBEDDINGS_ENDPOINT = "/api/embeddings";
	// 列出模型
	private static final String LIST_MODELS_ENDPOINT = "/api/tags";
	// 显示模型信息
	private static final String SHOW_MODEL_ENDPOINT = "/api/show";
	// 拉取模型
	private static final String PULL_MODEL_ENDPOINT = "/api/pull";
	// 删除模型
	private static final String DELETE_MODEL_ENDPOINT = "/api/delete";
	// 复制模型
	private static final String COPY_MODEL_ENDPOINT = "/api/copy";

	/**
	 * 构造函数
	 *
	 * @param config AI配置
	 */
	public OllamaServiceImpl(final AIConfig config) {
		super(config);
	}

	@Override
	public String chat(final List<Message> messages) {
		final String paramJson = buildChatRequestBody(messages);
		final HttpResponse response = sendPost(CHAT_ENDPOINT, paramJson);
		JSONObject responseJson = JSONUtil.parseObj(response.body());
		Object errorMessage = BeanPath.create("error").get(responseJson);
		if(errorMessage!=null){
			throw new RuntimeException(errorMessage.toString());
		}
		return BeanPath.create("message.content").get(responseJson).toString();
	}

	@Override
	public void chat(final List<Message> messages, final Consumer<String> callback) {
		Map<String, Object> paramMap = buildChatStreamRequestBody(messages);
		ThreadUtil.newThread(() -> sendPostStream(CHAT_ENDPOINT, paramMap, callback::accept), "ollama-chat-sse").start();
	}

	@Override
	public String generate(String prompt) {
		final String paramJson = buildGenerateRequestBody(prompt, null);
		final HttpResponse response = sendPost(GENERATE_ENDPOINT, paramJson);
		return response.body();
	}

	@Override
	public void generate(String prompt, Consumer<String> callback) {
		Map<String, Object> paramMap = buildGenerateStreamRequestBody(prompt, null);
		ThreadUtil.newThread(() -> sendPostStream(GENERATE_ENDPOINT, paramMap, callback::accept), "ollama-generate-sse").start();
	}

	@Override
	public String generate(String prompt, String format) {
		final String paramJson = buildGenerateRequestBody(prompt, format);
		final HttpResponse response = sendPost(GENERATE_ENDPOINT, paramJson);
		return response.body();
	}

	@Override
	public void generate(String prompt, String format, Consumer<String> callback) {
		Map<String, Object> paramMap = buildGenerateStreamRequestBody(prompt, format);
		ThreadUtil.newThread(() -> sendPostStream(GENERATE_ENDPOINT, paramMap, callback::accept), "ollama-generate-sse").start();
	}

	@Override
	public String embeddings(String prompt) {
		final String paramJson = buildEmbeddingsRequestBody(prompt);
		final HttpResponse response = sendPost(EMBEDDINGS_ENDPOINT, paramJson);
		return response.body();
	}

	@Override
	public String listModels() {
		final HttpResponse response = sendGet(LIST_MODELS_ENDPOINT);
		return response.body();
	}

	@Override
	public String showModel(String modelName) {
		final String paramJson = buildShowModelRequestBody(modelName);
		final HttpResponse response = sendPost(SHOW_MODEL_ENDPOINT, paramJson);
		return response.body();
	}

	@Override
	public String pullModel(String modelName) {
		final String paramJson = buildPullModelRequestBody(modelName);
		final HttpResponse response = sendPost(PULL_MODEL_ENDPOINT, paramJson);
		return response.body();
	}

	@Override
	public String deleteModel(String modelName) {
		final String paramJson = buildDeleteModelRequestBody(modelName);
		final HttpResponse response = sendDeleteRequest(DELETE_MODEL_ENDPOINT, paramJson);
		return response.body();
	}

	@Override
	public String copyModel(String source, String destination) {
		final String paramJson = buildCopyModelRequestBody(source, destination);
		final HttpResponse response = sendPost(COPY_MODEL_ENDPOINT, paramJson);
		return response.body();
	}

	// 构建chat请求体
	private String buildChatRequestBody(final List<Message> messages) {
		final Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("stream",false);
		paramMap.put("model", config.getModel());
		paramMap.put("messages", messages);
		// 合并其他参数
		paramMap.putAll(config.getAdditionalConfigMap());

		return JSONUtil.toJsonStr(paramMap);
	}

	// 构建chatStream请求体
	private Map<String, Object> buildChatStreamRequestBody(final List<Message> messages) {
		final Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("stream", true);
		paramMap.put("model", config.getModel());
		paramMap.put("messages", messages);
		// 合并其他参数
		paramMap.putAll(config.getAdditionalConfigMap());

		return paramMap;
	}

	// 构建generate请求体
	private String buildGenerateRequestBody(final String prompt, final String format) {
		final Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("model", config.getModel());
		paramMap.put("prompt", prompt);
		if (StrUtil.isNotBlank(format)) {
			paramMap.put("format", format);
		}
		// 合并其他参数
		paramMap.putAll(config.getAdditionalConfigMap());

		return JSONUtil.toJsonStr(paramMap);
	}

	// 构建generateStream请求体
	private Map<String, Object> buildGenerateStreamRequestBody(final String prompt, final String format) {
		final Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("stream", true);
		paramMap.put("model", config.getModel());
		paramMap.put("prompt", prompt);
		if (StrUtil.isNotBlank(format)) {
			paramMap.put("format", format);
		}
		// 合并其他参数
		paramMap.putAll(config.getAdditionalConfigMap());

		return paramMap;
	}

	// 构建embeddings请求体
	private String buildEmbeddingsRequestBody(final String prompt) {
		final Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("model", config.getModel());
		paramMap.put("prompt", prompt);
		// 合并其他参数
		paramMap.putAll(config.getAdditionalConfigMap());

		return JSONUtil.toJsonStr(paramMap);
	}

	// 构建showModel请求体
	private String buildShowModelRequestBody(final String modelName) {
		final Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("name", modelName);

		return JSONUtil.toJsonStr(paramMap);
	}

	// 构建pullModel请求体
	private String buildPullModelRequestBody(final String modelName) {
		final Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("name", modelName);

		return JSONUtil.toJsonStr(paramMap);
	}

	// 构建deleteModel请求体
	private String buildDeleteModelRequestBody(final String modelName) {
		final Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("name", modelName);

		return JSONUtil.toJsonStr(paramMap);
	}

	/**
	 * 发送DELETE请求
	 *
	 * @param endpoint 请求端点
	 * @param paramJson 请求参数JSON
	 * @return 响应结果
	 */
	private HttpResponse sendDeleteRequest(String endpoint, String paramJson) {
		try {
			return HttpRequest.delete(config.getApiUrl() + endpoint)
				.header(Header.CONTENT_TYPE, "application/json")
				.header(Header.ACCEPT, "application/json")
				.body(paramJson)
				.timeout(config.getTimeout())
				.execute();
		} catch (Exception e) {
			throw new AIException("Failed to send DELETE request: " + e.getMessage(), e);
		}
	}

	// 构建copyModel请求体
	private String buildCopyModelRequestBody(final String source, final String destination) {
		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("source", source);
		requestBody.put("destination", destination);
		return JSONUtil.toJsonStr(requestBody);
	}

}
