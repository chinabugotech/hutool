package cn.hutool.ai.model.openai;

import cn.hutool.ai.core.AIConfig;
import cn.hutool.ai.core.BaseAIService;
import cn.hutool.ai.core.Message;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * openai服务，AI具体功能的实现
 *
 * @author elichow
 * @since 5.8.37
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

	public OpenaiServiceImpl(AIConfig config) {
		//初始化Openai客户端
		super(config);
	}

	@Override
	public String chat(String prompt) {
		// 定义消息结构
		List<Message> messages = new ArrayList<>();
		messages.add(new Message("system", "You are a helpful assistant"));
		messages.add(new Message("user", prompt));
		return chat(messages);
	}

	@Override
	public String chat(List<Message> messages) {
		String paramJson = buildChatRequestBody(messages);
		HttpResponse response = sendPost(CHAT_ENDPOINT, paramJson);
		return response.body();
	}

	@Override
	public String chatVision(String prompt, List<String> images, String detail) {
		String paramJson = buildChatVisionRequestBody(prompt, images, detail);
		HttpResponse response = sendPost(CHAT_ENDPOINT, paramJson);
		return response.body();
	}

	@Override
	public String imagesGenerations(String prompt) {
		String paramJson = buildImagesGenerationsRequestBody(prompt);
		HttpResponse response = sendPost(IMAGES_GENERATIONS, paramJson);
		return response.body();
	}

	@Override
	public String imagesEdits(String prompt, File image, File mask) {
		Map<String, Object> paramMap = buildImagesEditsRequestBody(prompt, image, mask);
		HttpResponse response = sendFormData(IMAGES_EDITS, paramMap);
		return response.body();
	}

	@Override
	public String imagesVariations(File image) {
		Map<String, Object> paramMap = buildImagesVariationsRequestBody(image);
		HttpResponse response = sendFormData(IMAGES_VARIATIONS, paramMap);
		return response.body();
	}

	@Override
	public InputStream textToSpeech(String input, OpenaiCommon.OpenaiSpeech voice) {
		String paramJson = buildTTSRequestBody(input, voice.getVoice());
		HttpResponse response = sendPost(TTS, paramJson);
		return response.bodyStream();
	}

	@Override
	public String speechToText(File file) {
		Map<String, Object> paramMap = buildSTTRequestBody(file);
		HttpResponse response = sendFormData(STT, paramMap);
		return response.body();
	}

	@Override
	public String embeddingText(String input) {
		String paramJson = buildEmbeddingTextRequestBody(input);
		HttpResponse response = sendPost(EMBEDDINGS, paramJson);
		return response.body();
	}

	@Override
	public String moderations(String text, String imgUrl) {
		String paramJson = buileModerationsRequestBody(text, imgUrl);
		HttpResponse response = sendPost(MODERATIONS, paramJson);
		return response.body();
	}

	@Override
	public String chatReasoning(String prompt, String reasoningEffort) {
		// 定义消息结构
		List<Message> messages = new ArrayList<>();
		messages.add(new Message("system", "You are a helpful assistant"));
		messages.add(new Message("user", prompt));
		return chat(messages);
	}

	@Override
	public String chatReasoning(List<Message> messages, String reasoningEffort) {
		String paramJson = buildChatReasoningRequestBody(messages, reasoningEffort);
		HttpResponse response = sendPost(CHAT_ENDPOINT, paramJson);
		return response.body();
	}

	// 构建chat请求体
	private String buildChatRequestBody(List<Message> messages) {
		//使用JSON工具
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("model", config.getModel());
		paramMap.put("messages", messages);
		//合并其他参数
		paramMap.putAll(config.getAdditionalConfigMap());

		return JSONUtil.toJsonStr(paramMap);
	}

	//构建chatVision请求体
	private String buildChatVisionRequestBody(String prompt, List<String> images, String detail) {
		// 定义消息结构
		List<Message> messages = new ArrayList<>();
		List<Object> content = new ArrayList<>();

		Map<String, String> contentMap = new HashMap<>();
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
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("model", config.getModel());
		paramMap.put("messages", messages);
		//合并其他参数
		paramMap.putAll(config.getAdditionalConfigMap());
		return JSONUtil.toJsonStr(paramMap);
	}

	//构建文生图请求体
	private String buildImagesGenerationsRequestBody(String prompt) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("model", config.getModel());
		paramMap.put("prompt", prompt);
		//合并其他参数
		paramMap.putAll(config.getAdditionalConfigMap());

		return JSONUtil.toJsonStr(paramMap);
	}

	//构建图片编辑请求体
	private Map<String, Object> buildImagesEditsRequestBody(String prompt, File image, File mask) {
		Map<String, Object> paramMap = new HashMap<>();
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
	private Map<String, Object> buildImagesVariationsRequestBody(File image) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("model", config.getModel());
		paramMap.put("image", image);
		//合并其他参数
		paramMap.putAll(config.getAdditionalConfigMap());

		return paramMap;
	}

	//构建TTS请求体
	private String buildTTSRequestBody(String input, String voice) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("model", config.getModel());
		paramMap.put("input", input);
		paramMap.put("voice", voice);
		//合并其他参数
		paramMap.putAll(config.getAdditionalConfigMap());

		return JSONUtil.toJsonStr(paramMap);
	}

	//构建STT请求体
	private Map<String, Object> buildSTTRequestBody(File file) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("model", config.getModel());
		paramMap.put("file", file);
		//合并其他参数
		paramMap.putAll(config.getAdditionalConfigMap());

		return paramMap;
	}

	//构建文本向量化请求体
	private String buildEmbeddingTextRequestBody(String input) {
		//使用JSON工具
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("model", config.getModel());
		paramMap.put("input", input);
		//合并其他参数
		paramMap.putAll(config.getAdditionalConfigMap());
		return JSONUtil.toJsonStr(paramMap);
	}

	//构建检查图片或文字请求体
	private String buileModerationsRequestBody(String text, String imgUrl) {
		//使用JSON工具
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("model", config.getModel());

		List<Object> input = new ArrayList<>();
		//添加文本参数
		if (!StrUtil.isBlank(text)) {
			Map<String, String> textMap = new HashMap<>();
			textMap.put("type", "text");
			textMap.put("text", text);
			input.add(textMap);
		}
		//添加图片参数
		if (!StrUtil.isBlank(imgUrl)) {
			HashMap<String, Object> imgUrlMap = new HashMap<>();
			imgUrlMap.put("type", "image_url");
			HashMap<String, String> urlMap = new HashMap<>();
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
	private String buildChatReasoningRequestBody(List<Message> messages, String reasoningEffort) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("model", config.getModel());
		paramMap.put("messages", messages);
		paramMap.put("reasoning_effort", reasoningEffort);
		//合并其他参数
		paramMap.putAll(config.getAdditionalConfigMap());

		return JSONUtil.toJsonStr(paramMap);
	}

}
