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

package cn.hutool.v7.core.reflect;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class ModifierUtilTest {

	@Test
	public void hasAnyTest() throws NoSuchMethodException {
		final Method method = ModifierUtilTest.class.getDeclaredMethod("ddd");
		Assertions.assertTrue(ModifierUtil.hasAny(method,
			ModifierType.PRIVATE));
		Assertions.assertTrue(ModifierUtil.hasAny(method,
			ModifierType.PRIVATE,
			ModifierType.STATIC)
		);
		Assertions.assertTrue(ModifierUtil.hasAny(method,
			ModifierType.PRIVATE,
			ModifierType.ABSTRACT)
		);
	}

	@Test
	public void hasAllTest() throws NoSuchMethodException {
		final Method method = ModifierUtilTest.class.getDeclaredMethod("ddd");
		Assertions.assertTrue(ModifierUtil.hasAll(method,
			ModifierType.PRIVATE));
		Assertions.assertTrue(ModifierUtil.hasAll(method,
			ModifierType.PRIVATE,
			ModifierType.STATIC)
		);
		Assertions.assertFalse(ModifierUtil.hasAll(method,
			ModifierType.PRIVATE,
			// 不存在
			ModifierType.ABSTRACT)
		);
	}

	@Test
	void issueIAQ2U0Test() throws NoSuchMethodException {
		final Method method = ModifierUtilTest.class.getDeclaredMethod("ddd");

		Assertions.assertTrue(ModifierUtil.hasAny(method,
			ModifierType.PRIVATE,
			ModifierType.STATIC,
			// 不存在
			ModifierType.TRANSIENT
		));

		Assertions.assertFalse(ModifierUtil.hasAll(method,
			ModifierType.PRIVATE,
			ModifierType.STATIC,
			// 不存在
			ModifierType.TRANSIENT
		));
	}

	private static void ddd() {
	}

	@SuppressWarnings("unused")
	public static class JdbcDialects {
		private static final List<Number> DIALECTS =
			Arrays.asList(1L, 2L, 3L);
	}
}
