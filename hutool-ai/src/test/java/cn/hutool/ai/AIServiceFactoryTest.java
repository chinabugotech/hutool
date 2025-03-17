package cn.hutool.ai;

import cn.hutool.ai.core.AIConfigBuilder;
import cn.hutool.ai.core.AIService;
import cn.hutool.ai.model.deepseek.DeepSeekService;
import cn.hutool.ai.model.doubao.DoubaoService;
import cn.hutool.ai.model.grok.GrokService;
import cn.hutool.ai.model.openai.OpenaiService;
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
