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

import cn.hutool.ai.AIServiceFactory;
import cn.hutool.ai.ModelName;
import cn.hutool.ai.Models;
import cn.hutool.ai.core.AIConfigBuilder;
import cn.hutool.ai.core.Message;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.thread.ThreadUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class DoubaoServiceTest {

	String key = "your key";
	DoubaoService doubaoService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.DOUBAO.getValue()).setModel(Models.Doubao.DOUBAO_1_5_LITE_32K.getModel()).setApiKey(key).build(), DoubaoService.class);

	@Test
	@Disabled
	void chat(){
		final String chat = doubaoService.chat("写一个疯狂星期四广告词");
		assertNotNull(chat);
	}

	@Test
	@Disabled
	void chatStream() {
		String prompt = "写一个疯狂星期四广告词";
		// 使用AtomicBoolean作为结束标志
		AtomicBoolean isDone = new AtomicBoolean(false);

		doubaoService.chat(prompt, data -> {
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
		final String chat = doubaoService.chat(messages);
		assertNotNull(chat);
	}

	@Test
	@Disabled
	void chatVision() {
		final DoubaoService doubaoService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.DOUBAO.getValue())
			.setApiKey(key).setModel(Models.Doubao.DOUBAO_1_5_VISION_PRO_32K.getModel()).build(), DoubaoService.class);
		final String base64 = ImgUtil.toBase64DataUri(Toolkit.getDefaultToolkit().createImage("your imageUrl"), "png");
		final String chatVision = doubaoService.chatVision("图片上有些什么？", Arrays.asList(base64));
		assertNotNull(chatVision);
	}

	@Test
	@Disabled
	void testChatVision() {
		final DoubaoService doubaoService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.DOUBAO.getValue())
			.setApiKey(key).setModel(Models.Doubao.DOUBAO_1_5_VISION_PRO_32K.getModel()).build(), DoubaoService.class);
		final String chatVision = doubaoService.chatVision("图片上有些什么？", Arrays.asList("https://img2.baidu.com/it/u=862000265,4064861820&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=1544"),DoubaoCommon.DoubaoVision.HIGH.getDetail());
		assertNotNull(chatVision);
	}

	@Test
	@Disabled
	void testChatVisionStream() {
		final DoubaoService doubaoService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.DOUBAO.getValue())
			.setApiKey(key).setModel(Models.Doubao.DOUBAO_1_5_VISION_PRO_32K.getModel()).build(), DoubaoService.class);

		String prompt = "图片上有些什么？";
		List<String> images = Arrays.asList("https://img2.baidu.com/it/u=862000265,4064861820&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=1544");

		// 使用AtomicBoolean作为结束标志
		AtomicBoolean isDone = new AtomicBoolean(false);
		doubaoService.chatVision(prompt,images, data -> {
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
	void videoTasks() {
		final DoubaoService doubaoService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.DOUBAO.getValue())
			.setApiKey(key).setModel(Models.Doubao.Doubao_Seedance_1_0_lite_i2v.getModel()).build(), DoubaoService.class);
		final String videoTasks = doubaoService.videoTasks("生成一段动画视频，主角是大耳朵图图，一个活泼可爱的小男孩。视频中图图在公园里玩耍，" +
			"画面采用明亮温暖的卡通风格，色彩鲜艳，动作流畅。背景音乐轻快活泼，带有冒险感，音效包括鸟叫声、欢笑声和山洞回声。", "https://img2.baidu.com/it/u=862000265,4064861820&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=1544");
		assertNotNull(videoTasks);
	}

	@Test
	@Disabled
	void getVideoTasksInfo() {
		//cgt-20250306170051-6r9gk
		final DoubaoService doubaoService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.DOUBAO.getValue())
			.setApiKey(key).build(), DoubaoService.class);
		final String videoTasksInfo = doubaoService.getVideoTasksInfo("cgt-20250306170051-6r9gk");
		assertNotNull(videoTasksInfo);
	}

	@Test
	@Disabled
	void embeddingText() {
		final DoubaoService doubaoService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.DOUBAO.getValue())
			.setApiKey(key).setModel(Models.Doubao.DOUBAO_EMBEDDING_TEXT_240715.getModel()).build(), DoubaoService.class);
		final String embeddingText = doubaoService.embeddingText(new String[]{"阿斯顿", "马丁"});
		assertNotNull(embeddingText);
	}

	@Test
	@Disabled
	void embeddingVision() {
		final DoubaoService doubaoService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.DOUBAO.getValue())
			.setApiKey(key).setModel(Models.Doubao.DOUBAO_EMBEDDING_VISION.getModel()).build(), DoubaoService.class);
		final String embeddingVision = doubaoService.embeddingVision("天空好难", "https://img2.baidu.com/it/u=862000265,4064861820&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=1544");
		assertNotNull(embeddingVision);
	}

	@Test
	@Disabled
	void botsChat() {
		final DoubaoService doubaoService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.DOUBAO.getValue())
			.setApiKey(key).setModel("your bots id").build(), DoubaoService.class);
		final ArrayList<Message> messages = new ArrayList<>();
		messages.add(new Message("system","你是什么都可以"));
		messages.add(new Message("user","你想做些什么"));
		final String botsChat = doubaoService.botsChat(messages);
		assertNotNull(botsChat);
	}

	@Test
	@Disabled
	void botsChatStream() {
		final DoubaoService doubaoService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.DOUBAO.getValue())
			.setApiKey(key).setModel("your bots id").build(), DoubaoService.class);
		final ArrayList<Message> messages = new ArrayList<>();
		messages.add(new Message("system","你是什么都可以"));
		messages.add(new Message("user","你想做些什么"));

		// 使用AtomicBoolean作为结束标志
		AtomicBoolean isDone = new AtomicBoolean(false);
		doubaoService.botsChat(messages, data -> {
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
	void tokenization() {
		final String tokenization = doubaoService.tokenization(new String[]{"阿斯顿", "马丁"});
		assertNotNull(tokenization);
	}

	@Test
	@Disabled
	void batchChat() {
		final DoubaoService doubaoService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.DOUBAO.getValue())
			.setApiKey(key).setModel("your Endpoint ID").build(), DoubaoService.class);
		final String batchChat = doubaoService.batchChat("写首歌词");
		assertNotNull(batchChat);
	}

	@Test
	@Disabled
	void testBatchChat() {
		final DoubaoService doubaoService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.DOUBAO.getValue())
			.setApiKey(key).setModel("your Endpoint ID").build(), DoubaoService.class);
		final List<Message> messages = new ArrayList<>();
		messages.add(new Message("system","你是个抽象大师"));
		messages.add(new Message("user","写一个KFC的抽象广告"));
		final String batchChat = doubaoService.batchChat(messages);
		assertNotNull(batchChat);
	}

	@Test
	@Disabled
	void createContext() {
		final DoubaoService doubaoService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.DOUBAO.getValue())
			.setApiKey(key).setModel("your Endpoint ID").build(), DoubaoService.class);
		final List<Message> messages = new ArrayList<>();
		messages.add(new Message("system","你是个抽象大师,你真的很抽象"));
		final String context = doubaoService.createContext(messages);//ctx-20250307092153-cvslm
		assertNotNull(context);
	}

	@Test
	@Disabled
	void testCreateContext() {
		final DoubaoService doubaoService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.DOUBAO.getValue())
			.setApiKey(key).setModel("ep-20250305100610-bvbpc").build(), DoubaoService.class);
		final List<Message> messages = new ArrayList<>();
		messages.add(new Message("system","你是个抽象大师,你真的很抽象"));
		final String context = doubaoService.createContext(messages,DoubaoCommon.DoubaoContext.COMMON_PREFIX.getMode());
		assertNotNull(context);//ctx-20250307092153-cvslm
	}

	@Test
	@Disabled
	void chatContext() {
		//ctx-20250307092153-cvslm
		final DoubaoService doubaoService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.DOUBAO.getValue())
			.setApiKey(key).setModel("your Endpoint ID").build(), DoubaoService.class);
		final String chatContext = doubaoService.chatContext("你是谁？", "your contextId");
		assertNotNull(chatContext);
	}

	@Test
	@Disabled
	void testChatContext() {
		final DoubaoService doubaoService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.DOUBAO.getValue())
			.setApiKey(key).setModel("your Endpoint ID").build(), DoubaoService.class);
		final List<Message> messages = new ArrayList<>();
		messages.add(new Message("user","你怎么看待意大利面拌水泥？"));
		final String chatContext = doubaoService.chatContext(messages, "your contextId");
		assertNotNull(chatContext);
	}

	@Test
	@Disabled
	void testChatContextStream() {
		final DoubaoService doubaoService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.DOUBAO.getValue())
			.setApiKey(key).setModel("your Endpoint ID").build(), DoubaoService.class);
		final List<Message> messages = new ArrayList<>();
		messages.add(new Message("user","你怎么看待意大利面拌水泥？"));
		String contextId  = "your contextId";

		// 使用AtomicBoolean作为结束标志
		AtomicBoolean isDone = new AtomicBoolean(false);
		doubaoService.chatContext(messages,contextId, data -> {
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
	void imagesGenerations() {
		final DoubaoService doubaoService  = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.DOUBAO.getValue())
			.setApiKey(key).setModel(Models.Doubao.DOUBAO_SEEDREAM_3_0_T2I.getModel()).build(), DoubaoService.class);
		final String imagesGenerations = doubaoService.imagesGenerations("一位年轻的宇航员站在未来感十足的太空站内，透过巨大的弧形落地窗凝望浩瀚宇宙。窗外，璀璨的星河与五彩斑斓的星云交织，远处隐约可见未知星球的轮廓，仿佛在召唤着探索的脚步。宇航服上的呼吸灯与透明显示屏上的星图交相辉映，象征着人类科技与宇宙奥秘的碰撞。画面深邃而神秘，充满对未知的渴望与无限可能的想象。");
		assertNotNull(imagesGenerations);
	}
}
