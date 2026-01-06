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

package cn.hutool.v7.core.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Issue3809Test {
	@Test
	void roundStrTest() {
		Assertions.assertEquals("9999999999999999.99", NumberUtil.roundStr("9999999999999999.99", 2));  //输出结果不符合方法声明返回值规则
		Assertions.assertEquals("11111111111111119.00", NumberUtil.roundStr("11111111111111119.00", 2));
		Assertions.assertEquals("7999999999999999.99", NumberUtil.roundStr("7999999999999999.99", 2)); //输出结果不符合方法声明返回值规则
		Assertions.assertEquals("699999999991999.92", NumberUtil.roundStr("699999999991999.92", 2)); //输出结果不符合方法声明返回值规则
		Assertions.assertEquals("10.92", NumberUtil.roundStr("10.92", 2));
		Assertions.assertEquals("10.99", NumberUtil.roundStr("10.99", 2));
	}
}
