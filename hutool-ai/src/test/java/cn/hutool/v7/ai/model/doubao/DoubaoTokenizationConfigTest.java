/*
 * Copyright (c) 2026 Hutool Team.
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

package cn.hutool.v7.ai.model.doubao;

import cn.hutool.v7.ai.ModelName;
import cn.hutool.v7.ai.core.AIConfig;
import cn.hutool.v7.ai.core.AIConfigBuilder;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DoubaoTokenizationConfigTest {
	@Test
	void tokenizationRequestIncludesAdditionalConfig() throws Exception {
		final AIConfig config = new AIConfigBuilder(ModelName.DOUBAO.getValue())
			.setApiKey("test-key")
			.putAdditionalConfig("custom_param", "custom_value")
			.build();
		final DoubaoServiceImpl service = new DoubaoServiceImpl(config);

		final Method build = DoubaoServiceImpl.class.getDeclaredMethod("buildTokenizationRequestBody", String[].class);
		build.setAccessible(true);
		final String body = (String) build.invoke(service, (Object) new String[]{"hello"});

		assertTrue(body.contains("custom_param"),
			"tokenization request must forward user-set additional config, like the other Doubao endpoints (body: " + body + ")");
	}
}
