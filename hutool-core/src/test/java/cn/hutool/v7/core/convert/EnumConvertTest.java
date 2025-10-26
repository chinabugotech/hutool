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

package cn.hutool.v7.core.convert;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Enum转换单元测试
 */
public class EnumConvertTest {

	@Test
	public void convertTest(){
		TestEnum bbb = ConvertUtil.convert(TestEnum.class, "BBB");
		Assertions.assertEquals(TestEnum.B, bbb);

		bbb = ConvertUtil.convert(TestEnum.class, 22);
		Assertions.assertEquals(TestEnum.B, bbb);
	}

	@Test
	public void toEnumTest(){
		TestEnum ccc = ConvertUtil.toEnum(TestEnum.class, "CCC");
		Assertions.assertEquals(TestEnum.C, ccc);

		ccc = ConvertUtil.toEnum(TestEnum.class, 33);
		Assertions.assertEquals(TestEnum.C, ccc);
	}

	enum TestEnum {
		A, B, C;

		public static TestEnum parse(final String str) {
			return switch (str) {
				case "AAA" -> A;
				case "BBB" -> B;
				case "CCC" -> C;
				default -> null;
			};
		}

		public static TestEnum parseByNumber(final int i) {
			return switch (i) {
				case 11 -> A;
				case 22 -> B;
				case 33 -> C;
				default -> null;
			};
		}
	}
}
