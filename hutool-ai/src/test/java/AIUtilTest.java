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

import cn.hutool.ai.AIUtil;
import cn.hutool.ai.ModelName;
import cn.hutool.ai.core.AIConfigBuilder;
import cn.hutool.ai.core.AIService;
import cn.hutool.ai.core.Message;
import cn.hutool.ai.model.deepseek.DeepSeekService;
import cn.hutool.ai.model.doubao.DoubaoService;
import cn.hutool.ai.model.grok.GrokService;
import cn.hutool.ai.model.hutool.HutoolService;
import cn.hutool.ai.model.openai.OpenaiService;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class AIUtilTest {

	String key = "your key";

	@Test
	void getAIService() {
		final DeepSeekService deepSeekService = AIUtil.getAIService(new AIConfigBuilder(ModelName.DEEPSEEK.getValue()).setApiKey(key).build(), DeepSeekService.class);
		assertNotNull(deepSeekService);
	}

	@Test
	void testGetAIService() {
		final AIService aiService = AIUtil.getAIService(new AIConfigBuilder(ModelName.OPENAI.getValue()).setApiKey(key).build());
		assertNotNull(aiService);
	}

	@Test
	void getHutoolService() {
		final HutoolService hutoolService = AIUtil.getHutoolService(new AIConfigBuilder(ModelName.HUTOOL.getValue()).setApiKey(key).build());
		assertNotNull(hutoolService);
	}

	@Test
	void getDeepSeekService() {
		final DeepSeekService deepSeekService = AIUtil.getDeepSeekService(new AIConfigBuilder(ModelName.DEEPSEEK.getValue()).setApiKey(key).build());
		assertNotNull(deepSeekService);
	}

	@Test
	void getDoubaoService() {
		final DoubaoService doubaoService = AIUtil.getDoubaoService(new AIConfigBuilder(ModelName.DOUBAO.getValue()).setApiKey(key).build());
		assertNotNull(doubaoService);
	}

	@Test
	void getGrokService() {
		final GrokService grokService = AIUtil.getGrokService(new AIConfigBuilder(ModelName.GROK.getValue()).setApiKey(key).build());
		assertNotNull(grokService);
	}

	@Test
	void getOpenAIService() {
		final OpenaiService openAIService = AIUtil.getOpenAIService(new AIConfigBuilder(ModelName.OPENAI.getValue()).setApiKey(key).build());
		assertNotNull(openAIService);
	}

	@Test
	void chat() {
		final String chat = AIUtil.chat(new AIConfigBuilder(ModelName.DEEPSEEK.getValue()).setApiKey(key).build(), "写一首赞美我的诗");
		assertNotNull(chat);
	}

	@Test
	void testChat() {
		final List<Message> messages = new ArrayList<>();
		messages.add(new Message("system","你是财神爷，只会说“我是财神”"));
		messages.add(new Message("user","你是谁啊？"));
		final String chat = AIUtil.chat(new AIConfigBuilder(ModelName.DEEPSEEK.getValue()).setApiKey(key).build(), messages);
		assertNotNull(chat);
	}
}
