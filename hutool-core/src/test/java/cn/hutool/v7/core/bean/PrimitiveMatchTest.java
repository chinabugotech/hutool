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

package cn.hutool.v7.core.bean;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PrimitiveMatchTest {

	@Test
	void setMatchPrimitiveTest(){
		final TestUser testUser = new TestUser();
		StrictBeanDesc strictBeanDesc = new StrictBeanDesc(TestUser.class, false, true);
		strictBeanDesc.getSetter("id").invoke(testUser, 12);
		Assertions.assertEquals(12, testUser.getId());

		// 不匹配原始类型
		strictBeanDesc = new StrictBeanDesc(TestUser.class, false, false);
		Assertions.assertNull(strictBeanDesc.getSetter("id"));
	}

	static class TestUser{
		private int id;

		public Integer getId() {
			return id;
		}

		public void setId(final Integer id) {
			this.id = id;
		}
	}
}
