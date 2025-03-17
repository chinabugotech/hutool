package cn.hutool.ai.model.doubao;

import cn.hutool.ai.AIServiceFactory;
import cn.hutool.ai.ModelName;
import cn.hutool.ai.Models;
import cn.hutool.ai.core.AIConfigBuilder;
import cn.hutool.ai.core.Message;
import cn.hutool.core.img.ImgUtil;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DoubaoServiceTest {

	String key = "your key";
	DoubaoService doubaoService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.DOUBAO.getValue()).setModel(Models.Doubao.DOUBAO_1_5_LITE_32K.getModel()).setApiKey(key).build(), DoubaoService.class);

	@Test
	void chat(){
		String chat = doubaoService.chat("写一个疯狂星期四广告词");
		System.out.println(chat);
	}

	@Test
	void testChat(){
		List<Message> messages = new ArrayList<>();
		messages.add(new Message("system","你是个抽象大师，会说很抽象的话，最擅长说抽象的笑话"));
		messages.add(new Message("user","给我说一个笑话"));
		String chat = doubaoService.chat(messages);
		System.out.println(chat);
	}

	@Test
	void chatVision() {
		DoubaoService doubaoService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.DOUBAO.getValue())
			.setApiKey(key).setModel(Models.Doubao.DOUBAO_1_5_VISION_PRO_32K.getModel()).build(), DoubaoService.class);
		String base64 = ImgUtil.toBase64DataUri(Toolkit.getDefaultToolkit().createImage("your imageUrl"), "png");
		String chatVision = doubaoService.chatVision("图片上有些什么？", Arrays.asList(base64));
		System.out.println(chatVision);
	}

	@Test
	void testChatVision() {
		DoubaoService doubaoService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.DOUBAO.getValue())
			.setApiKey(key).setModel(Models.Doubao.DOUBAO_1_5_VISION_PRO_32K.getModel()).build(), DoubaoService.class);
		String chatVision = doubaoService.chatVision("图片上有些什么？", Arrays.asList("https://img2.baidu.com/it/u=862000265,4064861820&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=1544"),DoubaoCommon.DoubaoVision.HIGH.getDetail());
		System.out.println(chatVision);
	}

	@Test
	void videoTasks() {
		DoubaoService doubaoService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.DOUBAO.getValue())
			.setApiKey(key).setModel("your Endpoint ID").build(), DoubaoService.class);
		String videoTasks = doubaoService.videoTasks("生成一段动画视频，主角是大耳朵图图，一个活泼可爱的小男孩。视频中图图在公园里玩耍，" +
			"画面采用明亮温暖的卡通风格，色彩鲜艳，动作流畅。背景音乐轻快活泼，带有冒险感，音效包括鸟叫声、欢笑声和山洞回声。", "https://img2.baidu.com/it/u=862000265,4064861820&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=1544");
		System.out.println(videoTasks);//cgt-20250306170051-6r9gk
	}

	@Test
	void getVideoTasksInfo() {
		//cgt-20250306170051-6r9gk
		DoubaoService doubaoService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.DOUBAO.getValue())
			.setApiKey(key).build(), DoubaoService.class);
		String videoTasksInfo = doubaoService.getVideoTasksInfo("cgt-20250306170051-6r9gk");
		System.out.println(videoTasksInfo);
	}

	@Test
	void embeddingText() {
		DoubaoService doubaoService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.DOUBAO.getValue())
			.setApiKey(key).setModel(Models.Doubao.DOUBAO_EMBEDDING_TEXT_240715.getModel()).build(), DoubaoService.class);
		String embeddingText = doubaoService.embeddingText(new String[]{"阿斯顿", "马丁"});
		System.out.println(embeddingText);
	}

	@Test
	void embeddingVision() {
		DoubaoService doubaoService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.DOUBAO.getValue())
			.setApiKey(key).setModel(Models.Doubao.DOUBAO_EMBEDDING_VISION.getModel()).build(), DoubaoService.class);
		String embeddingVision = doubaoService.embeddingVision("天空好难", "https://img2.baidu.com/it/u=862000265,4064861820&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=1544");
		System.out.println(embeddingVision);
	}

	@Test
	void botsChat() {
		DoubaoService doubaoService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.DOUBAO.getValue())
			.setApiKey(key).setModel("your bots id").build(), DoubaoService.class);
		ArrayList<Message> messages = new ArrayList<>();
		messages.add(new Message("system","你是什么都可以"));
		messages.add(new Message("user","你想做些什么"));
		String botsChat = doubaoService.botsChat(messages);
		System.out.println(botsChat);
	}

	@Test
	void tokenization() {
		String tokenization = doubaoService.tokenization(new String[]{"阿斯顿", "马丁"});
		System.out.println(tokenization);
	}

	@Test
	void batchChat() {
		DoubaoService doubaoService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.DOUBAO.getValue())
			.setApiKey(key).setModel("your Endpoint ID").build(), DoubaoService.class);
		String batchChat = doubaoService.batchChat("写首歌词");
		System.out.println(batchChat);
	}

	@Test
	void testBatchChat() {
		DoubaoService doubaoService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.DOUBAO.getValue())
			.setApiKey(key).setModel("your Endpoint ID").build(), DoubaoService.class);
		List<Message> messages = new ArrayList<>();
		messages.add(new Message("system","你是个抽象大师"));
		messages.add(new Message("user","写一个KFC的抽象广告"));
		String batchChat = doubaoService.batchChat(messages);
		System.out.println(batchChat);
	}

	@Test
	void createContext() {
		DoubaoService doubaoService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.DOUBAO.getValue())
			.setApiKey(key).setModel("your Endpoint ID").build(), DoubaoService.class);
		List<Message> messages = new ArrayList<>();
		messages.add(new Message("system","你是个抽象大师,你真的很抽象"));
		String context = doubaoService.createContext(messages);//ctx-20250307092153-cvslm
		System.out.println(context);
	}

	@Test
	void testCreateContext() {
		DoubaoService doubaoService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.DOUBAO.getValue())
			.setApiKey(key).setModel("your Endpoint ID").build(), DoubaoService.class);
		List<Message> messages = new ArrayList<>();
		messages.add(new Message("system","你是个抽象大师,你真的很抽象"));
		String context = doubaoService.createContext(messages,DoubaoCommon.DoubaoContext.COMMON_PREFIX.getMode());
		System.out.println(context);//ctx-20250307092153-cvslm
	}

	@Test
	void chatContext() {
		//ctx-20250307092153-cvslm
		DoubaoService doubaoService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.DOUBAO.getValue())
			.setApiKey(key).setModel("eyour Endpoint ID").build(), DoubaoService.class);
		String chatContext = doubaoService.chatContext("你是谁？", "ctx-20250307092153-cvslm");
		System.out.println(chatContext);
	}

	@Test
	void testChatContext() {
		DoubaoService doubaoService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.DOUBAO.getValue())
			.setApiKey(key).setModel("your Endpoint ID").build(), DoubaoService.class);
		List<Message> messages = new ArrayList<>();
		messages.add(new Message("user","你怎么看待意大利面拌水泥？"));
		String chatContext = doubaoService.chatContext(messages, "ctx-20250307092153-cvslm");
		System.out.println(chatContext);
	}
}
