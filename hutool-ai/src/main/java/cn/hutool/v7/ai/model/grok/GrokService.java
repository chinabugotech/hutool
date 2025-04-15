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

package cn.hutool.v7.ai.model.grok;

import cn.hutool.v7.ai.core.AIService;

import java.util.List;

/**
 * grok支持的扩展接口
 *
 * @author elichow
 * @since 6.0.0
 */
public interface GrokService extends AIService {

	/**
	 * 创建消息回复
	 *
	 * @param prompt   题词
	 * @param maxToken 最大token
	 * @return AI回答
	 * @since 6.0.0
	 */
	String message(String prompt, int maxToken);

	/**
	 * 图像理解：模型会依据传入的图片信息以及问题，给出回复。
	 *
	 * @param prompt 题词
	 * @param images 图片列表/或者图片Base64编码图片列表(URI形式)
	 * @param detail 手动设置图片的质量，取值范围high、low、auto,默认为auto
	 * @return AI回答
	 * @since 6.0.0
	 */
	String chatVision(String prompt, final List<String> images, String detail);

	/**
	 * 图像理解：模型会依据传入的图片信息以及问题，给出回复。
	 *
	 * @param prompt 题词
	 * @param images 传入的图片列表地址/或者图片Base64编码图片列表(URI形式)
	 * @return AI回答
	 * @since 6.0.0
	 */
	default String chatVision(String prompt, final List<String> images) {
		return chatVision(prompt, images, GrokCommon.GrokVision.AUTO.getDetail());
	}

	/**
	 * 列出所有model列表
	 *
	 * @return model列表
	 * @since 6.0.0
	 */
	String models();

	/**
	 * 获取模型信息
	 *
	 * @param modelId model ID
	 * @return model信息
	 * @since 6.0.0
	 */
	String getModel(String modelId);

	/**
	 * 列出所有语言model
	 *
	 * @return languageModel列表
	 * @since 6.0.0
	 */
	String languageModels();

	/**
	 * 获取语言模型信息
	 *
	 * @param modelId model ID
	 * @return model信息
	 * @since 6.0.0
	 */
	String getLanguageModel(String modelId);

	/**
	 * 分词：可以将文本转换为模型可理解的 token 信息
	 *
	 * @param text 需要分词的内容
	 * @return 分词结果
	 * @since 6.0.0
	 */
	String tokenizeText(String text);

	/**
	 * 从延迟对话中获取结果
	 *
	 * @param requestId 延迟对话中的延迟请求ID
	 * @return AI回答
	 * @since 6.0.0
	 */
	String deferredCompletion(String requestId);
}
