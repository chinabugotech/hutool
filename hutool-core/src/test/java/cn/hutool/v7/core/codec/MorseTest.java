/*
 * Copyright (c) 2013-2026 Hutool Team.
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

package cn.hutool.v7.core.codec;

import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MorseTest {

	private final Morse morseCoder = new Morse();

	@Test
	public void test0() {
		final String text = "Hello World!";
		final String morse = "...././.-../.-../---/-...../.--/---/.-./.-../-../-.-.--/";
		assertEquals(morse, morseCoder.encode(text));
		assertEquals(morseCoder.decode(morse), text.toUpperCase(Locale.ROOT));
	}

	@Test
	public void test1() {
		final String text = "你好，世界！";
		final String morse = "-..----.--...../-.--..-.-----.-/--------....--../-..---....-.--./---.-.-.-..--../--------.......-/";
		assertEquals(morse, morseCoder.encode(text));
		assertEquals(text, morseCoder.decode(morse));
	}

	@Test
	public void test2() {
		final String text = "こんにちは";
		final String morse = "--.....-.-..--/--....-..-..--/--.....--.-.--/--.....--....-/--.....--.----/";
		assertEquals(morse, morseCoder.encode(text));
		assertEquals(text, morseCoder.decode(morse));
	}

	@Test
	public void turkishLocaleTest() {
		final Locale defaultLocale = Locale.getDefault();
		Locale.setDefault(Locale.forLanguageTag("tr-TR"));
		try {
			// 土耳其语下 i 会转成 İ，不能影响摩斯编码
			assertEquals("../", morseCoder.encode("i"));
			assertEquals("../", morseCoder.encode("I"));
		} finally {
			Locale.setDefault(defaultLocale);
		}
	}
}
