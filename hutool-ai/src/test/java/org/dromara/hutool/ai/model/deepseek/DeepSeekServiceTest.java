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

package org.dromara.hutool.ai.model.deepseek;

import org.dromara.hutool.ai.AIServiceFactory;
import org.dromara.hutool.ai.ModelName;
import org.dromara.hutool.ai.core.AIConfigBuilder;
import org.dromara.hutool.ai.core.Message;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class DeepSeekServiceTest {

	String key = "your key";
	DeepSeekService deepSeekService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.DEEPSEEK.getValue()).setApiKey(key).build(),DeepSeekService.class);

	@Test
	void chat(){
		String chat = deepSeekService.chat("写一个疯狂星期四广告词");
		System.out.println(chat);
	}

	@Test
	void testChat(){
		List<Message> messages = new ArrayList<>();
		messages.add(new Message("system","你是个抽象大师，会说很抽象的话，最擅长说抽象的笑话"));
		messages.add(new Message("user","给我说一个笑话"));
		String chat = deepSeekService.chat(messages);
		System.out.println(chat);
	}

	@Test
	void beta() {
		String beta = deepSeekService.beta("写一个疯狂星期四广告词");
		System.out.println(beta);
	}

	@Test
	void models() {
		String models = deepSeekService.models();
		System.out.println(models);
	}

	@Test
	void balance() {
		String balance = deepSeekService.balance();
		System.out.println(balance);
	}
}
