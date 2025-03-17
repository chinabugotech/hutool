package cn.hutool.ai.model.deepseek;

import cn.hutool.ai.AIServiceFactory;
import cn.hutool.ai.ModelName;
import cn.hutool.ai.core.AIConfigBuilder;
import cn.hutool.ai.core.Message;
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
