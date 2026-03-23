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

package cn.hutool.ai.model.doubao;

import cn.hutool.ai.core.AIService;
import cn.hutool.ai.core.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * doubao支持的扩展接口
 *
 * @author elichow
 * @since 5.8.38
 */
public interface DoubaoService extends AIService {

	/**
	 * 图像理解：模型会依据传入的图片信息以及问题，给出回复。
	 *
	 * @param prompt 提问
	 * @param images 传入的图片列表地址/或者图片Base64编码图片列表(URI形式)
	 * @return AI回答
	 * @since 5.8.38
	 */
	default String chatVision(String prompt, final List<String> images) {
		return chatVision(prompt, images, DoubaoCommon.DoubaoVision.AUTO.getDetail());
	}

	/**
	 * 图像理解-SSE流式输出
	 *
	 * @param prompt 提问
	 * @param images 图片列表/或者图片Base64编码图片列表(URI形式)
	 * @param callback 流式数据回调函数
	 * @since 5.8.39
	 */
	default void chatVision(String prompt, final List<String> images, final Consumer<String> callback) {
		chatVision(prompt, images, DoubaoCommon.DoubaoVision.AUTO.getDetail(), callback);
	}

	/**
	 * 图像理解：模型会依据传入的图片信息以及问题，给出回复。
	 *
	 * @param prompt 提问
	 * @param images 图片列表/或者图片Base64编码图片列表(URI形式)
	 * @param detail 手动设置图片的质量，取值范围high、low、auto,默认为auto
	 * @return AI回答
	 * @since 5.8.38
	 */
	String chatVision(String prompt, final List<String> images, String detail);

	/**
	 * 图像理解-SSE流式输出
	 *
	 * @param prompt 提问
	 * @param images 传入的图片列表地址/或者图片Base64编码图片列表(URI形式)
	 * @param detail 手动设置图片的质量，取值范围high、low、auto,默认为auto
	 * @param callback 流式数据回调函数
	 * @since 5.8.39
	 */
	void chatVision(String prompt, final List<String> images, String detail, final Consumer<String> callback);

	/**
	 * 创建视频生成任务
	 * 注意：调用该方法时，配置config中的model为您创建的推理接入点（Endpoint）ID。详细参考官方文档
	 *
	 * @param text        文本提示词
	 * @param image       图片/或者图片Base64编码图片(URI形式)
	 * @param videoParams 视频参数列表
	 * @return 生成任务id
	 * @since 5.8.38
	 */
	String videoTasks(String text, String image, final List<DoubaoCommon.DoubaoVideo> videoParams);

	/**
	 * 创建视频生成任务
	 * 注意：调用该方法时，配置config中的model为生成视频的模型或者您创建的推理接入点（Endpoint）ID。详细参考官方文档
	 *
	 * @param text  文本提示词
	 * @param image 图片/或者图片Base64编码图片(URI形式)
	 * @return 生成任务id
	 * @since 5.8.38
	 */
	default String videoTasks(String text, String image) {
		return videoTasks(text, image, null);
	}

	/**
	 * 查询视频生成任务信息
	 *
	 * @param taskId 通过创建生成视频任务返回的生成任务id
	 * @return 生成任务信息
	 * @since 5.8.38
	 */
	String getVideoTasksInfo(String taskId);

	/**
	 * 文本向量化
	 *
	 * @param input 需要向量化的内容列表，支持中文、英文
	 * @return 处理后的向量信息
	 * @since 5.8.38
	 */
	String embeddingText(String[] input);

	/**
	 * 图文向量化：仅支持单一文本、单张图片或文本与图片的组合输入（即一段文本 + 一张图片），暂不支持批量文本 / 图片的同时处理
	 *
	 * @param text  需要向量化的内容
	 * @param image 需要向量化的图片地址/或者图片Base64编码图片(URI形式)
	 * @return 处理后的向量信息
	 * @since 5.8.38
	 */
	String embeddingVision(String text, String image);

	/**
	 * 应用(Bot) config中model设置为您创建的应用ID
	 *
	 * @param messages 由对话组成的消息列表。如系统人设，背景信息等，用户自定义的信息
	 * @return AI回答
	 * @since 5.8.38
	 */
	String botsChat(final List<Message> messages);

	/**
	 * 应用(Bot)-SSE流式输出 config中model设置为您创建的应用ID
	 *
	 * @param messages 由对话组成的消息列表。如系统人设，背景信息等，用户自定义的信息
	 * @param callback 流式数据回调函数
	 * @since 5.8.39
	 */
	void botsChat(final List<Message> messages, final Consumer<String> callback);

	/**
	 * 分词：可以将文本转换为模型可理解的 token id，并返回文本的 tokens 数量、token id、 token 在原始文本中的偏移量等信息
	 *
	 * @param text 需要分词的内容列表
	 * @return 分词结果
	 * @since 5.8.38
	 */
	String tokenization(String[] text);

	/**
	 * 批量推理 Chat
	 * 注意：调用该方法时，配置config中的model为您创建的批量推理接入点（Endpoint）ID。详细参考官方文档
	 * 该方法不支持流式
	 *
	 * @param prompt chat内容
	 * @return AI回答
	 * @since 5.8.38
	 */
	default String batchChat(String prompt){
		final List<Message> messages = new ArrayList<>();
		messages.add(new Message("system", "You are a helpful assistant"));
		messages.add(new Message("user", prompt));
		return batchChat(messages);
	}

	/**
	 * 批量推理 Chat
	 * 注意：调用该方法时，配置config中的model为您创建的批量推理接入点（Endpoint）ID。详细参考官方文档
	 * 该方法不支持流式
	 *
	 * @param messages 由对话组成的消息列表。如系统人设，背景信息等，用户自定义的信息
	 * @return AI回答
	 * @since 5.8.38
	 */
	String batchChat(final List<Message> messages);

	/**
	 * 创建上下文缓存： 创建上下文缓存，获得缓存 id字段后，在上下文缓存对话 API中使用。
	 * 注意：调用该方法时，配置config中的model为您创建的推理接入点（Endpoint）ID,
	 * 推理接入点中使用的模型需要在模型管理中开启缓存功能。详细参考官方文档
	 *
	 * @param messages 由对话组成的消息列表。如系统人设，背景信息等，用户自定义的信息
	 * @param mode     上下文缓存的类型,详细参考官方文档 默认为session
	 * @return 返回的缓存id
	 * @since 5.8.38
	 */
	String createContext(final List<Message> messages, String mode);

	/**
	 * 创建上下文缓存： 创建上下文缓存，获得缓存 id字段后，在上下文缓存对话 API中使用。
	 * 注意：调用该方法时，配置config中的model为您创建的推理接入点（Endpoint）ID,
	 * 推理接入点中使用的模型需要在模型管理中开启缓存功能。详细参考官方文档
	 *
	 * @param messages 由对话组成的消息列表。如系统人设，背景信息等，用户自定义的信息
	 * @return 返回的缓存id
	 * @since 5.8.38
	 */
	default String createContext(final List<Message> messages) {
		return createContext(messages, DoubaoCommon.DoubaoContext.SESSION.getMode());
	}

	/**
	 * 上下文缓存对话： 向大模型发起带上下文缓存的请求
	 * 注意：配置config中的model可以为您创建的推理接入点（Endpoint）ID，也可以是支持chat的model
	 *
	 * @param prompt    对话的内容题词
	 * @param contextId 创建上下文缓存后获取的缓存id
	 * @return AI的回答
	 * @since 5.8.38
	 */
	default String chatContext(String prompt, String contextId){
		final List<Message> messages = new ArrayList<>();
		messages.add(new Message("user", prompt));
		return chatContext(messages, contextId);
	}

	/**
	 * 上下文缓存对话-SSE流式输出
	 * 注意：配置config中的model可以为您创建的推理接入点（Endpoint）ID，也可以是支持chat的model
	 *
	 * @param prompt    对话的内容题词
	 * @param contextId 创建上下文缓存后获取的缓存id
	 * @param callback 流式数据回调函数
	 * @since 5.8.39
	 */
	default void chatContext(String prompt, String contextId, final Consumer<String> callback){
		final List<Message> messages = new ArrayList<>();
		messages.add(new Message("user", prompt));
		chatContext(messages, contextId, callback);
	}

	/**
	 * 上下文缓存对话： 向大模型发起带上下文缓存的请求
	 * 注意：配置config中的model可以为您创建的推理接入点（Endpoint）ID，也可以是支持chat的model
	 *
	 * @param messages  对话的信息  不支持最后一个元素的role设置为assistant。如使用session 缓存（mode设置为session）传入最新一轮对话的信息，无需传入历史信息
	 * @param contextId 创建上下文缓存后获取的缓存id
	 * @return AI的回答
	 * @since 5.8.38
	 */
	String chatContext(final List<Message> messages, String contextId);

	/**
	 * 上下文缓存对话-SSE流式输出
	 * 注意：配置config中的model可以为您创建的推理接入点（Endpoint）ID，也可以是支持chat的model
	 *
	 * @param messages  对话的信息  不支持最后一个元素的role设置为assistant。如使用session 缓存（mode设置为session）传入最新一轮对话的信息，无需传入历史信息
	 * @param contextId 创建上下文缓存后获取的缓存id
	 * @param callback 流式数据回调函数
	 * @since 5.8.39
	 */
	void chatContext(final List<Message> messages, String contextId, final Consumer<String> callback);

	/**
	 * 文生图
	 * 请设置config中model为支持图片功能的模型，目前支持Doubao-Seedream-3.0-t2i
	 *
	 * @param prompt 题词
	 * @return 包含生成图片的url
	 * @since 5.8.39
	 */
	String imagesGenerations(String prompt);

}
