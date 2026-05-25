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

package cn.hutool.ai.core;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * 模型公共的API功能，特有的功能在model.xx.XXService下定义
 *
 * @author elichow
 * @since 5.8.38
 */
public interface AIService {

	/**
	 * 对话
	 *
	 * @param prompt user题词
	 * @return AI回答
	 * @since 5.8.38
	 */
	default String chat(String prompt){
		final List<Message> messages = new ArrayList<>();
		messages.add(new Message("system", "You are a helpful assistant"));
		messages.add(new Message("user", prompt));
		return chat(messages);
	}

	/**
	 * 对话-SSE流式输出
	 * @param prompt user题词
	 * @param callback 流式数据回调函数
	 * @since 5.8.39
	 */
	default void chat(String prompt, final Consumer<String> callback){
		final List<Message> messages = new ArrayList<>();
		messages.add(new Message("system", "You are a helpful assistant"));
		messages.add(new Message("user", prompt));
		chat(messages, callback);
	}

	/**
	 * 对话
	 *
	 * @param messages 由目前为止的对话组成的消息列表，可以设置role，content。详细参考官方文档
	 * @return AI回答
	 * @since 5.8.38
	 */
	String chat(final List<Message> messages);


	/**
	 * 对话-SSE流式输出
	 * @param messages 由目前为止的对话组成的消息列表，可以设置role，content。详细参考官方文档
	 * @param callback 流式数据回调函数
	 * @since 5.8.39
	 */
	void chat(final List<Message> messages, final Consumer<String> callback);
}
