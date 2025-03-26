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

package org.dromara.hutool.ai.model.grok;

import org.dromara.hutool.ai.AIServiceFactory;
import org.dromara.hutool.ai.ModelName;
import org.dromara.hutool.ai.Models;
import org.dromara.hutool.ai.core.AIConfigBuilder;
import org.dromara.hutool.ai.core.Message;
import org.dromara.hutool.swing.img.ImgUtil;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GrokServiceTest {

	String key = "your key";
	GrokService grokService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.GROK.getValue()).setApiKey(key).build(), GrokService.class);


	@Test
	void chat(){
		String chat = grokService.chat("写一个疯狂星期四广告词");
		System.out.println(chat);
	}

	@Test
	void testChat(){
		List<Message> messages = new ArrayList<>();
		messages.add(new Message("system","你是个抽象大师，会说很抽象的话，最擅长说抽象的笑话"));
		messages.add(new Message("user","给我说一个笑话"));
		String chat = grokService.chat(messages);
		System.out.println(chat);
	}

	@Test
	void message() {
		String message = grokService.message("给我一个KFC的广告词", 4096);
		System.out.println(message);
	}

	@Test
	void chatVision() {
		GrokService grokService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.GROK.getValue()).setModel(Models.Grok.GROK_2_VISION_1212.getModel()).setApiKey(key).build(), GrokService.class);
		String base64 = ImgUtil.toBase64DataUri(Toolkit.getDefaultToolkit().createImage("your imageUrl"), "png");
		String chatVision = grokService.chatVision("图片上有些什么？", Arrays.asList(base64));
		System.out.println(chatVision);
	}

	@Test
	void testChatVision() {
		GrokService grokService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.GROK.getValue()).setModel(Models.Grok.GROK_2_VISION_1212.getModel()).setApiKey(key).build(), GrokService.class);
		String chatVision = grokService.chatVision("图片上有些什么？", Arrays.asList("https://img2.baidu.com/it/u=862000265,4064861820&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=1544"));
		System.out.println(chatVision);
	}

	@Test
	void models() {
		String models = grokService.models();
		assertNotNull(models);
	}

	@Test
	void getModel() {
		String model = grokService.getModel("");
		assertNotNull(model);
	}

	@Test
	void languageModels() {
		String languageModels = grokService.languageModels();
		assertNotNull(languageModels);
	}

	@Test
	void getLanguageModel() {
		String language = grokService.getLanguageModel("");
		assertNotNull(language);
	}

	@Test
	void tokenizeText() {
		String tokenizeText = grokService.tokenizeText(key);
		assertNotNull(tokenizeText);
	}

	@Test
	void deferredCompletion() {
		String deferred = grokService.deferredCompletion(key);
		assertNotNull(deferred);
	}
}
