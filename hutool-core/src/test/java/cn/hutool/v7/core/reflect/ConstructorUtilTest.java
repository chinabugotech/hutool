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

package cn.hutool.v7.core.reflect;

import cn.hutool.v7.core.date.Week;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ConstructorUtilTest {
	@Test
	public void noneStaticInnerClassTest() {
		final ReflectTestBeans.NoneStaticClass testAClass = ConstructorUtil.newInstanceIfPossible(ReflectTestBeans.NoneStaticClass.class);
		assertNotNull(testAClass);
		assertEquals(2, testAClass.getA());
	}

	@Test
	public void newInstanceIfPossibleTest(){
		//noinspection ConstantConditions
		final int intValue = ConstructorUtil.newInstanceIfPossible(int.class);
		assertEquals(0, intValue);

		final Integer integer = ConstructorUtil.newInstanceIfPossible(Integer.class);
		assertEquals(Integer.valueOf(0), integer);

		final Map<?, ?> map = ConstructorUtil.newInstanceIfPossible(Map.class);
		assertNotNull(map);

		final Collection<?> collection = ConstructorUtil.newInstanceIfPossible(Collection.class);
		assertNotNull(collection);

		final Week week = ConstructorUtil.newInstanceIfPossible(Week.class);
		assertEquals(Week.SUNDAY, week);

		final int[] intArray = ConstructorUtil.newInstanceIfPossible(int[].class);
		Assertions.assertArrayEquals(new int[0], intArray);
	}

	@Test
	void newInstanceTest() {
		final TestBean testBean = ConstructorUtil.newInstance(TestBean.class);
		Assertions.assertNull(testBean.getA());
		assertEquals(0, testBean.getB());
	}

	@Test
	void newInstanceAllArgsTest() {
		final TestBean testBean = ConstructorUtil.newInstance(TestBean.class, "aValue", 1);
		assertEquals("aValue", testBean.getA());
		assertEquals(1, testBean.getB());
	}

	@Test
	void newInstanceHashtableTest() {
		final Hashtable<?, ?> testBean = ConstructorUtil.newInstance(Hashtable.class);
		assertNotNull(testBean);
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	private static class TestBean{
		private String a;
		private int b;
	}

	@Test
	public void newInstanceIfPossibleTest2() {
		// 测试Object.class不应该被错误地实例化为HashMap，应该返回Object实例
		final Object objectInstance = ConstructorUtil.newInstanceIfPossible(Object.class);
		assertNotNull(objectInstance);
		assertEquals(Object.class, objectInstance.getClass());

		// 测试Map.class能够正确实例化为HashMap
		final Map<?, ?> mapInstance = ConstructorUtil.newInstanceIfPossible(Map.class);
		assertNotNull(mapInstance);
		assertInstanceOf(HashMap.class, mapInstance);

		// 测试Collection.class能够正确实例化为ArrayList
		final Collection<?> collectionInstance = ConstructorUtil.newInstanceIfPossible(Collection.class);
		assertNotNull(collectionInstance);
		assertInstanceOf(ArrayList.class, collectionInstance);


		// 测试List.class能够正确实例化为ArrayList
		final List<?> listInstance = ConstructorUtil.newInstanceIfPossible(List.class);
		assertNotNull(listInstance);
		assertInstanceOf(ArrayList.class, listInstance);

		// 测试Set.class能够正确实例化为HashSet
		final Set<?> setInstance = ConstructorUtil.newInstanceIfPossible(Set.class);
		assertNotNull(setInstance);
		assertInstanceOf(HashSet.class, setInstance);

		// 测试Queue接口能够正确实例化为LinkedList
		final Queue<?> queueInstance = ConstructorUtil.newInstanceIfPossible(Queue.class);
		assertNotNull(queueInstance);
		assertInstanceOf(LinkedList.class, queueInstance);

		// 测试Deque接口能够正确实例化为LinkedList
		final Deque<?> dequeInstance = ConstructorUtil.newInstanceIfPossible(Deque.class);
		assertNotNull(dequeInstance);
		assertInstanceOf(LinkedList.class, dequeInstance);
	}
}
