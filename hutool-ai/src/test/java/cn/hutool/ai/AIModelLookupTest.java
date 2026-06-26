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

package cn.hutool.ai;

import cn.hutool.ai.core.AIConfigBuilder;
import cn.hutool.ai.model.openai.OpenaiConfig;
import cn.hutool.ai.model.openai.OpenaiService;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AIModelLookupTest {

	private static final Locale TURKISH = Locale.forLanguageTag("tr-TR");

	@Test
	void builderShouldResolveUppercaseModelNameWithTurkishLocale() {
		final Locale previous = Locale.getDefault();
		Locale.setDefault(TURKISH);
		try {
			assertNotNull(new AIConfigBuilder("OPENAI").build());
		} finally {
			Locale.setDefault(previous);
		}
	}

	@Test
	void serviceFactoryShouldResolveUppercaseModelNameWithTurkishLocale() {
		final Locale previous = Locale.getDefault();
		Locale.setDefault(TURKISH);
		try {
			final OpenaiService service = AIServiceFactory.getAIService(new UppercaseOpenaiConfig(), OpenaiService.class);
			assertNotNull(service);
		} finally {
			Locale.setDefault(previous);
		}
	}

	@Test
	void builderShouldRejectBlankModelName() {
		final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new AIConfigBuilder("  "));
		assertEquals("Model name must not be blank", exception.getMessage());
	}

	private static class UppercaseOpenaiConfig extends OpenaiConfig {
		@Override
		public String getModelName() {
			return "OPENAI";
		}
	}
}
