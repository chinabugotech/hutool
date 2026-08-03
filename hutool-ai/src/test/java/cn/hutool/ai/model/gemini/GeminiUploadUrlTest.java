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

package cn.hutool.ai.model.gemini;

import cn.hutool.ai.ModelName;
import cn.hutool.ai.core.AIConfig;
import cn.hutool.ai.core.AIConfigBuilder;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GeminiUploadUrlTest {

	@Test
	void uploadBaseUrlForCustomEndpointIsWellFormed() throws Exception {
		// A reverse-proxy / custom node endpoint (not the official host).
		final AIConfig config = new AIConfigBuilder(ModelName.GEMINI.getValue()).setApiKey("test-key").build();
		config.setApiUrl("https://myproxy.example.com/v1beta");

		final GeminiServiceImpl service = new GeminiServiceImpl(config);
		final Method getUploadBaseUrl = GeminiServiceImpl.class.getDeclaredMethod("getUploadBaseUrl");
		getUploadBaseUrl.setAccessible(true);

		final String uploadUrl = (String) getUploadBaseUrl.invoke(service);

		assertEquals("https://myproxy.example.com/upload/v1beta/files", uploadUrl,
			"the upload URL must keep the proxy host and append only the upload path");
	}
}
