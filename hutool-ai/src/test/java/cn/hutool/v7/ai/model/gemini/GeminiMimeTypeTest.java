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

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GeminiMimeTypeTest {
	@Test
	void detectsMimeTypeOfUrlWithQueryStringAndFragment() throws Exception {
		final Method detect = GeminiServiceImpl.class.getDeclaredMethod("detectMimeType", String.class);
		detect.setAccessible(true);

		// CDN / presigned image URLs commonly carry a query string or fragment; the extension
		// is still ".png", so the MIME type must resolve to image/png rather than being lost.
		assertEquals("image/png", detect.invoke(null, "https://cdn.example.com/photo.png?v=123&x=1"));
		assertEquals("image/png", detect.invoke(null, "https://cdn.example.com/photo.png#section"));
		// A plain URL must still resolve exactly as before.
		assertEquals("image/png", detect.invoke(null, "https://cdn.example.com/photo.png"));
	}
}
