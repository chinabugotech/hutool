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

package cn.hutool.v7.ai.model.gemini;

import cn.hutool.v7.ai.ModelName;
import cn.hutool.v7.ai.core.AIConfig;
import cn.hutool.v7.ai.core.AIConfigBuilder;
import cn.hutool.v7.ai.core.Message;
import cn.hutool.v7.http.client.Response;
import cn.hutool.v7.json.JSONObject;
import cn.hutool.v7.json.JSONUtil;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class GeminiChatJsonTest {
	/** Sentinel used to capture the serialized request body without performing a real HTTP call. */
	private static final class CapturedBody extends RuntimeException {
		final String body;
		CapturedBody(final String body) { this.body = body; }
	}

	@Test
	void chatJsonRequestCarriesResponseMimeType() {
		final AIConfig config = new AIConfigBuilder(ModelName.GEMINI.getValue()).setApiKey("test-key").build();

		final GeminiServiceImpl service = new GeminiServiceImpl(config) {
			@Override
			protected Response sendPost(final String endpoint, final String paramJson) {
				throw new CapturedBody(paramJson);
			}
		};

		try {
			service.chatJson(Collections.singletonList(new Message("user", "hello")));
			fail("expected chatJson to issue a POST request");
		} catch (final CapturedBody captured) {
			final JSONObject request = JSONUtil.parseObj(captured.body);
			final JSONObject generationConfig = request.getJSONObject("generationConfig");
			assertNotNull(generationConfig, "chatJson must attach generationConfig to the request");
			assertEquals("application/json", generationConfig.getStr("response_mime_type"),
				"chatJson must request JSON output via response_mime_type");
		}
	}
}
