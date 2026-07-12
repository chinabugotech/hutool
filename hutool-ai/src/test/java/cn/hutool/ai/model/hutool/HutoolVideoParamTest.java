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

package cn.hutool.ai.model.hutool;

import cn.hutool.ai.ModelName;
import cn.hutool.ai.core.AIConfig;
import cn.hutool.ai.core.AIConfigBuilder;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HutoolVideoParamTest {

	@Test
	void videoParamFlagAndValueAreSpaceSeparatedWithoutText() throws Exception {
		final AIConfig config = new AIConfigBuilder(ModelName.HUTOOL.getValue()).setApiKey("test-key").build();
		final HutoolServiceImpl service = new HutoolServiceImpl(config);

		final Method build = HutoolServiceImpl.class.getDeclaredMethod(
			"buildGenerationsTasksRequestBody", String.class, String.class, List.class);
		build.setAccessible(true);

		// Blank text exercises the else branch that assembles the parameter string from scratch.
		final String body = (String) build.invoke(service, "", "https://example.com/a.png",
			Collections.singletonList(HutoolCommon.HutoolVideo.RATIO_16_9));

		assertTrue(body.contains("--rt 16:9"),
			"video flag and value must be space-separated (body: " + body + ")");
		assertFalse(body.contains("--rt16:9"),
			"video flag and value must not be concatenated (body: " + body + ")");
	}
}
