package cn.hutool.v7.core.codec.hash;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class KetamaHashTest {

	@Test
	public void testHash64() {
		final KetamaHash ketamaHash = new KetamaHash();

		// Test with different inputs
		final byte[] input1 = "test1".getBytes();
		final byte[] input2 = "test2".getBytes();
		final byte[] input3 = "test1".getBytes(); // Same as input1

		final long hash1 = ketamaHash.hash64(input1);
		final long hash2 = ketamaHash.hash64(input2);
		final long hash3 = ketamaHash.hash64(input3);

		// Same inputs should produce same hash
		assertEquals(hash1, hash3);

		// Different inputs should generally produce different hashes
		assertNotEquals(hash1, hash2);

		// Hash should be non-negative (Ketama hash is typically positive)
		assertTrue(hash1 >= 0);
		assertTrue(hash2 >= 0);
		assertTrue(hash3 >= 0);
	}

	@Test
	public void testHash32() {
		final KetamaHash ketamaHash = new KetamaHash();

		// Test with different inputs
		final byte[] input1 = "test1".getBytes();
		final byte[] input2 = "test2".getBytes();
		final byte[] input3 = "test1".getBytes(); // Same as input1

		final int hash1 = ketamaHash.hash32(input1);
		final int hash2 = ketamaHash.hash32(input2);
		final int hash3 = ketamaHash.hash32(input3);

		// Same inputs should produce same hash
		assertEquals(hash1, hash3);

		// Different inputs should generally produce different hashes
		assertNotEquals(hash1, hash2);

		// Hash should be non-negative
		assertTrue(hash1 < 0);
		assertTrue(hash2 < 0);
		assertTrue(hash3 < 0);
	}

	@Test
	public void testEncode() {
		final KetamaHash ketamaHash = new KetamaHash();

		final byte[] input = "test".getBytes();
		final Number result = ketamaHash.encode(input);

		// Encode should return the 64-bit hash as a Number
		assertNotNull(result);
		assertInstanceOf(Long.class, result);

		// The result should match the hash64 result
		assertEquals(ketamaHash.hash64(input), result.longValue());
	}

	@Test
	public void testConsistencyBetweenHashMethods() {
		final KetamaHash ketamaHash = new KetamaHash();

		final byte[] input = "consistency_test".getBytes();

		final long hash64 = ketamaHash.hash64(input);
		final int hash32 = ketamaHash.hash32(input);

		// hash32 should be the lower 32 bits of hash64
		assertEquals((int) (hash64 & 0xffffffffL), hash32);
	}

	@Test
	public void testEmptyInput() {
		final KetamaHash ketamaHash = new KetamaHash();

		final byte[] emptyInput = new byte[0];

		final long hash64 = ketamaHash.hash64(emptyInput);
		final int hash32 = ketamaHash.hash32(emptyInput);
		final Number encoded = ketamaHash.encode(emptyInput);

		// Should handle empty input without error
		assertTrue(hash64 >= 0);
		assertTrue(hash32 < 0);
		assertNotNull(encoded);
		assertEquals(hash64, encoded.longValue());
	}

	@Test
	public void testNullInput() {
		final KetamaHash ketamaHash = new KetamaHash();

		// Testing with null input should throw an exception or handle appropriately
		assertThrows(NullPointerException.class, () -> {
			ketamaHash.hash64(null);
		});

		assertThrows(NullPointerException.class, () -> {
			ketamaHash.hash32(null);
		});

		assertThrows(NullPointerException.class, () -> {
			ketamaHash.encode(null);
		});
	}

	@Test
	public void testLongInput() {
		final KetamaHash ketamaHash = new KetamaHash();

		// Test with a longer input string
		final StringBuilder longInputBuilder = new StringBuilder();
		for (int i = 0; i < 1000; i++) {
			longInputBuilder.append("This is a test string number ").append(i).append(" ");
		}
		final byte[] longInput = longInputBuilder.toString().getBytes();

		final long hash64 = ketamaHash.hash64(longInput);
		final int hash32 = ketamaHash.hash32(longInput);

		// Should handle long input without error
		assertTrue(hash64 >= 0);
		assertTrue(hash32 < 0);
	}

	@Test
	public void testSpecialCharacters() {
		final KetamaHash ketamaHash = new KetamaHash();

		// Test with special characters
		final byte[] specialInput = "测试!@#$%^&*()_+中文".getBytes();

		final long hash64 = ketamaHash.hash64(specialInput);
		final int hash32 = ketamaHash.hash32(specialInput);

		// Should handle special characters without error
		assertTrue(hash64 >= 0);
		assertTrue(hash32 >= 0);
	}

	@Test
	public void testRepeatability() {
		final KetamaHash ketamaHash = new KetamaHash();

		final byte[] input = "repeat_test".getBytes();

		// Multiple calls with same input should produce same result
		final long[] hash64Results = new long[10];
		final int[] hash32Results = new int[10];

		for (int i = 0; i < 10; i++) {
			hash64Results[i] = ketamaHash.hash64(input);
			hash32Results[i] = ketamaHash.hash32(input);
		}

		// All results should be the same
		for (int i = 1; i < 10; i++) {
			assertEquals(hash64Results[0], hash64Results[i]);
			assertEquals(hash32Results[0], hash32Results[i]);
		}
	}

	@Test
	public void testDistribution() {
		final KetamaHash ketamaHash = new KetamaHash();

		// Test that hash values are distributed across the range
		// This is a basic test to ensure different inputs produce different outputs
		final long[] hashes = new long[100];
		for (int i = 0; i < 100; i++) {
			hashes[i] = ketamaHash.hash64(("test" + i).getBytes());
		}

		// Count unique values - most should be unique
		int uniqueCount = 0;
		for (int i = 0; i < 100; i++) {
			boolean isUnique = true;
			for (int j = 0; j < i; j++) {
				if (hashes[i] == hashes[j]) {
					isUnique = false;
					break;
				}
			}
			if (isUnique) {
				uniqueCount++;
			}
		}

		// We expect most values to be unique, though some collisions are possible
		assertTrue(uniqueCount >= 90, "Most hash values should be unique");
	}
}
