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

package cn.hutool.v7.core.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BooleanUtilTest {

	@Test
	public void toBooleanTest() {
		assertTrue(BooleanUtil.toBoolean("true"));
		assertTrue(BooleanUtil.toBoolean("yes"));
		assertTrue(BooleanUtil.toBoolean("y"));
		assertTrue(BooleanUtil.toBoolean("t"));
		assertTrue(BooleanUtil.toBoolean("OK"));
		assertTrue(BooleanUtil.toBoolean("correct"));
		assertTrue(BooleanUtil.toBoolean("success"));
		assertTrue(BooleanUtil.toBoolean("1"));
		assertTrue(BooleanUtil.toBoolean("On"));
		assertTrue(BooleanUtil.toBoolean("是"));
		assertTrue(BooleanUtil.toBoolean("对"));
		assertTrue(BooleanUtil.toBoolean("真"));
		assertTrue(BooleanUtil.toBoolean("對"));
		assertTrue(BooleanUtil.toBoolean("正确"));
		assertTrue(BooleanUtil.toBoolean("开"));
		assertTrue(BooleanUtil.toBoolean("开启"));
		assertTrue(BooleanUtil.toBoolean("√"));
		assertTrue(BooleanUtil.toBoolean("☑"));

		assertFalse(BooleanUtil.toBoolean("false"));
		assertFalse(BooleanUtil.toBoolean("no"));
		assertFalse(BooleanUtil.toBoolean("n"));
		assertFalse(BooleanUtil.toBoolean("f"));
		assertFalse(BooleanUtil.toBoolean("off"));
		assertFalse(BooleanUtil.toBoolean("wrong"));
		assertFalse(BooleanUtil.toBoolean("fail"));
		assertFalse(BooleanUtil.toBoolean("0"));
		assertFalse(BooleanUtil.toBoolean("Off"));
		assertFalse(BooleanUtil.toBoolean("否"));
		assertFalse(BooleanUtil.toBoolean("错"));
		assertFalse(BooleanUtil.toBoolean("假"));
		assertFalse(BooleanUtil.toBoolean("錯"));
		assertFalse(BooleanUtil.toBoolean("错误"));
		assertFalse(BooleanUtil.toBoolean("关"));
		assertFalse(BooleanUtil.toBoolean("关闭"));
		assertFalse(BooleanUtil.toBoolean("×"));
		assertFalse(BooleanUtil.toBoolean("☒"));
		assertFalse(BooleanUtil.toBoolean("6455434"));
		assertFalse(BooleanUtil.toBoolean(""));
	}

	@Test
	public void andTest() {
		assertFalse(BooleanUtil.and(true, false));
		assertFalse(BooleanUtil.andOfWrap(true, false));
	}

	@Test
	public void orTest() {
		assertTrue(BooleanUtil.or(true, false));
		assertTrue(BooleanUtil.orOfWrap(true, false));
	}

	@Test
	public void xorTest() {
		assertTrue(BooleanUtil.xor(true, false));
		assertTrue(BooleanUtil.xor(true, true, true));
		assertFalse(BooleanUtil.xor(true, true, false));
		assertTrue(BooleanUtil.xor(true, false, false));
		assertFalse(BooleanUtil.xor(false, false, false));

		assertTrue(BooleanUtil.xorOfWrap(true, false));
		assertTrue(BooleanUtil.xorOfWrap(true, true, true));
		assertFalse(BooleanUtil.xorOfWrap(true, true, false));
		assertTrue(BooleanUtil.xorOfWrap(true, false, false));
		assertFalse(BooleanUtil.xorOfWrap(false, false, false));
	}

	@SuppressWarnings("ConstantConditions")
	@Test
	public void isTrueIsFalseTest() {
		assertFalse(BooleanUtil.isTrue(null));
		assertFalse(BooleanUtil.isFalse(null));
	}

	@Test
	public void orOfWrapTest() {
		assertFalse(BooleanUtil.orOfWrap(Boolean.FALSE, null));
		assertTrue(BooleanUtil.orOfWrap(Boolean.TRUE, null));
	}

	@SuppressWarnings("ConstantConditions")
	@Test
	public void negateTest() {
		assertFalse(BooleanUtil.negate(Boolean.TRUE));
		assertTrue(BooleanUtil.negate(Boolean.FALSE));

		assertFalse(BooleanUtil.negate(Boolean.TRUE.booleanValue()));
		assertTrue(BooleanUtil.negate(Boolean.FALSE.booleanValue()));
	}

	@Test
	public void toStringTest() {
		Assertions.assertEquals("true", BooleanUtil.toStringTrueFalse(true));
		Assertions.assertEquals("false", BooleanUtil.toStringTrueFalse(false));

		Assertions.assertEquals("yes", BooleanUtil.toStringYesNo(true));
		Assertions.assertEquals("no", BooleanUtil.toStringYesNo(false));

		Assertions.assertEquals("on", BooleanUtil.toStringOnOff(true));
		Assertions.assertEquals("off", BooleanUtil.toStringOnOff(false));
	}

	@SuppressWarnings("ConstantConditions")
	@Test
	public void toBooleanObjectTest(){
		assertTrue(BooleanUtil.toBooleanObject("yes"));
		assertTrue(BooleanUtil.toBooleanObject("真"));
		assertTrue(BooleanUtil.toBooleanObject("是"));
		assertTrue(BooleanUtil.toBooleanObject("√"));

		Assertions.assertNull(BooleanUtil.toBooleanObject(null));
		Assertions.assertNull(BooleanUtil.toBooleanObject("不识别"));
	}

	@Test
	public void issue3587Test() {
		final Boolean boolean1 = true;
		final Boolean boolean2 = null;
		final Boolean result = BooleanUtil.andOfWrap(boolean1, boolean2);
		assertFalse(result);
	}
}
