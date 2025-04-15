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

package cn.hutool.v7.ai.model.deepseek;

import cn.hutool.v7.ai.AIServiceFactory;
import cn.hutool.v7.ai.ModelName;
import cn.hutool.v7.ai.core.AIConfigBuilder;
import cn.hutool.v7.ai.core.Message;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class DeepSeekServiceTest {

	String key = "your key";
	DeepSeekService deepSeekService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.DEEPSEEK.getValue()).setApiKey(key).build(),DeepSeekService.class);

	@Test
	@Disabled
	void chat(){
		final String chat = deepSeekService.chat("写一个疯狂星期四广告词");
		System.out.println(chat);
	}

	@Test
	@Disabled
	void testChat(){
		final List<Message> messages = new ArrayList<>();
		messages.add(new Message("system","你是个抽象大师，会说很抽象的话，最擅长说抽象的笑话"));
		messages.add(new Message("user","给我说一个笑话"));
		final String chat = deepSeekService.chat(messages);
		System.out.println(chat);
	}

	@Test
	@Disabled
	void beta() {
		final String beta = deepSeekService.beta("写一个疯狂星期四广告词");
		System.out.println(beta);
	}

	@Test
	@Disabled
	void models() {
		final String models = deepSeekService.models();
		System.out.println(models);
	}

	@Test
	@Disabled
	void balance() {
		final String balance = deepSeekService.balance();
		System.out.println(balance);
	}
}
