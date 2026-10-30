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

package cn.hutool.v7.core.codec.binary;

import cn.hutool.v7.core.util.RandomUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Base62单元测试
 *
 * @author Looly
 *
 */
public class Base62Test {

	@Test
	public void encodeAndDecodeTest() {
		final String a = "伦家是一个非常长的字符串66";
		final String encode = Base62.encode(a);
		assertEquals("17vKU8W4JMG8dQF8lk9VNnkdMOeWn4rJMva6F0XsLrrT53iKBnqo", encode);

		final String decodeStr = Base62.decodeStr(encode);
		assertEquals(a, decodeStr);
	}

	@Test
	public void encodeAndDecodeInvertedTest() {
		final String a = "伦家是一个非常长的字符串66";
		final String encode = Base62.encodeInverted(a);
		assertEquals("17Vku8w4jmg8Dqf8LK9vnNKDmoEwN4RjmVA6f0xSlRRt53IkbNQO", encode);

		final String decodeStr = Base62.decodeStrInverted(encode);
		assertEquals(a, decodeStr);
	}

	@Test
	public void encodeAndDecodeRandomTest() {
		final String a = RandomUtil.randomLettersAndNumbersLower(RandomUtil.randomInt(1000));
		final String encode = Base62.encode(a);
		final String decodeStr = Base62.decodeStr(encode);
		assertEquals(a, decodeStr);
	}

	@Test
	public void encodeAndDecodeInvertedRandomTest() {
		final String a = RandomUtil.randomLettersAndNumbersLower(RandomUtil.randomInt(1000));
		final String encode = Base62.encodeInverted(a);
		final String decodeStr = Base62.decodeStrInverted(encode);
		assertEquals(a, decodeStr);
	}

	@Test
	void encodeNumbersTest() {
		final String encode = Base62.encode("181338494");
		assertEquals("HRmWh8NiFvYi", encode);
	}

	@Test
	public void decodeInvalidCharTest() {
		// 不在字母表中的合法ASCII字符（如'+'）应抛出异常，而非被静默当作'0'解码出错误数据
		assertThrows(IllegalArgumentException.class, () -> Base62.decode("+HelloWorld123".getBytes()));
		assertThrows(IllegalArgumentException.class, () -> Base62.decodeInverted("+HelloWorld123".getBytes()));
		assertThrows(IllegalArgumentException.class, () -> Base62.decodeStr("http://hutool.cn"));
	}

	@Test
	public void decodeOutOfRangeCharTest() {
		// 大于'z'的字符与高位字节（负数byte）应抛出IllegalArgumentException，而非ArrayIndexOutOfBoundsException
		assertThrows(IllegalArgumentException.class, () -> Base62.decode("{".getBytes()));
		assertThrows(IllegalArgumentException.class, () -> Base62.decode(new byte[]{(byte) 0xE4, (byte) 0xB8, (byte) 0xAD}));
	}
}
