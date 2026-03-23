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

import cn.hutool.ai.AIException;
import cn.hutool.ai.AIServiceFactory;
import cn.hutool.ai.ModelName;
import cn.hutool.ai.core.AIConfigBuilder;
import cn.hutool.ai.core.Message;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class HutoolServiceTest {

	String key = "请前往Hutool-AI官网：https://ai.hutool.cn 获取";
	HutoolService hutoolService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.HUTOOL.getValue()).setApiKey(key).build(), HutoolService.class);


	@Test
	@Disabled
	void chat(){
		final String chat = hutoolService.chat("写一个疯狂星期四广告词");
		assertNotNull(chat);
	}

	@Test
	@Disabled
	void chatStream() {
		String prompt = "写一个疯狂星期四广告词";
		// 使用AtomicBoolean作为结束标志
		AtomicBoolean isDone = new AtomicBoolean(false);

		hutoolService.chat(prompt, data -> {
			assertNotNull(data);
			if (data.contains("[DONE]")) {
				// 设置结束标志
				isDone.set(true);
			} else if (data.contains("\"error\"")) {
				isDone.set(true);
			}

		});
		// 轮询检查结束标志
		while (!isDone.get()) {
			ThreadUtil.sleep(100);
		}
	}

	@Test
	@Disabled
	void testChat(){
		final List<Message> messages = new ArrayList<>();
		messages.add(new Message("system","你是个抽象大师，会说很抽象的话，最擅长说抽象的笑话"));
		messages.add(new Message("user","给我说一个笑话"));
		final String chat = hutoolService.chat(messages);
		assertNotNull(chat);
	}


	@Test
	@Disabled
	void chatVision() {
		final String base64 = ImgUtil.toBase64DataUri(Toolkit.getDefaultToolkit().createImage("your imageUrl"), "png");
		final String chatVision = hutoolService.chatVision("图片上有些什么？", Arrays.asList(base64));
		assertNotNull(chatVision);
	}

	@Test
	@Disabled
	void testChatVisionStream() {
		String prompt = "图片上有些什么？";
		List<String> images = Arrays.asList("https://img2.baidu.com/it/u=862000265,4064861820&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=1544");

		// 使用AtomicBoolean作为结束标志
		AtomicBoolean isDone = new AtomicBoolean(false);
		hutoolService.chatVision(prompt,images, data -> {
			assertNotNull(data);
			if (data.contains("[DONE]")) {
				// 设置结束标志
				isDone.set(true);
			} else if (data.contains("\"error\"")) {
				isDone.set(true);
			}

		});
		// 轮询检查结束标志
		while (!isDone.get()) {
			ThreadUtil.sleep(100);
		}
	}

	@Test
	@Disabled
	void testChatVision() {
		final String chatVision = hutoolService.chatVision("图片上有些什么？", Arrays.asList("https://img2.baidu.com/it/u=862000265,4064861820&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=1544"));
		assertNotNull(chatVision);
	}

	@Test
	@Disabled
	void tokenizeText() {
		final String tokenizeText = hutoolService.tokenizeText(key);
		assertNotNull(tokenizeText);
	}

	@Test
	@Disabled
	void imagesGenerations() {
		final String imagesGenerations = hutoolService.imagesGenerations("一位年轻的宇航员站在未来感十足的太空站内，透过巨大的弧形落地窗凝望浩瀚宇宙。窗外，璀璨的星河与五彩斑斓的星云交织，远处隐约可见未知星球的轮廓，仿佛在召唤着探索的脚步。宇航服上的呼吸灯与透明显示屏上的星图交相辉映，象征着人类科技与宇宙奥秘的碰撞。画面深邃而神秘，充满对未知的渴望与无限可能的想象。");
		assertNotNull(imagesGenerations);
	}

	@Test
	@Disabled
	void embeddingVision() {
		final String embeddingVision = hutoolService.embeddingVision("天空好难", "https://img2.baidu.com/it/u=862000265,4064861820&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=1544");
		assertNotNull(embeddingVision);
	}

	@Test
	@Disabled
	void textToSpeech() {
		try {
			// 测试正常音频流返回
			final InputStream inputStream = hutoolService.tts("万里山河一夜白，\n" +
				"千峰尽染玉龙哀。\n" +
				"长风卷起琼花碎，\n" +
				"直上九霄揽月来。", HutoolCommon.HutoolSpeech.NOVA);
			assertNotNull(inputStream);

			// 保存音频文件
			final String filePath = "your filePath";
			FileUtil.writeFromStream(inputStream, new File(filePath));

		} catch (Exception e) {
			throw new AIException("TTS测试失败: " + e.getMessage());
		}

	}

	@Test
	@Disabled
	void speechToText() {
		final File file = FileUtil.file("your filePath");
		final String speechToText = hutoolService.stt(file);
		assertNotNull(speechToText);
	}

	@Test
	@Disabled
	void videoTasks() {
		final String videoTasks = hutoolService.videoTasks("生成一段动画视频，主角是大耳朵图图，一个活泼可爱的小男孩。视频中图图在公园里玩耍，" +
			"画面采用明亮温暖的卡通风格，色彩鲜艳，动作流畅。背景音乐轻快活泼，带有冒险感，音效包括鸟叫声、欢笑声和山洞回声。", "https://img2.baidu.com/it/u=862000265,4064861820&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=1544");
		assertNotNull(videoTasks);//cgt-20250529154621-d7dq9
	}

	@Test
	@Disabled
	void getVideoTasksInfo() {
		final String videoTasksInfo = hutoolService.getVideoTasksInfo("cgt-20250529154621-d7dq9");
		assertNotNull(videoTasksInfo);
	}

}
