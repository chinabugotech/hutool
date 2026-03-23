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

package cn.hutool.ai.model.grok;

import cn.hutool.ai.core.AIService;
import cn.hutool.ai.core.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * grok支持的扩展接口
 *
 * @author elichow
 * @since 5.8.38
 */
public interface GrokService extends AIService {

	/**
	 * 创建消息回复
	 *
	 * @param prompt   题词
	 * @param maxToken 最大token
	 * @return AI回答
	 * @since 5.8.38
	 */
	default String message(String prompt, int maxToken){
		// 定义消息结构
		final List<Message> messages = new ArrayList<>();
		messages.add(new Message("system", "You are a helpful assistant"));
		messages.add(new Message("user", prompt));
		return message(messages, maxToken);
	}

	/**
	 * 创建消息回复-SSE流式输出
	 *
	 * @param prompt   题词
	 * @param maxToken 最大token
	 * @param callback 流式数据回调函数
	 * @since 5.8.39
	 */
	default void message(String prompt, int maxToken, final Consumer<String> callback){
		final List<Message> messages = new ArrayList<>();
		messages.add(new Message("system", "You are a helpful assistant"));
		messages.add(new Message("user", prompt));
		message(messages, maxToken, callback);
	}

	/**
	 * 创建消息回复
	 *
	 * @param messages messages 由对话组成的消息列表。如系统人设，背景信息等，用户自定义的信息
	 * @param maxToken 最大token
	 * @return AI回答
	 * @since 5.8.39
	 */
	String message(List<Message> messages, int maxToken);

	/**
	 * 创建消息回复-SSE流式输出
	 *
	 * @param messages messages 由对话组成的消息列表。如系统人设，背景信息等，用户自定义的信息
	 * @param maxToken 最大token
	 * @param callback 流式数据回调函数
	 * @since 5.8.39
	 */
	void message(List<Message> messages, int maxToken, final Consumer<String> callback);

	/**
	 * 图像理解：模型会依据传入的图片信息以及问题，给出回复。
	 *
	 * @param prompt 题词
	 * @param images 图片列表/或者图片Base64编码图片列表(URI形式)
	 * @param detail 手动设置图片的质量，取值范围high、low、auto,默认为auto
	 * @return AI回答
	 * @since 5.8.38
	 */
	String chatVision(String prompt, final List<String> images, String detail);

	/**
	 * 图像理解-SSE流式输出
	 *
	 * @param prompt 题词
	 * @param images 图片列表/或者图片Base64编码图片列表(URI形式)
	 * @param detail 手动设置图片的质量，取值范围high、low、auto,默认为auto
	 * @param callback 流式数据回调函数
	 * @since 5.8.39
	 */
	void chatVision(String prompt, final List<String> images, String detail,final Consumer<String> callback);

	/**
	 * 图像理解：模型会依据传入的图片信息以及问题，给出回复。
	 *
	 * @param prompt 题词
	 * @param images 传入的图片列表地址/或者图片Base64编码图片列表(URI形式)
	 * @return AI回答
	 * @since 5.8.38
	 */
	default String chatVision(String prompt, final List<String> images) {
		return chatVision(prompt, images, GrokCommon.GrokVision.AUTO.getDetail());
	}

	/**
	 * 图像理解：模型会依据传入的图片信息以及问题，给出回复。
	 *
	 * @param prompt 题词
	 * @param images 传入｜的图片列表地址/或者图片Base64编码图片列表(URI形式)
	 * @param callback 流式数据回调函数
	 * @since 5.8.39
	 */
	default void chatVision(String prompt, final List<String> images, final Consumer<String> callback){
		chatVision(prompt, images, GrokCommon.GrokVision.AUTO.getDetail(), callback);
	}

	/**
	 * 列出所有model列表
	 *
	 * @return model列表
	 * @since 5.8.38
	 */
	String models();

	/**
	 * 获取模型信息
	 *
	 * @param modelId model ID
	 * @return model信息
	 * @since 5.8.38
	 */
	String getModel(String modelId);

	/**
	 * 列出所有语言model
	 *
	 * @return languageModel列表
	 * @since 5.8.38
	 */
	String languageModels();

	/**
	 * 获取语言模型信息
	 *
	 * @param modelId model ID
	 * @return model信息
	 * @since 5.8.38
	 */
	String getLanguageModel(String modelId);

	/**
	 * 分词：可以将文本转换为模型可理解的 token 信息
	 *
	 * @param text 需要分词的内容
	 * @return 分词结果
	 * @since 5.8.38
	 */
	String tokenizeText(String text);

	/**
	 * 从延迟对话中获取结果
	 *
	 * @param requestId 延迟对话中的延迟请求ID
	 * @return AI回答
	 * @since 5.8.38
	 */
	String deferredCompletion(String requestId);

	/**
	 * 文生图
	 * 请设置config中model为支持图片功能的模型，目前支持GROK_2_IMAGE
	 *
	 * @param prompt 题词
	 * @return 包含生成图片的url
	 * @since 5.8.39
	 */
	String imagesGenerations(String prompt);
}
