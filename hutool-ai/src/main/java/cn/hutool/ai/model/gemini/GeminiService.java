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

package cn.hutool.ai.model.gemini;

import cn.hutool.ai.core.AIService;
import cn.hutool.ai.core.Message;

import java.io.File;
import java.util.List;
import java.util.function.Consumer;

/**
 * Gemini服务支持的扩展接口
 *
 * @author elichow
 * @since 5.8.43
 */
public interface GeminiService extends AIService {

	/**
	 * 全模态理解（图像/视频/音频/PDF）：模型会依据传入的媒体资源给出回复。
	 *
	 * @param prompt    指令
	 * @param mediaList 媒体资源列表 (支持 Base64, URL, 或 File API 的 URI)
	 * @return AI回答
	 */
	String chatMultimodal(String prompt, final List<String> mediaList);

	/**
	 * 全模态理解-SSE流式输出
	 *
	 * @param prompt    指令
	 * @param mediaList 媒体资源列表
	 * @param callback  流式数据回调函数
	 */
	void chatMultimodal(String prompt, final List<String> mediaList, final Consumer<String> callback);

	/**
	 * 结构化输出：强制要求模型返回 JSON 格式
	 *
	 * @param messages 消息列表
	 * @return AI回答
	 */
	String chatJson(final List<Message> messages);

	/**
	 * 生成图像 (Imagen 模型集成)
	 *
	 * @param prompt 图像描述词
	 * @return 包含图片数据的响应 (通常为 Base64)
	 */
	String predictImage(String prompt);

	/**
	 * 生成视频:根据文本提示语生成视频
	 *
	 * @param prompt 视频描述词
	 * @return 包含 operationName 的 JSON 字符串
	 */
	String predictVideo(String prompt);

	/**
	 * 获取视频生成状态：用于轮询视频生成进度
	 *
	 * @param operationName 生成视频接口返回的任务名称
	 * @return 包含视频状态（done）及结果的 JSON 字符串
	 */
	String getVideoOperation(String operationName);


	/**
	 * 下载生成的视频文件
	 *
	 * @param videoUri 视频文件的 URI
	 * @param filePath 保存视频的文件路径
	 */
	void downLoadVideo(String videoUri, String filePath);

	/**
	 * 文本转语音 (TTS)
	 *
	 * @param prompt 文本或带有导演备注的内容
	 * @return 语音文件的 Base64 编码字符串
	 */
	String textToSpeech(String prompt);

	/**
	 * 文本转语音 (TTS) - 指定音色
	 *
	 * @param prompt 文本或带有导演备注的内容
	 * @param voice 预定义的音色常量
	 * @return 语音文件的 Base64 编码字符串
	 */
	String textToSpeech(String prompt, String voice);

	/**
	 * 上传大文件到Gemini File API
	 *
	 * @param file 本地文件
	 * @return 上传后的文件对象信息
	 */
	String uploadFile(final File file);

	/**
	 * 为原始 PCM 音频数据添加 WAV 头
	 *
	 * @param rawPcm 原始 PCM 音频字节数组
	 * @return 带有 WAV 头的音频字节数组
	 */
	byte[] addWavHeader(final byte[] rawPcm);
}
