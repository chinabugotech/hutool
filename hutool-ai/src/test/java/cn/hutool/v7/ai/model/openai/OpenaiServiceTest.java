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

package cn.hutool.v7.ai.model.openai;

import cn.hutool.v7.ai.AIServiceFactory;
import cn.hutool.v7.ai.ModelName;
import cn.hutool.v7.ai.Models;
import cn.hutool.v7.ai.core.AIConfigBuilder;
import cn.hutool.v7.ai.core.Message;
import cn.hutool.v7.core.io.file.FileUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class OpenaiServiceTest {

	String key = "your key";
	OpenaiService openaiService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.OPENAI.getValue()).setApiKey(key).build(), OpenaiService.class);


	@Test
	@Disabled
	void chat(){
		final String chat = openaiService.chat("写一个疯狂星期四广告词");
		System.out.println(chat);
	}

	@Test
	@Disabled
	void testChat(){
		final List<Message> messages = new ArrayList<>();
		messages.add(new Message("system","你是个抽象大师，会说很抽象的话，最擅长说抽象的笑话"));
		messages.add(new Message("user","给我说一个笑话"));
		final String chat = openaiService.chat(messages);
		System.out.println(chat);
	}

	@Test
	@Disabled
	void chatVision() {
		final OpenaiService openaiService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.OPENAI.getValue())
			.setApiKey(key).setModel(Models.Openai.GPT_4O_MINI.getModel()).build(), OpenaiService.class);
		final String chatVision = openaiService.chatVision("图片上有些什么？", Arrays.asList("https://img2.baidu.com/it/u=862000265,4064861820&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=1544","https://img2.baidu.com/it/u=1682510685,1244554634&fm=253&fmt=auto&app=138&f=JPEG?w=803&h=800"));
		System.out.println(chatVision);
	}

	@Test
	@Disabled
	void imagesGenerations() {
		final OpenaiService openaiService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.OPENAI.getValue())
			.setApiKey(key).setModel(Models.Openai.DALL_E_3.getModel()).build(), OpenaiService.class);
		final String imagesGenerations = openaiService.imagesGenerations("一位年轻的宇航员站在未来感十足的太空站内，透过巨大的弧形落地窗凝望浩瀚宇宙。窗外，璀璨的星河与五彩斑斓的星云交织，远处隐约可见未知星球的轮廓，仿佛在召唤着探索的脚步。宇航服上的呼吸灯与透明显示屏上的星图交相辉映，象征着人类科技与宇宙奥秘的碰撞。画面深邃而神秘，充满对未知的渴望与无限可能的想象。");
		System.out.println(imagesGenerations);
		//https://oaidalleapiprodscus.blob.core.windows.net/private/org-l99H6T0zCZejctB2TqdYrXFB/user-LilDVU1V8cUxJYwVAGRkUwYd/img-yA9kNatHnBiUHU5lZGim1hP2.png?st=2025-03-07T01%3A04%3A18Z&se=2025-03-07T03%3A04%3A18Z&sp=r&sv=2024-08-04&sr=b&rscd=inline&rsct=image/png&skoid=d505667d-d6c1-4a0a-bac7-5c84a87759f8&sktid=a48cca56-e6da-484e-a814-9c849652bcb3&skt=2025-03-06T15%3A04%3A42Z&ske=2025-03-07T15%3A04%3A42Z&sks=b&skv=2024-08-04&sig=rjcRzC5U7Y3pEDZ4ME0CiviAPdIpoGO2rRTXw3m8rHw%3D
	}

	@Test
	@Disabled
	void imagesEdits() {
		final OpenaiService openaiService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.OPENAI.getValue())
			.setApiKey(key).setModel(Models.Openai.DALL_E_2.getModel()).build(), OpenaiService.class);
		final File file = FileUtil.file("your imgUrl");
		final String imagesEdits = openaiService.imagesEdits("茂密的森林中，有一只九色鹿若隐若现",file);
		System.out.println(imagesEdits);
	}

	@Test
	@Disabled
	void imagesVariations() {
		final OpenaiService openaiService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.OPENAI.getValue())
			.setApiKey(key).setModel(Models.Openai.DALL_E_2.getModel()).build(), OpenaiService.class);
		final File file = FileUtil.file("your imgUrl");
		final String imagesVariations = openaiService.imagesVariations(file);
		System.out.println(imagesVariations);
	}

	@Test
	@Disabled
	void textToSpeech() {
		final OpenaiService openaiService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.OPENAI.getValue())
			.setApiKey(key).setModel(Models.Openai.TTS_1_HD.getModel()).build(), OpenaiService.class);
		final InputStream inputStream = openaiService.textToSpeech("万里山河一夜白，\n" +
			"千峰尽染玉龙哀。\n" +
			"长风卷起琼花碎，\n" +
			"直上九霄揽月来。", OpenaiCommon.OpenaiSpeech.NOVA);

		final String filePath = "your filePath";
		final Path path = Paths.get(filePath);
		try (final FileOutputStream outputStream = new FileOutputStream(filePath)) {
			Files.createDirectories(path.getParent());
			final byte[] buffer = new byte[1024];
			int bytesRead;
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}

	}

	@Test
	@Disabled
	void speechToText() {
		final OpenaiService openaiService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.OPENAI.getValue())
			.setApiKey(key).setModel(Models.Openai.WHISPER_1.getModel()).build(), OpenaiService.class);
		final File file = FileUtil.file("your filePath");
		final String speechToText = openaiService.speechToText(file);
		System.out.println(speechToText);
	}

	@Test
	@Disabled
	void embeddingText() {
		final OpenaiService openaiService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.OPENAI.getValue())
			.setApiKey(key).setModel(Models.Openai.TEXT_EMBEDDING_3_SMALL.getModel()).build(), OpenaiService.class);
		final String embeddingText = openaiService.embeddingText("萬里山河一夜白,千峰盡染玉龍哀,長風捲起瓊花碎,直上九霄闌月來");
		System.out.println(embeddingText);
	}

	@Test
	@Disabled
	void moderations() {
		final OpenaiService openaiService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.OPENAI.getValue())
			.setApiKey(key).setModel(Models.Openai.OMNI_MODERATION_LATEST.getModel()).build(), OpenaiService.class);
		final String moderations = openaiService.moderations("你要杀人", "https://img2.baidu.com/it/u=862000265,4064861820&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=1544");
		System.out.println(moderations);
	}

	@Test
	@Disabled
	void chatReasoning() {
		final OpenaiService openaiService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.OPENAI.getValue())
			.setApiKey(key).setModel(Models.Openai.O3_MINI.getModel()).build(), OpenaiService.class);
		final List<Message> messages = new ArrayList<>();
		messages.add(new Message("system","你是现代抽象家"));
		messages.add(new Message("user","给我一个KFC疯狂星期四的文案"));
		final String chatReasoning = openaiService.chatReasoning(messages, OpenaiCommon.OpenaiReasoning.HIGH.getEffort());
		System.out.println(chatReasoning);
	}
}
