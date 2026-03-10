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

package cn.hutool.v7.core.bean.path;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class BeanPathTest {

	@Test
	void parseDotTest() {
		BeanPath<Object> beanPath = BeanPath.of("userInfo.examInfoDict[0].id");
		assertEquals("userInfo", beanPath.getNode().toString());
		assertEquals("examInfoDict[0].id", beanPath.getChild());

		beanPath = beanPath.next();
		assertEquals("examInfoDict", beanPath.getNode().toString());
		assertEquals("[0].id", beanPath.getChild());

		beanPath = beanPath.next();
		assertEquals("0", beanPath.getNode().toString());
		assertEquals(".id", beanPath.getChild());

		beanPath = beanPath.next();
		assertEquals("id", beanPath.getNode().toString());
		assertNull(beanPath.getChild());
	}

	@Test
	void parseDotWithQuoteTest() {
		BeanPath<Object> beanPath = BeanPath.of("'userInfo'.examInfoDict[0].'id'");
		assertEquals("userInfo", beanPath.getNode().toString());
		assertEquals("examInfoDict[0].'id'", beanPath.getChild());

		beanPath = beanPath.next();
		assertEquals("examInfoDict", beanPath.getNode().toString());
		assertEquals("[0].'id'", beanPath.getChild());

		beanPath = beanPath.next();
		assertEquals("0", beanPath.getNode().toString());
		assertEquals(".'id'", beanPath.getChild());

		beanPath = beanPath.next();
		assertEquals("id", beanPath.getNode().toString());
		assertNull(beanPath.getChild());
	}

	@Test
	void parseDotWithQuoteTest2() {
		BeanPath<Object> beanPath = BeanPath.of("userInfo.'examInfoDict'[0].id");
		assertEquals("userInfo", beanPath.getNode().toString());
		assertEquals("'examInfoDict'[0].id", beanPath.getChild());

		beanPath = beanPath.next();
		assertEquals("examInfoDict", beanPath.getNode().toString());
		assertEquals("[0].id", beanPath.getChild());

		beanPath = beanPath.next();
		assertEquals("0", beanPath.getNode().toString());
		assertEquals(".id", beanPath.getChild());

		beanPath = beanPath.next();
		assertEquals("id", beanPath.getNode().toString());
		assertNull(beanPath.getChild());
	}

	@Test
	void parseBucketTest() {
		BeanPath<Object> beanPath = BeanPath.of("[userInfo][examInfoDict][0][id]");
		assertEquals("userInfo", beanPath.getNode().toString());
		assertEquals("[examInfoDict][0][id]", beanPath.getChild());

		beanPath = beanPath.next();
		assertEquals("examInfoDict", beanPath.getNode().toString());
		assertEquals("[0][id]", beanPath.getChild());

		beanPath = beanPath.next();
		assertEquals("0", beanPath.getNode().toString());
		assertEquals("[id]", beanPath.getChild());

		beanPath = beanPath.next();
		assertEquals("id", beanPath.getNode().toString());
		assertNull(beanPath.getChild());
	}

	@Test
	void parseBucketWithQuoteTest() {
		BeanPath<Object> beanPath = BeanPath.of("['userInfo']['examInfoDict'][0][id]");
		assertEquals("userInfo", beanPath.getNode().toString());
		assertEquals("['examInfoDict'][0][id]", beanPath.getChild());

		beanPath = beanPath.next();
		assertEquals("examInfoDict", beanPath.getNode().toString());
		assertEquals("[0][id]", beanPath.getChild());

		beanPath = beanPath.next();
		assertEquals("0", beanPath.getNode().toString());
		assertEquals("[id]", beanPath.getChild());

		beanPath = beanPath.next();
		assertEquals("id", beanPath.getNode().toString());
		assertNull(beanPath.getChild());
	}

	@Test
	void parseBucketWithQuoteTest2() {
		BeanPath<Object> beanPath = BeanPath.of("[userInfo][examInfoDict][0]['id']");
		assertEquals("userInfo", beanPath.getNode().toString());
		assertEquals("[examInfoDict][0]['id']", beanPath.getChild());

		beanPath = beanPath.next();
		assertEquals("examInfoDict", beanPath.getNode().toString());
		assertEquals("[0]['id']", beanPath.getChild());

		beanPath = beanPath.next();
		assertEquals("0", beanPath.getNode().toString());
		assertEquals("['id']", beanPath.getChild());

		beanPath = beanPath.next();
		assertEquals("id", beanPath.getNode().toString());
		assertNull(beanPath.getChild());
	}

	@Test
	void rangePathTest() {
		BeanPath<Object> beanPath = BeanPath.of("[userInfo][2:3]");
		assertEquals("userInfo", beanPath.getNode().toString());
		assertEquals("[2:3]", beanPath.getChild());

		beanPath = beanPath.next();
		assertEquals("[2:3:1]", beanPath.getNode().toString());
		assertNull(beanPath.getChild());
	}

	@Test
	void listPathTest() {
		BeanPath<Object> beanPath = BeanPath.of("[userInfo][1,2,3]");
		assertEquals("userInfo", beanPath.getNode().toString());
		assertEquals("[1,2,3]", beanPath.getChild());

		beanPath = beanPath.next();
		assertEquals("[1, 2, 3]", beanPath.getNode().toString());
		assertNull(beanPath.getChild());
	}

	@Test
	void listKeysPathTest() {
		BeanPath<Object> beanPath = BeanPath.of("[userInfo]['a', 'b', 'c']");
		assertEquals("userInfo", beanPath.getNode().toString());
		assertEquals("['a', 'b', 'c']", beanPath.getChild());

		beanPath = beanPath.next();
		assertEquals("[a, b, c]", beanPath.getNode().toString());
		assertNull(beanPath.getChild());
	}
}
