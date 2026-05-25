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

package cn.hutool.ai.model.deepseek;

import cn.hutool.ai.AIServiceFactory;
import cn.hutool.ai.ModelName;
import cn.hutool.ai.core.AIConfigBuilder;
import cn.hutool.ai.core.Message;
import cn.hutool.core.thread.ThreadUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class DeepSeekServiceTest {

	String key = "your key";
	DeepSeekService deepSeekService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.DEEPSEEK.getValue()).setApiKey(key).build(),DeepSeekService.class);

	@Test
	@Disabled
	void chat(){
		final String chat = deepSeekService.chat("写一个疯狂星期四广告词");
		assertNotNull(chat);
	}

	@Test
	@Disabled
	void chatStream() {
		String prompt = "写一个疯狂星期四广告词";
		// 使用AtomicBoolean作为结束标志
		AtomicBoolean isDone = new AtomicBoolean(false);

		deepSeekService.chat(prompt, data -> {
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
		final String chat = deepSeekService.chat(messages);
		assertNotNull(chat);
	}

	@Test
	@Disabled
	void beta() {
		final String beta = deepSeekService.beta("写一个疯狂星期四广告词");
		assertNotNull(beta);

	}

	@Test
	@Disabled
	void betaStream() {
		String beta = "写一个疯狂星期四广告词";
		// 使用AtomicBoolean作为结束标志
		AtomicBoolean isDone = new AtomicBoolean(false);

		deepSeekService.beta(beta, data -> {
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
	void models() {
		final String models = deepSeekService.models();
		assertNotNull(models);
	}

	@Test
	@Disabled
	void balance() {
		final String balance = deepSeekService.balance();
		assertNotNull(balance);
	}
}
