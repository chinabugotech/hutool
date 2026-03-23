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

package cn.hutool.ai.model.gemini;

import cn.hutool.ai.AIException;
import cn.hutool.ai.core.AIConfig;
import cn.hutool.ai.core.BaseAIService;
import cn.hutool.ai.core.Message;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.*;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.function.Consumer;

/**
 * Gemini服务，AI具体功能的实现
 *
 * @author elichow
 * @since 5.8.43
 */
public class GeminiServiceImpl extends BaseAIService implements GeminiService {

	private final String GENERATE_CONTENT = ":generateContent";
	private final String STREAM_GENERATE_CONTENT = ":streamGenerateContent";
	private final String PREDICT = ":predict";
	private final String PREDICT_LONG_RUNNING = ":predictLongRunning";
	private final String UPLOAD_BASE_URL = "https://generativelanguage.googleapis.com/upload/v1beta/files";

	public GeminiServiceImpl(final AIConfig config) {
		super(config);
	}

	private String getEndpoint(final boolean stream) {
		String action = stream ? STREAM_GENERATE_CONTENT : GENERATE_CONTENT;
		return "/models/" + config.getModel() + action;
	}

	private String getPredictImageEndpoint() {
		return "/models/" + config.getModel() + PREDICT;
	}

	private String getPredictVideoEndpoint() {
		return "/models/" + config.getModel() + PREDICT_LONG_RUNNING;
	}

	@Override
	public String chat(final List<Message> messages) {
		final Map<String, Object> paramMap = buildChatRequestMap(messages);
		final HttpResponse response = sendPost(getEndpoint(false), JSONUtil.toJsonStr(paramMap));
		return response.body();
	}

	@Override
	public void chat(final List<Message> messages, final Consumer<String> callback) {
		final Map<String, Object> paramMap = buildChatRequestMap(messages);
		final String endpoint = getEndpoint(true) + "?alt=sse";
		ThreadUtil.newThread(() -> sendPostStream(endpoint, paramMap, callback), "gemini-chat-sse").start();
	}

	@Override
	public String chatMultimodal(String prompt, final List<String> mediaList) {
		final Map<String, Object> paramMap = buildMultimodalRequestMap(prompt, mediaList);
		final HttpResponse response = sendPost(getEndpoint(false), JSONUtil.toJsonStr(paramMap));
		return response.body();
	}

	@Override
	public void chatMultimodal(String prompt, final List<String> mediaList, final Consumer<String> callback) {
		final Map<String, Object> paramMap = buildMultimodalRequestMap(prompt, mediaList);
		final String endpoint = getEndpoint(true) + "?alt=sse";
		ThreadUtil.newThread(() -> sendPostStream(endpoint, paramMap, callback), "gemini-m-sse").start();
	}

	@Override
	public String chatJson(final List<Message> messages) {
		final Map<String, Object> paramMap = buildChatRequestMap(messages);
		Map<String, Object> genConfig = MapUtil.get(paramMap, "generationConfig", Map.class);
		if (genConfig == null) {
			genConfig = new HashMap<>();
		}
		//指定响应MIME类型为JSON
		genConfig.put("response_mime_type", "application/json");

		final HttpResponse response = sendPost(getEndpoint(false), JSONUtil.toJsonStr(paramMap));
		return response.body();
	}

	@Override
	public String predictImage(String prompt) {
		final Map<String, Object> paramMap = buildPredictImageRequestMap(prompt);
		final HttpResponse response = sendPost(getPredictImageEndpoint(), JSONUtil.toJsonStr(paramMap));
		return response.body();
	}

	@Override
	public String predictVideo(String prompt) {
		final Map<String, Object> paramMap = buildPredictVideoRequestMap(prompt);
		final HttpResponse response = sendPost(getPredictVideoEndpoint(), JSONUtil.toJsonStr(paramMap));
		return response.body();
	}

	@Override
	public String getVideoOperation(String operationName) {
		String endPoint = "/" + operationName;
		final HttpResponse response = sendGet(endPoint);
		return response.body();
	}

	@Override
	public void downLoadVideo(String videoUri, String filePath) {
		if (StrUtil.isBlank(videoUri)) {
			throw new AIException("Video URI is empty");
		}
		final HttpResponse response = HttpRequest.get(videoUri)
			.header("x-goog-api-key", config.getApiKey())
			.setFollowRedirects(true)
			.executeAsync();
		if (response.isOk()) {
			response.writeBody(FileUtil.file(filePath));
		} else {
			throw new AIException("Download failed with status: " + response.getStatus());
		}
	}

	@Override
	public String textToSpeech(String prompt) {
		final Map<String, Object> paramMap = buildTextToSpeechRequestMap(prompt);
		final HttpResponse response = sendPost(getEndpoint(false), JSONUtil.toJsonStr(paramMap));
		return response.body();
	}

	@Override
	public String textToSpeech(String prompt, String voice) {
		final Map<String, Object> voiceConfig = MapUtil.of("prebuilt_voice_config", MapUtil.of("voice_name", voice));
		config.putAdditionalConfigByKey("speech_config", MapUtil.of("voice_config", voiceConfig));
		return this.textToSpeech(prompt);
	}

	@Override
	public String uploadFile(final File file) {
		if (null == file || !file.exists()) {
			throw new AIException("File not found!");
		}
		try {
			//自动获取MIME
			String mimeType = FileUtil.getMimeType(file.getName());
			if (StrUtil.isBlank(mimeType)) {
				mimeType = "application/octet-stream";
			}

			String uploadUrl = getUploadBaseUrl();

			//获取 Upload URL
			String metadata = JSONUtil.toJsonStr(MapUtil.of("file", MapUtil.of("display_name", file.getName())));
			final HttpResponse res = HttpRequest.post(uploadUrl)
				.header("x-goog-api-key", config.getApiKey())
				.header("X-Goog-Upload-Protocol", "resumable")
				.header("X-Goog-Upload-Command", "start")
				.header("X-Goog-Upload-Header-Content-Length", String.valueOf(file.length()))
				.header("X-Goog-Upload-Header-Content-Type", mimeType)
				.body(metadata).execute();

			String sessionUrl = res.header("X-Goog-Upload-URL");

			//上传二进制流
			final HttpResponse uploadRes = HttpRequest.put(sessionUrl)
				.header("X-Goog-Upload-Command", "upload, finalize")
				.header("X-Goog-Upload-Offset", "0")
				.body(FileUtil.readBytes(file)).execute();

			//返回 JSON，调用者可以从中解析出 file.uri
			return uploadRes.body();
		} catch (Exception e) {
			throw new AIException("Upload failed", e);
		}
	}


	@Override
	public byte[] addWavHeader(final byte[] pcmData) {
		final int totalDataLen = pcmData.length;
		final int totalAudioLen = totalDataLen + 36;
		// Gemini TTS 默认通常是 24k 或 16k
		final int sampleRate = 24000;
		// 单声道
		final int channels = 1;
		// 16bit
		final int byteRate = sampleRate * channels * 2;

		final byte[] header = new byte[44];
		header[0] = 'R'; header[1] = 'I'; header[2] = 'F'; header[3] = 'F';
		header[4] = (byte) (totalAudioLen & 0xff);
		header[5] = (byte) ((totalAudioLen >> 8) & 0xff);
		header[6] = (byte) ((totalAudioLen >> 16) & 0xff);
		header[7] = (byte) ((totalAudioLen >> 24) & 0xff);
		header[8] = 'W'; header[9] = 'A'; header[10] = 'V'; header[11] = 'E';
		header[12] = 'f'; header[13] = 'm'; header[14] = 't'; header[15] = ' ';
		header[16] = 16; header[17] = 0; header[18] = 0; header[19] = 0;
		// PCM 格式
		header[20] = 1; header[21] = 0;
		header[22] = (byte) channels; header[23] = 0;
		header[24] = (byte) (sampleRate & 0xff);
		header[25] = (byte) ((sampleRate >> 8) & 0xff);
		header[26] = (byte) ((sampleRate >> 16) & 0xff);
		header[27] = (byte) ((sampleRate >> 24) & 0xff);
		header[28] = (byte) (byteRate & 0xff);
		header[29] = (byte) ((byteRate >> 8) & 0xff);
		header[30] = (byte) ((byteRate >> 16) & 0xff);
		header[31] = (byte) ((byteRate >> 24) & 0xff);
		header[32] = (byte) (channels * 2); header[33] = 0;
		// 16 bits per sample
		header[34] = 16; header[35] = 0;
		header[36] = 'd'; header[37] = 'a'; header[38] = 't'; header[39] = 'a';
		header[40] = (byte) (totalDataLen & 0xff);
		header[41] = (byte) ((totalDataLen >> 8) & 0xff);
		header[42] = (byte) ((totalDataLen >> 16) & 0xff);
		header[43] = (byte) ((totalDataLen >> 24) & 0xff);

		final byte[] wavData = new byte[header.length + pcmData.length];
		System.arraycopy(header, 0, wavData, 0, header.length);
		System.arraycopy(pcmData, 0, wavData, header.length, pcmData.length);
		return wavData;
	}

	/**
	 * 动态根据 API 配置生成 Upload 地址
	 */
	private String getUploadBaseUrl() {
		String apiUrl = config.getApiUrl();
		//自动提取域名部分
		if (StrUtil.contains(apiUrl, "generativelanguage.googleapis.com")) {
			return "https://generativelanguage.googleapis.com/upload/v1beta/files";
		}
		//如果是反代或自定义节点，动态拼接
		try {
			final URL url = new URL(apiUrl);
			return new URL(url.getProtocol(), url.getHost(), url.getPort(), UPLOAD_BASE_URL).toString();
		} catch (Exception e) {
			return apiUrl.replace("/models/", "/upload/v1beta/files").split("/models")[0];
		}
	}

	private Map<String, Object> buildChatRequestMap(final List<Message> messages) {
		final Map<String, Object> paramMap = new HashMap<>();
		final List<Map<String, Object>> contents = new ArrayList<>();
		Map<String, Object> systemInstruction = null;

		for (Message msg : messages) {
			if ("system".equalsIgnoreCase(msg.getRole())) {
				systemInstruction = MapUtil.ofEntries(MapUtil.entry("parts",
					Collections.singletonList(MapUtil.ofEntries(MapUtil.entry("text", msg.getContent())))));
			} else {
				contents.add(MapUtil.ofEntries(
					MapUtil.entry("role", "user".equalsIgnoreCase(msg.getRole()) ? "user" : "model"),
					MapUtil.entry("parts", Collections.singletonList(MapUtil.ofEntries(MapUtil.entry("text", msg.getContent()))))
				));
			}
		}
		paramMap.put("contents", contents);
		if (ObjectUtil.isNotNull(systemInstruction)) {
			paramMap.put("system_instruction", systemInstruction);
		}
		paramMap.putAll(config.getAdditionalConfigMap());
		return paramMap;
	}

	private Map<String, Object> buildMultimodalRequestMap(String prompt, final List<String> mediaList) {
		final List<Map<String, Object>> parts = new ArrayList<>();
		parts.add(MapUtil.ofEntries(MapUtil.entry("text", prompt)));

		if (ObjectUtil.isNotNull(mediaList)) {
			for (String media : mediaList) {
				if (StrUtil.isBlank(media)) {
					continue;
				}

				//Gemini File资源
				if (media.contains("files/")) {
					String fileUri = media;
					if (!media.startsWith("http")) {
						fileUri = "https://generativelanguage.googleapis.com/v1beta/" + media;
					}
					//直接从服务端获取该文件上传时真实记录的 mimeType
					String realMimeType = getRemoteFileMimeType(fileUri);
					parts.add(MapUtil.ofEntries(
						MapUtil.entry("file_data", MapUtil.ofEntries(
							MapUtil.entry("mime_type", realMimeType),
							MapUtil.entry("file_uri", fileUri)
						))
					));
				} else if (media.startsWith("http")) {
					//普通网络图片 (下载并转 Base64)
					try {
						final byte[] bytes = HttpUtil.downloadBytes(media);
						//尝试识别下载文件的 MIME，无法识别则不强加后缀逻辑，通过流内容自适应
						String mime = FileUtil.getMimeType(media);
						if (StrUtil.isBlank(mime)) {
							// 基础兜底
							mime = "image/jpeg";
						}
						parts.add(MapUtil.ofEntries(
							MapUtil.entry("inline_data", MapUtil.ofEntries(
								MapUtil.entry("mime_type", mime),
								MapUtil.entry("data", Base64.encode(bytes))
							))
						));
					} catch (Exception e) {
						throw new AIException("Failed to download media from URL: " + media, e.getMessage());
					}
				} else {
					//Base64 数据
					parts.add(MapUtil.ofEntries(
						MapUtil.entry("inline_data", MapUtil.ofEntries(
							MapUtil.entry("mime_type", "image/jpeg"),
							MapUtil.entry("data", media)
						))
					));
				}
			}
		}

		final Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("contents", Collections.singletonList(MapUtil.ofEntries(
			MapUtil.entry("role", "user"),
			MapUtil.entry("parts", parts)
		)));

		//合并其他参数
		if (MapUtil.isNotEmpty(config.getAdditionalConfigMap())) {
			paramMap.putAll(config.getAdditionalConfigMap());
		}
		return paramMap;
	}

	private Map<String, Object> buildPredictVideoRequestMap(String prompt) {
		final Map<String, Object> instance = new HashMap<>();
		instance.put("prompt", prompt);

		final Map<String, Object> parameters = new HashMap<>();
		parameters.put("durationSeconds", GeminiCommon.GeminiDurationSeconds.EIGHT.getValue());

		//合并其他参数
		final Map<String, Object> additional = config.getAdditionalConfigMap();
		if (MapUtil.isNotEmpty(additional)) {
			parameters.putAll(additional);
		}

		final Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("instances", Collections.singletonList(instance));
		paramMap.put("parameters", parameters);
		return paramMap;
	}

	private Map<String, Object> buildPredictImageRequestMap(String prompt) {
		final Map<String, Object> instance = new HashMap<>();
		instance.put("prompt", prompt);

		final Map<String, Object> parameters = new HashMap<>();
		// 官方默认4，通常我们会按需设为1
		parameters.put("sampleCount", GeminiCommon.GeminiImageCount.ONE.getCount());
		// 默认 1:1
		parameters.put("aspectRatio", GeminiCommon.GeminiAspectRatio.SQUARE.getRatio());
		// 默认
		parameters.put("personGeneration", GeminiCommon.GeminiPersonGeneration.ALLOW_ADULT.getValue());

		//合并其他参数
		final Map<String, Object> additional = config.getAdditionalConfigMap();
		if (MapUtil.isNotEmpty(additional)) {
			parameters.putAll(additional);
			if (additional.containsKey("numberOfImages")) {
				parameters.put("sampleCount", additional.get("numberOfImages"));
			}
		}

		final Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("instances", Collections.singletonList(instance));
		paramMap.put("parameters", parameters);
		return paramMap;
	}

	private Map<String, Object> buildTextToSpeechRequestMap(String prompt) {
		final Map<String, Object> paramMap = new HashMap<>();
		final Map<String, Object> part = new HashMap<>();
		part.put("text", prompt);

		final Map<String, Object> content = new HashMap<>();
		content.put("role", "user");
		content.put("parts", Collections.singletonList(part));
		paramMap.put("contents", Collections.singletonList(content));

		final Map<String, Object> generationConfig = new HashMap<>();
		//基础固定参数：必须指定返回音频格式
		generationConfig.put("response_modalities", Collections.singletonList("AUDIO"));

		//合并其他参数
		final Map<String, Object> additionalMap = config.getAdditionalConfigMap();
		if (MapUtil.isNotEmpty(additionalMap)) {
			generationConfig.putAll(additionalMap);
		}
		paramMap.put("generation_config", generationConfig);
		return paramMap;
	}

	/**
	 * 获取远程文件的 MIME 类型
	 *
	 * @param fileUri 文件URI
	 * @return MIME类型
	 */
	private String getRemoteFileMimeType(String fileUri) {
		try {
			final HttpRequest httpRequest = HttpRequest.get(fileUri)
				.header(Header.ACCEPT, "application/json")
				.header("x-goog-api-key", config.getApiKey())
				.timeout(config.getTimeout());
			if (config.getHasProxy()) {
				httpRequest.setProxy(config.getProxy());
			}
			String responseBody = httpRequest.execute().body();
			final JSONObject json = JSONUtil.parseObj(responseBody);

			//提取服务端的mimeType
			String mimeType = json.getStr("mimeType");
			if (StrUtil.isNotBlank(mimeType)) {
				return mimeType;
			}
		} catch (Exception e) {
			throw new AIException("Failed to get remote file MIME type", e.getMessage());
		}
		return "application/octet-stream";
	}

	/**
	 * 发送Get请求
	 * @param endpoint 请求节点
	 * @return 请求响应
	 */
	@Override
	protected HttpResponse sendGet(String endpoint) {
		//链式构建请求
		try {
			//设置超时3分钟
			final HttpRequest httpRequest = HttpRequest.get(config.getApiUrl() + endpoint)
				.header(Header.ACCEPT, "application/json")
				.header("x-goog-api-key", config.getApiKey())
				.timeout(config.getTimeout());
			if (config.getHasProxy()) {
				httpRequest.setProxy(config.getProxy());
			}
			return httpRequest.execute();
		} catch (final Exception e) {
			throw new AIException("Failed to send GET request: " + e.getMessage(), e);
		}
	}

	@Override
	protected HttpResponse sendPost(String endpoint, String paramJson) {
		//链式构建请求
		try {
			final HttpRequest httpRequest = HttpRequest.post(config.getApiUrl() + endpoint)
				.header(Header.CONTENT_TYPE, "application/json")
				.header("x-goog-api-key", config.getApiKey())
				.body(paramJson)
				.timeout(config.getTimeout());
			if (config.getHasProxy()) {
				httpRequest.setProxy(config.getProxy());
			}
			return httpRequest.execute();
		} catch (final Exception e) {
			throw new AIException("Failed to send POST request：" + e.getMessage(), e);
		}
	}

	/**
	 * 支持流式返回的 POST 请求
	 *
	 * @param endpoint 请求地址
	 * @param paramMap 请求参数
	 * @param callback 流式数据回调函数
	 */
	@Override
	protected void sendPostStream(String endpoint, final Map<String, Object> paramMap, final Consumer<String> callback) {
		HttpURLConnection connection = null;
		try {
			// 创建连接
			URL apiUrl = new URL(config.getApiUrl() + endpoint);
			connection = (HttpURLConnection) apiUrl.openConnection();
			if (config.getHasProxy()) {
				connection = (HttpURLConnection) apiUrl.openConnection(config.getProxy());
			}
			connection.setRequestMethod(Method.POST.name());
			connection.setRequestProperty(Header.CONTENT_TYPE.getValue(), "application/json");
			connection.setRequestProperty("x-goog-api-key", config.getApiKey());
			connection.setDoOutput(true);
			//5分钟
			connection.setReadTimeout(config.getReadTimeout());
			//3分钟
			connection.setConnectTimeout(config.getTimeout());
			// 发送请求体
			try (OutputStream os = connection.getOutputStream()) {
				String jsonInputString = JSONUtil.toJsonStr(paramMap);
				os.write(jsonInputString.getBytes());
				os.flush();
			}

			// 读取流式响应
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
				String line;
				while ((line = reader.readLine()) != null) {
					// 调用回调函数处理每一行数据
					callback.accept(line);
				}
			}
		} catch (Exception e) {
			callback.accept("{\"error\": \"" + e.getMessage() + "\"}");
		} finally {
			// 关闭连接
			if (connection != null) {
				connection.disconnect();
			}
		}
	}
}
