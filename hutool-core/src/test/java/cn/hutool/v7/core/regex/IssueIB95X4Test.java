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

package cn.hutool.v7.core.regex;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IssueIB95X4Test {
	@Test
	void isMacTest() {
		Assertions.assertTrue(ReUtil.isMatch(PatternPool.MAC_ADDRESS, "ab1c.2d3e.f468"));
		Assertions.assertTrue(ReUtil.isMatch(PatternPool.MAC_ADDRESS, "ab:1c:2d:3e:f4:68"));
		Assertions.assertTrue(ReUtil.isMatch(PatternPool.MAC_ADDRESS, "ab-1c-2d-3e-f4-68"));
		Assertions.assertTrue(ReUtil.isMatch(PatternPool.MAC_ADDRESS, "ab1c2d3ef468"));
	}
}
