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

package cn.hutool.ai.core;

import cn.hutool.ai.ModelName;
import cn.hutool.json.JSONUtil;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLStreamHandler;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class BaseAIServiceCharsetTest {

	private static final ByteArrayOutputStream CAPTURED = new ByteArrayOutputStream();

	static {
		// Intercept only the custom "aiutf8" protocol; every other protocol falls back to the JDK default.
		URL.setURLStreamHandlerFactory(protocol -> "aiutf8".equals(protocol)
			? new URLStreamHandler() {
				@Override
				protected HttpURLConnection openConnection(final URL u) {
					return new HttpURLConnection(u) {
						@Override public void connect() {}
						@Override public void disconnect() {}
						@Override public boolean usingProxy() { return false; }
						@Override public OutputStream getOutputStream() { return CAPTURED; }
						@Override public InputStream getInputStream() { return new ByteArrayInputStream(new byte[0]); }
					};
				}
			}
			: null);
	}

	@Test
	void streamRequestBodyIsEncodedAsUtf8() {
		CAPTURED.reset();

		final AIConfig config = new AIConfigBuilder(ModelName.HUTOOL.getValue()).setApiKey("test-key").build();
		config.setApiUrl("aiutf8://ai.example.com");

		final BaseAIService service = new BaseAIService(config);

		final Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("prompt", "你好，世界"); // non-ASCII text

		service.sendPostStream("/chat", paramMap, line -> {});

		final byte[] expected = JSONUtil.toJsonStr(paramMap).getBytes(StandardCharsets.UTF_8);
		assertArrayEquals(expected, CAPTURED.toByteArray(),
			"streaming request body must be UTF-8 encoded regardless of the JVM default charset");
	}
}
