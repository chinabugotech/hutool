/*
 * Copyright (c) 2013-2025 Hutool Team and hutool.cn
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

package cn.hutool.v7.core.data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PasswdStrengthTest {
	@Test
	public void strengthTest(){
		final String passwd = "2hAj5#mne-ix.86H";
		assertEquals(13, PasswdStrength.check(passwd));
	}

	@Test
	public void strengthNumberTest(){
		final String passwd = "9999999999999";
		assertEquals(0, PasswdStrength.check(passwd));
	}

	@Test
	void strengthEmptyTest(){
		final String passwd = null;
		assertThrows(IllegalArgumentException.class, () -> PasswdStrength.check(passwd));

		final String passwd2 = "";
		assertThrows(IllegalArgumentException.class, () -> PasswdStrength.check(passwd2));
	}

	@Test
	void strengthBlankTest(){
		String passwd = " ";
		assertEquals(0, PasswdStrength.check(passwd));

		passwd = "   ";
		assertEquals(0, PasswdStrength.check(passwd));
	}

	@Test
	public void consecutiveLettersTest() {
		// 测试连续小写字母会被降级
		assertEquals(0, PasswdStrength.check("abcdefghijklmn"));
		// 测试连续大写字母会被降级
		assertEquals(0, PasswdStrength.check("ABCDEFGHIJKLMN"));
	}

	@Test
	public void dictionaryWeakPasswordTest() {
		// 测试包含简单密码字典中的弱密码
		assertEquals(0, PasswdStrength.check("password"));
		assertEquals(3, PasswdStrength.check("password2"));
	}

	@Test
	public void numericSequenceTest() {
		assertEquals(0, PasswdStrength.check("01234567890"));
		assertEquals(0, PasswdStrength.check("09876543210"));
	}
}
