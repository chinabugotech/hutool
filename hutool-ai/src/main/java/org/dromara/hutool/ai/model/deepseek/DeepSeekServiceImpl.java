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

	public DeepSeekServiceImpl(AIConfig config) {
		//初始化DeepSeek客户端
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
		Response response = sendPost(CHAT_ENDPOINT, paramJson);
		return response.bodyStr();
	}

	@Override
	public String beta(String prompt) {
		String paramJson = buildBetaRequestBody(prompt);
		Response response = sendPost(BETA_ENDPOINT, paramJson);
		return response.bodyStr();
	}

	@Override
	public String models() {
		Response response = sendGet(MODELS_ENDPOINT);
		return response.bodyStr();
	}

	@Override
	public String balance() {
		Response response = sendGet(BALANCE_ENDPOINT);
		return response.bodyStr();
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

	// 构建beta请求体
	private String buildBetaRequestBody(String prompt) {
		// 定义消息结构
		//使用JSON工具
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("model", config.getModel());
		paramMap.put("prompt", prompt);
//		//合并其他参数
		paramMap.putAll(config.getAdditionalConfigMap());

		return JSONUtil.toJsonStr(paramMap);
	}

}
