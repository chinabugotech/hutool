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

package cn.hutool.v7.core.codec;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RadixUtilTest {
	@Test
	public void issueIDFPGRTest() {
		final String radixs = "0123456789ABC"; // base 13
		final String bad = "1X3"; // 'X' 不在 radix 中
		assertThrows(IllegalArgumentException.class, () -> {
			RadixUtil.decode(radixs, bad);
		});
	}

	@Test
	public void testEncodeInt() {
		// Test binary conversion (base 2)
		assertEquals("1010", RadixUtil.encode("01", 10));

		// Test base 3 conversion
		assertEquals("101", RadixUtil.encode("012", 10));

		// Test base 16 conversion
		assertEquals("A", RadixUtil.encode("0123456789ABCDEF", 10));

		// Test with 34-radix
		assertEquals("Y", RadixUtil.encode(RadixUtil.RADIXS_34, 32));

		// Test with 59-radix
		assertEquals("b", RadixUtil.encode(RadixUtil.RADIXS_59, 11));
	}

	@Test
	public void testEncodeLong() {
		// Test binary conversion (base 2)
		assertEquals("1010", RadixUtil.encode("01", 10L));

		// Test base 3 conversion
		assertEquals("101", RadixUtil.encode("012", 10L));

		// Test larger number
		assertEquals("RR", RadixUtil.encode("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ", 999L));

		// Test with shuffle 34-radix
		assertEquals("R", RadixUtil.encode(RadixUtil.RADIXS_SHUFFLE_34, 22));
	}

	@Test
	public void testDecodeToInt() {
		// Test binary decoding (base 2)
		assertEquals(10, RadixUtil.decodeToInt("01", "1010"));

		// Test base 3 decoding
		assertEquals(10, RadixUtil.decodeToInt("012", "101"));

		// Test base 16 decoding
		assertEquals(10, RadixUtil.decodeToInt("0123456789ABCDEF", "A"));

		// Test with 34-radix
		assertEquals(30, RadixUtil.decodeToInt(RadixUtil.RADIXS_34, "W"));
	}

	@Test
	public void testDecode() {
		// Test binary decoding (base 2)
		assertEquals(10L, RadixUtil.decode("01", "1010"));

		// Test base 3 decoding
		assertEquals(10L, RadixUtil.decode("012", "101"));

		// Test base 16 decoding
		assertEquals(10L, RadixUtil.decode("0123456789ABCDEF", "A"));

		// Test with 59-radix
		assertEquals(11L, RadixUtil.decode(RadixUtil.RADIXS_59, "b"));

		// Test with shuffle 59-radix
		assertEquals(1L, RadixUtil.decode(RadixUtil.RADIXS_SHUFFLE_59, "vh"));
	}

	@Test
	public void testEncodeDecodeRoundTrip() {
		// Test round trip for various bases
		assertEquals(42, RadixUtil.decodeToInt("01", RadixUtil.encode("01", 42)));
		assertEquals(123, RadixUtil.decodeToInt("0123456789", RadixUtil.encode("0123456789", 123)));
		assertEquals(255, RadixUtil.decodeToInt("0123456789ABCDEF", RadixUtil.encode("0123456789ABCDEF", 255)));

		// Test with predefined radixes
		assertEquals(1000, RadixUtil.decodeToInt(RadixUtil.RADIXS_34, RadixUtil.encode(RadixUtil.RADIXS_34, 1000)));
		assertEquals(2000, RadixUtil.decodeToInt(RadixUtil.RADIXS_59, RadixUtil.encode(RadixUtil.RADIXS_59, 2000)));
		assertEquals(500, RadixUtil.decodeToInt(RadixUtil.RADIXS_SHUFFLE_34, RadixUtil.encode(RadixUtil.RADIXS_SHUFFLE_34, 500)));
		assertEquals(750, RadixUtil.decodeToInt(RadixUtil.RADIXS_SHUFFLE_59, RadixUtil.encode(RadixUtil.RADIXS_SHUFFLE_59, 750)));
	}

	@Test
	public void testEdgeCases() {
		// Test zero
		assertEquals("0", RadixUtil.encode("01", 0));
		assertEquals(0, RadixUtil.decodeToInt("01", "0"));

		// Test single digit numbers
		assertEquals("1", RadixUtil.encode("01", 1));
		assertEquals(1, RadixUtil.decodeToInt("01", "1"));

		// Test with larger numbers
		assertEquals("11111111", RadixUtil.encode("01", 255)); // 255 in binary
		assertEquals(255, RadixUtil.decodeToInt("01", "11111111"));

		// Test negative numbers (using the special handling in encode)
		assertEquals(4294967254L, RadixUtil.decode("01", RadixUtil.encode("01", -42)));
	}

	@SuppressWarnings("ConstantValue")
	@Test
	public void testPredefinedRadixes() {
		// Test 34-radix constants
		assertNotNull(RadixUtil.RADIXS_34);
		assertEquals(34, RadixUtil.RADIXS_34.length());
		assertFalse(RadixUtil.RADIXS_34.contains("I"));
		assertFalse(RadixUtil.RADIXS_34.contains("O"));

		// Test 59-radix constants
		assertNotNull(RadixUtil.RADIXS_59);
		assertEquals(59, RadixUtil.RADIXS_59.length());
		assertFalse(RadixUtil.RADIXS_59.contains("I"));
		assertFalse(RadixUtil.RADIXS_59.contains("O"));
		assertFalse(RadixUtil.RADIXS_59.contains("l"));

		// Test shuffle radixes
		assertNotNull(RadixUtil.RADIXS_SHUFFLE_34);
		assertEquals(34, RadixUtil.RADIXS_SHUFFLE_34.length());

		assertNotNull(RadixUtil.RADIXS_SHUFFLE_59);
		assertEquals(59, RadixUtil.RADIXS_SHUFFLE_59.length());
	}

	@Test
	public void testInvalidInputs() {
		// Test invalid radix (too short)
		assertThrows(RuntimeException.class, () -> RadixUtil.encode("0", 10)); // only 1 char

		// Test decode with null radix
		assertThrows(IllegalArgumentException.class, () -> RadixUtil.decode(null, "10"));

		// Test decode with empty string
		assertThrows(IllegalArgumentException.class, () -> RadixUtil.decode("01", ""));

		// Test decode with invalid character (already tested in original method)
		final String radixs = "0123456789ABC"; // base 13
		final String bad = "1X3"; // 'X' 不在 radix 中
		assertThrows(IllegalArgumentException.class, () -> RadixUtil.decode(radixs, bad));
	}

	@Test
	public void testLongValueEncodeDecode() {
		final long testValue = 1234567890L;
		final String encoded = RadixUtil.encode("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ", testValue);
		final long decoded = RadixUtil.decode("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ", encoded);
		assertEquals(testValue, decoded);
	}
}
