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

public class NumberWordFormatTest {

	@Test
	public void formatTest() {
		final String format = EnglishNumberFormatter.format(100.23);
		Assertions.assertEquals("ONE HUNDRED AND CENTS TWENTY THREE ONLY", format);

		final String format2 = EnglishNumberFormatter.format("2100.00");
		Assertions.assertEquals("TWO THOUSAND ONE HUNDRED AND CENTS  ONLY", format2);

		final String format3 = EnglishNumberFormatter.format("1234567890123.12");
		Assertions.assertEquals("ONE TRILLION TWO HUNDRED AND THIRTY FOUR BILLION FIVE HUNDRED AND SIXTY SEVEN MILLION EIGHT HUNDRED AND NINETY THOUSAND ONE HUNDRED AND TWENTY THREE AND CENTS TWELVE ONLY", format3);
	}

	@Test
	public void formatSimpleTest() {
		final String format1 = EnglishNumberFormatter.formatSimple(1200, false);
		Assertions.assertEquals("1.2k", format1);

		final String format2 = EnglishNumberFormatter.formatSimple(4384324, false);
		Assertions.assertEquals("4.38m", format2);

		final String format3 = EnglishNumberFormatter.formatSimple(4384324, true);
		Assertions.assertEquals("438.43w", format3);

		final String format4 = EnglishNumberFormatter.formatSimple(4384324);
		Assertions.assertEquals("438.43w", format4);

		final String format5 = EnglishNumberFormatter.formatSimple(438);
		Assertions.assertEquals("438", format5);
	}

	@Test
	public void formatSimpleTest2(){
		final String s = EnglishNumberFormatter.formatSimple(1000);
		Assertions.assertEquals("1k", s);
	}

	@Test
	public void issue4033Test(){
		String s = EnglishNumberFormatter.formatSimple(1_000, false);
		Assertions.assertEquals("1k", s);

		s = EnglishNumberFormatter.formatSimple(10_000, false);
		Assertions.assertEquals("10k", s);

		s = EnglishNumberFormatter.formatSimple(100_000, false);
		Assertions.assertEquals("100k", s);

		s = EnglishNumberFormatter.formatSimple(1_000_000, false);
		Assertions.assertEquals("1m", s);

		s = EnglishNumberFormatter.formatSimple(10_000_000, false);
		Assertions.assertEquals("10m", s);

		s = EnglishNumberFormatter.formatSimple(100_000_000, false);
		Assertions.assertEquals("100m", s);

		s = EnglishNumberFormatter.formatSimple(1_000_000_000, false);
		Assertions.assertEquals("1b", s);
	}

	@Test
	public void issue4033Test2(){
		String s = EnglishNumberFormatter.formatSimple(1_000, true);
		Assertions.assertEquals("1k", s);

		s = EnglishNumberFormatter.formatSimple(10_000, true);
		Assertions.assertEquals("1w", s);

		s = EnglishNumberFormatter.formatSimple(100_000, true);
		Assertions.assertEquals("10w", s);

		s = EnglishNumberFormatter.formatSimple(1_000_000, true);
		Assertions.assertEquals("100w", s);

		s = EnglishNumberFormatter.formatSimple(10_000_000, true);
		Assertions.assertEquals("1000w", s);

		s = EnglishNumberFormatter.formatSimple(100_000_000, true);
		Assertions.assertEquals("10000w", s);

		s = EnglishNumberFormatter.formatSimple(1_000_000_000, true);
		Assertions.assertEquals("1b", s);
	}
}
