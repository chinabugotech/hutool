package cn.hutool.ai.model.doubao;

import cn.hutool.ai.core.AIConfig;
import cn.hutool.ai.core.BaseAIService;
import cn.hutool.ai.core.Message;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Doubao服务，AI具体功能的实现
 *
 * @author elichow
 * @since 5.8.37
 */
public class DoubaoServiceImpl extends BaseAIService implements DoubaoService {

	//对话
	private final String CHAT_ENDPOINT = "/chat/completions";
	//文本向量化
	private final String EMBEDDING_TEXT = "/embeddings";
	//图文向量化
	private final String EMBEDDING_VISION = "/embeddings/multimodal";
	//应用bots
	private final String BOTS_CHAT = "/bots/chat/completions";
	//分词
	private final String TOKENIZATION = "/tokenization";
	//批量推理chat
	private final String BATCH_CHAT = "/batch/chat/completions";
	//创建上下文缓存
	private final String CREATE_CONTEXT = "/context/create";
	//上下文缓存对话
	private final String CHAT_CONTEXT = "/context/chat/completions";
	//创建视频生成任务
	private final String CREATE_VIDEO = "/contents/generations/tasks";

	public DoubaoServiceImpl(AIConfig config) {
		//初始化doubao客户端
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
	public String videoTasks(String text, String image, List<DoubaoCommon.DoubaoVideo> videoParams) {
		String paramJson = buildGenerationsTasksRequestBody(text, image, videoParams);
		HttpResponse response = sendPost(CREATE_VIDEO, paramJson);
		return response.body();
	}

	@Override
	public String getVideoTasksInfo(String taskId) {
		HttpResponse response = sendGet(CREATE_VIDEO + "/" + taskId);
		return response.body();
	}


	@Override
	public String embeddingText(String[] input) {
		String paramJson = buildEmbeddingTextRequestBody(input);
		HttpResponse response = sendPost(EMBEDDING_TEXT, paramJson);
		return response.body();
	}

	@Override
	public String embeddingVision(String text, String image) {
		String paramJson = buildEmbeddingVisionRequestBody(text, image);
		HttpResponse response = sendPost(EMBEDDING_VISION, paramJson);
		return response.body();
	}

	@Override
	public String botsChat(List<Message> messages) {
		String paramJson = buildBotsChatRequestBody(messages);
		System.out.println(paramJson);
		HttpResponse response = sendPost(BOTS_CHAT, paramJson);
		return response.body();
	}

	@Override
	public String tokenization(String[] text) {
		String paramJson = buildTokenizationRequestBody(text);
		HttpResponse response = sendPost(TOKENIZATION, paramJson);
		return response.body();
	}

	@Override
	public String batchChat(String prompt) {
		// 定义消息结构
		List<Message> messages = new ArrayList<>();
		messages.add(new Message("system", "You are a helpful assistant"));
		messages.add(new Message("user", prompt));
		return batchChat(messages);
	}

	@Override
	public String batchChat(List<Message> messages) {
		String paramJson = buildBatchChatRequestBody(messages);
		System.out.println(paramJson);
		HttpResponse response = sendPost(BATCH_CHAT, paramJson);
		return response.body();
	}

	@Override
	public String createContext(List<Message> messages, String mode) {
		String paramJson = buildCreateContextRequest(messages, mode);
		System.out.println(paramJson);
		HttpResponse response = sendPost(CREATE_CONTEXT, paramJson);
		return response.body();
	}

	@Override
	public String chatContext(String prompt, String contextId) {
		// 定义消息结构
		List<Message> messages = new ArrayList<>();
		messages.add(new Message("user", prompt));
		return chatContext(messages, contextId);
	}

	@Override
	public String chatContext(List<Message> messages, String contextId) {
		String paramJson = buildChatContentRequestBody(messages, contextId);
		HttpResponse response = sendPost(CHAT_CONTEXT, paramJson);
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

	//构建文本向量化请求体
	private String buildEmbeddingTextRequestBody(String[] input) {
		//使用JSON工具
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("model", config.getModel());
		paramMap.put("input", input);
		//合并其他参数
		paramMap.putAll(config.getAdditionalConfigMap());
		return JSONUtil.toJsonStr(paramMap);
	}

	//构建图文向量化请求体
	private String buildEmbeddingVisionRequestBody(String text, String image) {
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
		if (!StrUtil.isBlank(image)) {
			HashMap<String, Object> imgUrlMap = new HashMap<>();
			imgUrlMap.put("type", "image_url");
			HashMap<String, String> urlMap = new HashMap<>();
			urlMap.put("url", image);
			imgUrlMap.put("image_url", urlMap);
			input.add(imgUrlMap);
		}

		paramMap.put("input", input);
		//合并其他参数
		paramMap.putAll(config.getAdditionalConfigMap());

		return JSONUtil.toJsonStr(paramMap);
	}

	//构建应用chat请求体
	private String buildBotsChatRequestBody(List<Message> messages) {
		return buildChatRequestBody(messages);
	}

	//构建分词请求体
	private String buildTokenizationRequestBody(String[] text) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("model", config.getModel());
		paramMap.put("text", text);
		return JSONUtil.toJsonStr(paramMap);
	}

	//构建批量推理chat请求体
	private String buildBatchChatRequestBody(List<Message> messages) {
		return buildChatRequestBody(messages);
	}

	//构建创建上下文缓存请求体
	private String buildCreateContextRequest(List<Message> messages, String mode) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("messages", messages);
		paramMap.put("model", config.getModel());
		paramMap.put("mode", mode);
		//合并其他参数
		paramMap.putAll(config.getAdditionalConfigMap());

		return JSONUtil.toJsonStr(paramMap);
	}

	//构建上下文缓存对话请求体
	private String buildChatContentRequestBody(List<Message> messages, String contextId) {
		//使用JSON工具
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("model", config.getModel());
		paramMap.put("messages", messages);
		paramMap.put("context_id", contextId);
		//合并其他参数
		paramMap.putAll(config.getAdditionalConfigMap());

		return JSONUtil.toJsonStr(paramMap);
	}

	//构建创建视频任务请求体
	private String buildGenerationsTasksRequestBody(String text, String image, List<DoubaoCommon.DoubaoVideo> videoParams) {
		//使用JSON工具
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("model", config.getModel());

		List<Object> content = new ArrayList<>();
		//添加文本参数
		Map<String, String> textMap = new HashMap<>();
		if (!StrUtil.isBlank(text)) {
			textMap.put("type", "text");
			textMap.put("text", text);
			content.add(textMap);
		}
		//添加图片参数
		if (!StrUtil.isNotBlank(image)) {
			Map<String, Object> imgUrlMap = new HashMap<>();
			imgUrlMap.put("type", "image_url");
			HashMap<String, String> urlMap = new HashMap<>();
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
				for (DoubaoCommon.DoubaoVideo videoParam : videoParams) {
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
				for (DoubaoCommon.DoubaoVideo videoParam : videoParams) {
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

		return JSONUtil.toJsonStr(paramMap);
	}

}
