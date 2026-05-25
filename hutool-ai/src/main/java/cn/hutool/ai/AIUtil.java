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

package cn.hutool.ai;

import cn.hutool.ai.core.AIConfig;
import cn.hutool.ai.core.AIService;
import cn.hutool.ai.core.Message;
import cn.hutool.ai.model.deepseek.DeepSeekService;
import cn.hutool.ai.model.doubao.DoubaoService;
import cn.hutool.ai.model.grok.GrokService;
import cn.hutool.ai.model.hutool.HutoolService;
import cn.hutool.ai.model.openai.OpenaiService;

import java.util.List;

/**
 * AI工具类
 *
 * @author elichow
 * @since 5.8.38
 */
public class AIUtil {

	/**
	 * 获取AI模型服务，每个大模型提供的功能会不一样，可以调用此方法指定不同AI服务类，调用不同的功能
	 *
	 * @param config 创建的AI服务模型的配置
	 * @param clazz  AI模型服务类
	 * @return AIModelService的实现类实例
	 * @since 5.8.38
	 * @param <T> AIService实现类
	 */
	public static <T extends AIService> T getAIService(final AIConfig config, final Class<T> clazz) {
		return AIServiceFactory.getAIService(config, clazz);
	}

	/**
	 * 获取AI模型服务
	 *
	 * @param config 创建的AI服务模型的配置
	 * @return AIModelService 其中只有公共方法
	 * @since 5.8.38
	 */
	public static AIService getAIService(final AIConfig config) {
		return getAIService(config, AIService.class);
	}

	/**
	 * 获取Hutool-AI服务
	 *
	 * @param config 创建的AI服务模型的配置
	 * @return HutoolService
	 * @since 5.8.39
	 */
	public static HutoolService getHutoolService(final AIConfig config) {
		return getAIService(config, HutoolService.class);
	}

	/**
	 * 获取DeepSeek模型服务
	 *
	 * @param config 创建的AI服务模型的配置
	 * @return DeepSeekService
	 * @since 5.8.38
	 */
	public static DeepSeekService getDeepSeekService(final AIConfig config) {
		return getAIService(config, DeepSeekService.class);
	}

	/**
	 * 获取Doubao模型服务
	 *
	 * @param config 创建的AI服务模型的配置
	 * @return DoubaoService
	 * @since 5.8.38
	 */
	public static DoubaoService getDoubaoService(final AIConfig config) {
		return getAIService(config, DoubaoService.class);
	}

	/**
	 * 获取Grok模型服务
	 *
	 * @param config 创建的AI服务模型的配置
	 * @return GrokService
	 * @since 5.8.38
	 */
	public static GrokService getGrokService(final AIConfig config) {
		return getAIService(config, GrokService.class);
	}

	/**
	 * 获取Openai模型服务
	 *
	 * @param config 创建的AI服务模型的配置
	 * @return OpenAIService
	 * @since 5.8.38
	 */
	public static OpenaiService getOpenAIService(final AIConfig config) {
		return getAIService(config, OpenaiService.class);
	}

	/**
	 * AI大模型对话功能
	 *
	 * @param config 创建的AI服务模型的配置
	 * @param prompt 需要对话的内容
	 * @return AI模型返回的Response响应字符串
	 * @since 5.8.38
	 */
	public static String chat(final AIConfig config, final String prompt) {
		return getAIService(config).chat(prompt);
	}

	/**
	 * AI大模型对话功能
	 *
	 * @param config   创建的AI服务模型的配置
	 * @param messages 由目前为止的对话组成的消息列表，可以设置role，content。详细参考官方文档
	 * @return AI模型返回的Response响应字符串
	 * @since 5.8.38
	 */
	public static String chat(final AIConfig config, final List<Message> messages) {
		return getAIService(config).chat(messages);
	}

}
