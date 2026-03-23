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

package cn.hutool.ai.model.hutool;

import cn.hutool.ai.core.AIService;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.function.Consumer;

/**
 * hutool支持的扩展接口
 *
 * @author elichow
 * @since 5.8.39
 */
public interface HutoolService extends AIService {

	/**
	 * 图像理解：模型会依据传入的图片信息以及问题，给出回复。
	 *
	 * @param prompt 题词
	 * @param images 图片列表/或者图片Base64编码图片列表(URI形式)
	 * @param detail 手动设置图片的质量，取值范围high、low、auto,默认为auto
	 * @return AI回答
	 * @since 5.8.39
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
	 * @since 5.8.39
	 */
	default String chatVision(String prompt, final List<String> images) {
		return chatVision(prompt, images, HutoolCommon.HutoolVision.AUTO.getDetail());
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
		chatVision(prompt, images, HutoolCommon.HutoolVision.AUTO.getDetail(), callback);
	}

	/**
	 * 分词：可以将文本转换为模型可理解的 token 信息
	 *
	 * @param text 需要分词的内容
	 * @return 分词结果
	 * @since 5.8.39
	 */
	String tokenizeText(String text);

	/**
	 * 文生图
	 *
	 * @param prompt 题词
	 * @return 包含生成图片的url
	 * @since 5.8.39
	 */
	String imagesGenerations(String prompt);

	/**
	 * 图文向量化：仅支持单一文本、单张图片或文本与图片的组合输入（即一段文本 + 一张图片），暂不支持批量文本 / 图片的同时处理
	 *
	 * @param text  需要向量化的内容
	 * @param image 需要向量化的图片地址/或者图片Base64编码图片(URI形式)
	 * @return 处理后的向量信息
	 * @since 5.8.39
	 */
	String embeddingVision(String text, String image);

	/**
	 * TTS文本转语音
	 *
	 * @param input 需要转成语音的文本
	 * @param voice AI的音色
	 * @return 返回的音频mp3文件流
	 * @since 5.8.39
	 */
	InputStream tts(String input, final HutoolCommon.HutoolSpeech voice);

	/**
	 * TTS文本转语音
	 *
	 * @param input 需要转成语音的文本
	 * @return 返回的音频mp3文件流
	 * @since 5.8.39
	 */
	default InputStream tts(String input) {
		return tts(input, HutoolCommon.HutoolSpeech.ALLOY);
	}

	/**
	 * STT音频转文本
	 *
	 * @param file 需要转成文本的音频文件
	 * @return 返回的文本内容
	 * @since 5.8.39
	 */
	String stt(final File file);

	/**
	 * 创建视频生成任务
	 *
	 * @param text        文本提示词
	 * @param image       图片/或者图片Base64编码图片(URI形式)
	 * @param videoParams 视频参数列表
	 * @return 生成任务id
	 * @since 5.8.39
	 */
	String videoTasks(String text, String image, final List<HutoolCommon.HutoolVideo> videoParams);

	/**
	 * 创建视频生成任务
	 *
	 * @param text  文本提示词
	 * @param image 图片/或者图片Base64编码图片(URI形式)
	 * @return 生成任务id
	 * @since 5.8.39
	 */
	default String videoTasks(String text, String image) {
		return videoTasks(text, image, null);
	}

	/**
	 * 查询视频生成任务信息
	 *
	 * @param taskId 通过创建生成视频任务返回的生成任务id
	 * @return 生成任务信息
	 * @since 5.8.39
	 */
	String getVideoTasksInfo(String taskId);

}
