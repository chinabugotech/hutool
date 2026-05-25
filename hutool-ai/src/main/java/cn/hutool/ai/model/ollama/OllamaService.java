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

import cn.hutool.ai.core.AIService;
import cn.hutool.ai.core.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Ollama特有的功能
 *
 * @author yangruoyu-yumeisoft
 * @since 5.8.40
 */
public interface OllamaService extends AIService {

	/**
	 * 生成文本补全
	 *
	 * @param prompt 输入提示
	 * @return AI回答
	 * @since 5.8.40
	 */
	String generate(String prompt);

	/**
	 * 生成文本补全-SSE流式输出
	 *
	 * @param prompt 输入提示
	 * @param callback 流式数据回调函数
	 * @since 5.8.40
	 */
	void generate(String prompt, Consumer<String> callback);

	/**
	 * 生成文本补全（带选项）
	 *
	 * @param prompt 输入提示
	 * @param format 响应格式
	 * @return AI回答
	 * @since 5.8.40
	 */
	String generate(String prompt, String format);

	/**
	 * 生成文本补全（带选项）-SSE流式输出
	 *
	 * @param prompt 输入提示
	 * @param format 响应格式
	 * @param callback 流式数据回调函数
	 * @since 5.8.40
	 */
	void generate(String prompt, String format, Consumer<String> callback);

	/**
	 * 生成文本嵌入向量
	 *
	 * @param prompt 输入文本
	 * @return 嵌入向量结果
	 * @since 5.8.40
	 */
	String embeddings(String prompt);

	/**
	 * 列出本地可用的模型
	 *
	 * @return 模型列表
	 * @since 5.8.40
	 */
	String listModels();

	/**
	 * 显示模型信息
	 *
	 * @param modelName 模型名称
	 * @return 模型信息
	 * @since 5.8.40
	 */
	String showModel(String modelName);

	/**
	 * 拉取模型
	 *
	 * @param modelName 模型名称
	 * @return 拉取结果
	 * @since 5.8.40
	 */
	String pullModel(String modelName);

	/**
	 * 删除模型
	 *
	 * @param modelName 模型名称
	 * @return 删除结果
	 * @since 5.8.40
	 */
	String deleteModel(String modelName);

	/**
	 * 复制模型
	 *
	 * @param source 源模型名称
	 * @param destination 目标模型名称
	 * @return 复制结果
	 * @since 5.8.40
	 */
	String copyModel(String source, String destination);

	/**
	 * 简化的对话方法
	 *
	 * @param prompt 对话题词
	 * @return AI回答
	 * @since 5.8.40
	 */
	default String chat(String prompt) {
		final List<Message> messages = new ArrayList<>();
		messages.add(new Message("user", prompt));
		return chat(messages);
	}

	/**
	 * 简化的对话方法-SSE流式输出
	 *
	 * @param prompt 对话题词
	 * @param callback 流式数据回调函数
	 * @since 5.8.40
	 */
	default void chat(String prompt, Consumer<String> callback) {
		final List<Message> messages = new ArrayList<>();
		messages.add(new Message("user", prompt));
		chat(messages, callback);
	}
}
