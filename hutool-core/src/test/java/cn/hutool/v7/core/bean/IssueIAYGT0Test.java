/*
 * Copyright (c) 2025 Hutool Team and hutool.cn
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

public class IssueIAYGT0Test {
	@Test
	void setPropertyTest() {
		final Cat cat = new Cat();
		// 优先调用setter方法
		BeanUtil.setProperty(cat, "name", "Kitty");
		Assertions.assertEquals("RedKitty", cat.getName());
	}

	static class Cat {
		private String name;

		public void setName(final String name) {
			this.name = "Red" + name;
		}

		public String getName() {
			return name;
		}
	}
}
