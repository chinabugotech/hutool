package org.dromara.hutool.ai;

import org.dromara.hutool.ai.core.AIConfigBuilder;
import org.dromara.hutool.ai.core.AIService;
import org.dromara.hutool.ai.model.deepseek.DeepSeekService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AIServiceFactoryTest {

	String key = "your key";

	@Test
	void getAIService() {
		AIService aiService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.DEEPSEEK.getValue()).setApiKey(key).build());
		assertNotNull(aiService);
	}

	@Test
	void testGetAIService() {
		DeepSeekService deepSeekService = AIServiceFactory.getAIService(new AIConfigBuilder(ModelName.DEEPSEEK.getValue()).setApiKey(key).build(), DeepSeekService.class);
		assertNotNull(deepSeekService);
	}
}
