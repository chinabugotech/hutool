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

package cn.hutool.v7.core.lang.ref;

import java.lang.ref.ReferenceQueue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PhantomObjTest {

	private ReferenceQueue<String> queue;
    private String testObject;
    private PhantomObj<String> phantomObj;

    @BeforeEach
    void setUp() {
        queue = new ReferenceQueue<>();
        testObject = "test";
        phantomObj = new PhantomObj<>(testObject, queue);
    }

	@Test
	@DisplayName("测试 equals 方法与不同引用对象比较")
    void testEqualsWithDifferentReferent() {
        final String differentObject = "different";
        final PhantomObj<String> anotherPhantomObj = new PhantomObj<>(differentObject, queue);
        assertFalse(phantomObj.equals(anotherPhantomObj));
    }
}
