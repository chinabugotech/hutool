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

package cn.hutool.v7.ai.model.doubao;

import cn.hutool.v7.ai.AIServiceFactory;
import cn.hutool.v7.ai.ModelName;
import cn.hutool.v7.ai.Models;
import cn.hutool.v7.ai.core.AIConfigBuilder;
import cn.hutool.v7.ai.core.Message;
import cn.hutool.v7.swing.img.ImgUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class DoubaoServiceTest {

	String key = "your key";
	DoubaoService doubaoService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.DOUBAO.getValue()).setModel(Models.Doubao.DOUBAO_1_5_LITE_32K.getModel()).setApiKey(key).build(), DoubaoService.class);

	@Test
	@Disabled
	void chat(){
		final String chat = doubaoService.chat("写一个疯狂星期四广告词");
		System.out.println(chat);
	}

	@Test
	@Disabled
	void testChat(){
		final List<Message> messages = new ArrayList<>();
		messages.add(new Message("system","你是个抽象大师，会说很抽象的话，最擅长说抽象的笑话"));
		messages.add(new Message("user","给我说一个笑话"));
		final String chat = doubaoService.chat(messages);
		System.out.println(chat);
	}

	@Test
	@Disabled
	void chatVision() {
		final DoubaoService doubaoService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.DOUBAO.getValue())
			.setApiKey(key).setModel(Models.Doubao.DOUBAO_1_5_VISION_PRO_32K.getModel()).build(), DoubaoService.class);
		final String base64 = ImgUtil.toBase64DataUri(Toolkit.getDefaultToolkit().createImage("your imageUrl"), "png");
		final String chatVision = doubaoService.chatVision("图片上有些什么？", Arrays.asList(base64));
		System.out.println(chatVision);
	}

	@Test
	@Disabled
	void testChatVision() {
		final DoubaoService doubaoService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.DOUBAO.getValue())
			.setApiKey(key).setModel(Models.Doubao.DOUBAO_1_5_VISION_PRO_32K.getModel()).build(), DoubaoService.class);
		final String chatVision = doubaoService.chatVision("图片上有些什么？", Arrays.asList("https://img2.baidu.com/it/u=862000265,4064861820&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=1544"),DoubaoCommon.DoubaoVision.HIGH.getDetail());
		System.out.println(chatVision);
	}

	@Test
	@Disabled
	void videoTasks() {
		final DoubaoService doubaoService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.DOUBAO.getValue())
			.setApiKey(key).setModel("your Endpoint ID").build(), DoubaoService.class);
		final String videoTasks = doubaoService.videoTasks("生成一段动画视频，主角是大耳朵图图，一个活泼可爱的小男孩。视频中图图在公园里玩耍，" +
			"画面采用明亮温暖的卡通风格，色彩鲜艳，动作流畅。背景音乐轻快活泼，带有冒险感，音效包括鸟叫声、欢笑声和山洞回声。", "https://img2.baidu.com/it/u=862000265,4064861820&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=1544");
		System.out.println(videoTasks);//cgt-20250306170051-6r9gk
	}

	@Test
	@Disabled
	void getVideoTasksInfo() {
		//cgt-20250306170051-6r9gk
		final DoubaoService doubaoService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.DOUBAO.getValue())
			.setApiKey(key).build(), DoubaoService.class);
		final String videoTasksInfo = doubaoService.getVideoTasksInfo("cgt-20250306170051-6r9gk");
		System.out.println(videoTasksInfo);
	}

	@Test
	@Disabled
	void embeddingText() {
		final DoubaoService doubaoService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.DOUBAO.getValue())
			.setApiKey(key).setModel(Models.Doubao.DOUBAO_EMBEDDING_TEXT_240715.getModel()).build(), DoubaoService.class);
		final String embeddingText = doubaoService.embeddingText(new String[]{"阿斯顿", "马丁"});
		System.out.println(embeddingText);
	}

	@Test
	@Disabled
	void embeddingVision() {
		final DoubaoService doubaoService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.DOUBAO.getValue())
			.setApiKey(key).setModel(Models.Doubao.DOUBAO_EMBEDDING_VISION.getModel()).build(), DoubaoService.class);
		final String embeddingVision = doubaoService.embeddingVision("天空好难", "https://img2.baidu.com/it/u=862000265,4064861820&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=1544");
		System.out.println(embeddingVision);
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
		System.out.println(botsChat);
	}

	@Test
	@Disabled
	void tokenization() {
		final String tokenization = doubaoService.tokenization(new String[]{"阿斯顿", "马丁"});
		System.out.println(tokenization);
	}

	@Test
	@Disabled
	void batchChat() {
		final DoubaoService doubaoService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.DOUBAO.getValue())
			.setApiKey(key).setModel("your Endpoint ID").build(), DoubaoService.class);
		final String batchChat = doubaoService.batchChat("写首歌词");
		System.out.println(batchChat);
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
		System.out.println(batchChat);
	}

	@Test
	@Disabled
	void createContext() {
		final DoubaoService doubaoService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.DOUBAO.getValue())
			.setApiKey(key).setModel("your Endpoint ID").build(), DoubaoService.class);
		final List<Message> messages = new ArrayList<>();
		messages.add(new Message("system","你是个抽象大师,你真的很抽象"));
		final String context = doubaoService.createContext(messages);//ctx-20250307092153-cvslm
		System.out.println(context);
	}

	@Test
	@Disabled
	void testCreateContext() {
		final DoubaoService doubaoService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.DOUBAO.getValue())
			.setApiKey(key).setModel("your Endpoint ID").build(), DoubaoService.class);
		final List<Message> messages = new ArrayList<>();
		messages.add(new Message("system","你是个抽象大师,你真的很抽象"));
		final String context = doubaoService.createContext(messages,DoubaoCommon.DoubaoContext.COMMON_PREFIX.getMode());
		System.out.println(context);//ctx-20250307092153-cvslm
	}

	@Test
	@Disabled
	void chatContext() {
		//ctx-20250307092153-cvslm
		final DoubaoService doubaoService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.DOUBAO.getValue())
			.setApiKey(key).setModel("eyour Endpoint ID").build(), DoubaoService.class);
		final String chatContext = doubaoService.chatContext("你是谁？", "ctx-20250307092153-cvslm");
		System.out.println(chatContext);
	}

	@Test
	@Disabled
	void testChatContext() {
		final DoubaoService doubaoService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.DOUBAO.getValue())
			.setApiKey(key).setModel("your Endpoint ID").build(), DoubaoService.class);
		final List<Message> messages = new ArrayList<>();
		messages.add(new Message("user","你怎么看待意大利面拌水泥？"));
		final String chatContext = doubaoService.chatContext(messages, "ctx-20250307092153-cvslm");
		System.out.println(chatContext);
	}
}
