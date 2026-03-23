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

package cn.hutool.ai.model.grok;

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

class GrokServiceTest {

	String key = "your key";
	GrokService grokService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.GROK.getValue()).setApiKey(key).build(), GrokService.class);


	@Test
	@Disabled
	void chat(){
		final String chat = grokService.chat("写一个疯狂星期四广告词");
		assertNotNull(chat);
	}

	@Test
	@Disabled
	void chatStream() {
		String prompt = "写一个疯狂星期四广告词";
		// 使用AtomicBoolean作为结束标志
		AtomicBoolean isDone = new AtomicBoolean(false);

		grokService.chat(prompt, data -> {
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
		final String chat = grokService.chat(messages);
		assertNotNull(chat);
	}

	@Test
	@Disabled
	void message() {
		final String message = grokService.message("给我一个KFC的广告词", 4096);
		assertNotNull(message);
	}

	@Test
	@Disabled
	void messageStream() {
		String prompt = "给我一个KFC的广告词";

		// 使用AtomicBoolean作为结束标志
		AtomicBoolean isDone = new AtomicBoolean(false);
		grokService.message(prompt, 4096, data -> {
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
	void chatVision() {
		final GrokService grokService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.GROK.getValue()).setModel(Models.Grok.GROK_2_VISION_1212.getModel()).setApiKey(key).build(), GrokService.class);
		final String base64 = ImgUtil.toBase64DataUri(Toolkit.getDefaultToolkit().createImage("your imageUrl"), "png");
		final String chatVision = grokService.chatVision("图片上有些什么？", Arrays.asList(base64));
		assertNotNull(chatVision);
	}

	@Test
	@Disabled
	void testChatVisionStream() {
		final GrokService grokService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.GROK.getValue()).setModel(Models.Grok.GROK_2_VISION_1212.getModel()).setApiKey(key).build(), GrokService.class);
		String prompt = "图片上有些什么？";
		List<String> images = Arrays.asList("https://img2.baidu.com/it/u=862000265,4064861820&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=1544");

		// 使用AtomicBoolean作为结束标志
		AtomicBoolean isDone = new AtomicBoolean(false);
		grokService.chatVision(prompt,images, data -> {
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
		final GrokService grokService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.GROK.getValue()).setModel(Models.Grok.GROK_2_VISION_1212.getModel()).setApiKey(key).build(), GrokService.class);
		final String chatVision = grokService.chatVision("图片上有些什么？", Arrays.asList("https://img2.baidu.com/it/u=862000265,4064861820&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=1544"));
		assertNotNull(chatVision);
	}

	@Test
	@Disabled
	void models() {
		final String models = grokService.models();
		assertNotNull(models);
	}

	@Test
	@Disabled
	void getModel() {
		final String model = grokService.getModel("");
		assertNotNull(model);
	}

	@Test
	@Disabled
	void languageModels() {
		final String languageModels = grokService.languageModels();
		assertNotNull(languageModels);
	}

	@Test
	@Disabled
	void getLanguageModel() {
		final String language = grokService.getLanguageModel("");
		assertNotNull(language);
	}

	@Test
	@Disabled
	void tokenizeText() {
		final String tokenizeText = grokService.tokenizeText(key);
		assertNotNull(tokenizeText);
	}

	@Test
	@Disabled
	void deferredCompletion() {
		final String deferred = grokService.deferredCompletion(key);
		assertNotNull(deferred);
	}

	@Test
	@Disabled
	void imagesGenerations() {
		final GrokService grokService  = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.GROK.getValue())
			.setApiKey(key).setModel(Models.Grok.GROK_2_IMAGE.getModel()).build(), GrokService.class);
		final String imagesGenerations = grokService.imagesGenerations("一位年轻的宇航员站在未来感十足的太空站内，透过巨大的弧形落地窗凝望浩瀚宇宙。窗外，璀璨的星河与五彩斑斓的星云交织，远处隐约可见未知星球的轮廓，仿佛在召唤着探索的脚步。宇航服上的呼吸灯与透明显示屏上的星图交相辉映，象征着人类科技与宇宙奥秘的碰撞。画面深邃而神秘，充满对未知的渴望与无限可能的想象。");
		assertNotNull(imagesGenerations);
	}
}
