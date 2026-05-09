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

package cn.hutool.v7.core.reflect.kotlin;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class KClassUtilTest {

	@Test
	void isKotlinClassTest() {
		boolean kotlinClass = KClassUtil.isKotlinClass(TestKBean.class);
		assertTrue(kotlinClass);

		kotlinClass = KClassUtil.isKotlinClass(KClassUtilTest.class);
		assertFalse(kotlinClass);
	}

	@Test
	void getConstructorTest() {
		final List<?> constructors = KClassUtil.getConstructors(TestKBean.class);
		assertEquals(1, constructors.size());

		assertEquals("kotlin.reflect.jvm.internal.KotlinKConstructor",
			constructors.get(0).getClass().getName());
	}

	@Test
	void getParametersTest() {
		final List<?> constructors = KClassUtil.getConstructors(TestKBean.class);

		final List<KParameter> parameters = KClassUtil.getParameters(constructors.get(0));
		assertEquals(3, parameters.size());

		assertEquals("id", parameters.get(0).getName());
		assertEquals("name", parameters.get(1).getName());
		assertEquals("country", parameters.get(2).getName());
	}

	@Test
	void newInstanceTest() {
		final HashMap<String, Object> argsMap = new HashMap<>();
		argsMap.put("country", "中国");
		argsMap.put("age", 18);
		argsMap.put("id", "VampireAchao");

		final TestKBean testKBean = KClassUtil.newInstance(TestKBean.class, argsMap);

		assertEquals("VampireAchao", testKBean.getId());
		assertEquals("中国", testKBean.getCountry());
		assertNull(testKBean.getName());
	}
}
