package cn.hutool.ai;

import cn.hutool.ai.core.AIConfig;
import cn.hutool.ai.core.AIService;
import cn.hutool.ai.core.Message;
import cn.hutool.ai.model.deepseek.DeepSeekService;
import cn.hutool.ai.model.doubao.DoubaoService;
import cn.hutool.ai.model.grok.GrokService;
import cn.hutool.ai.model.openai.OpenaiService;

import java.util.List;

/**
 * AI工具类
 *
 * @author elichow
 * @since 5.8.37
 */
public class AIUtil {

	/**
	 * 获取AI模型服务，每个大模型提供的功能会不一样，可以调用此方法指定不同AI服务类，调用不同的功能
	 *
	 * @param config 创建的AI服务模型的配置
	 * @param clazz  AI模型服务类
	 * @return AIModelService的实现类实例
	 * @since 5.8.37
	 */
	public static <T extends AIService> T getAIService(AIConfig config, Class<T> clazz) {
		return AIServiceFactory.getAIService(config, clazz);
	}

	/**
	 * 获取AI模型服务
	 *
	 * @param config 创建的AI服务模型的配置
	 * @return AIModelService 其中只有公共方法
	 * @since 5.8.37
	 */
	public static AIService getAIService(AIConfig config) {
		return getAIService(config, AIService.class);
	}

	/**
	 * 获取DeepSeek模型服务
	 *
	 * @param config 创建的AI服务模型的配置
	 * @return DeepSeekService
	 * @since 5.8.37
	 */
	public static DeepSeekService getDeepSeekService(AIConfig config) {
		return getAIService(config, DeepSeekService.class);
	}

	/**
	 * 获取Doubao模型服务
	 *
	 * @param config 创建的AI服务模型的配置
	 * @return DoubaoService
	 * @since 5.8.37
	 */
	public static DoubaoService getDoubaoService(AIConfig config) {
		return getAIService(config, DoubaoService.class);
	}

	/**
	 * 获取Grok模型服务
	 *
	 * @param config 创建的AI服务模型的配置
	 * @return GrokService
	 * @since 5.8.37
	 */
	public static GrokService getGrokService(AIConfig config) {
		return getAIService(config, GrokService.class);
	}

	/**
	 * 获取Openai模型服务
	 *
	 * @param config 创建的AI服务模型的配置
	 * @return OpenAIService
	 * @since 5.8.37
	 */
	public static OpenaiService getOpenAIService(AIConfig config) {
		return getAIService(config, OpenaiService.class);
	}

	/**
	 * AI大模型对话功能
	 *
	 * @param config 创建的AI服务模型的配置
	 * @param prompt 需要对话的内容
	 * @return AI模型返回的Response响应字符串
	 * @since 5.8.37
	 */
	public static String chat(AIConfig config, String prompt) {
		return getAIService(config).chat(prompt);
	}

	/**
	 * AI大模型对话功能
	 *
	 * @param config   创建的AI服务模型的配置
	 * @param messages 由目前为止的对话组成的消息列表，可以设置role，content。详细参考官方文档
	 * @return AI模型返回的Response响应字符串
	 * @since 5.8.37
	 */
	public static String chat(AIConfig config, List<Message> messages) {
		return getAIService(config).chat(messages);
	}

}
