package cn.hutool.core.codec;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.Locale;

public class MorseTest {

	private final Morse morseCoder = new Morse();

	@Test
	public void test0() {
		String text = "Hello World!";
		String morse = "...././.-../.-../---/-...../.--/---/.-./.-../-../-.-.--/";
		assertEquals(morse, morseCoder.encode(text));
		assertEquals(morseCoder.decode(morse), text.toUpperCase());
	}

	@Test
	public void test1() {
		String text = "你好，世界！";
		String morse = "-..----.--...../-.--..-.-----.-/--------....--../-..---....-.--./---.-.-.-..--../--------.......-/";
		assertEquals(morseCoder.encode(text), morse);
		assertEquals(morseCoder.decode(morse), text);
	}

	@Test
	public void test2() {
		String text = "こんにちは";
		String morse = "--.....-.-..--/--....-..-..--/--.....--.-.--/--.....--....-/--.....--.----/";
		assertEquals(morseCoder.encode(text), morse);
		assertEquals(morseCoder.decode(morse), text);
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
