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

package cn.hutool.ai.model.deepseek;

import cn.hutool.ai.core.AIService;
import java.util.function.Consumer;

/**
 * deepSeek支持的扩展接口
 *
 * @author elichow
 * @since 5.8.38
 */
public interface DeepSeekService extends AIService {

	/**
	 * 模型beta功能
	 *
	 * @param prompt 题词
	 * @return AI的回答
	 * @since 5.8.38
	 */
	String beta(String prompt);

	/**
	 * 模型beta功能-SSE流式输出
	 * @param prompt 题词
	 * @param callback 流式数据回调函数
	 * @since 5.8.39
	 */
	 void beta(String prompt, final Consumer<String> callback);

	/**
	 * 列出所有模型列表
	 *
	 * @return model列表
	 * @since 5.8.38
	 */
	String models();

	/**
	 * 查询余额
	 *
	 * @return 余额
	 * @since 5.8.38
	 */
	String balance();
}
