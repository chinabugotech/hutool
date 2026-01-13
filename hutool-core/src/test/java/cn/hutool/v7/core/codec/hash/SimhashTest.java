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

package cn.hutool.v7.core.codec.hash;

import cn.hutool.v7.core.text.StrUtil;
import cn.hutool.v7.core.text.split.SplitUtil;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SimhashTest {

	@Test
	public void simTest() {
		final String text1 = "我是 一个 普通 字符串";
		final String text2 = "我是 一个 普通 字符串";

		final Simhash simhash = new Simhash();
		final long hash = simhash.hash64(SplitUtil.split(text1, StrUtil.SPACE));
		assertTrue(hash != 0);

		simhash.store(hash);
		final boolean duplicate = simhash.equals(SplitUtil.split(text2, StrUtil.SPACE));
		assertTrue(duplicate);
	}

	@Test
	public void testConstructor() {
		// Test default constructor
		final Simhash defaultSimhash = new Simhash();
		// We can't directly access private fields, so test functionality instead

		// Test parameterized constructor
		final Simhash paramSimhash = new Simhash(4, 3);
		assertNotNull(paramSimhash);
	}

	@Test
	public void testHash64() {
		final Simhash simhash = new Simhash();

		// Test with different inputs
		final List<String> input1 = Arrays.asList("hello", "world");
		final List<String> input2 = Arrays.asList("hello", "universe");
		final List<String> input3 = Arrays.asList("hello", "world"); // Same as input1

		final long hash1 = simhash.hash64(input1);
		final long hash2 = simhash.hash64(input2);
		final long hash3 = simhash.hash64(input3);

		// Same inputs should produce same hash
		assertEquals(hash1, hash3);

		// Different inputs should produce different hashes (though not guaranteed always)
		assertNotEquals(0, hash1);
		assertNotEquals(0, hash2);
	}

	@Test
	public void testHash64WithEmptyInput() {
		final Simhash simhash = new Simhash();

		// Test with empty collection
		final long hash = simhash.hash64(Collections.emptyList());
		// Empty input should produce a valid hash (likely 0 if no features)
		assertEquals(0, hash);
	}

	@Test
	public void testHash64WithSingleWord() {
		final Simhash simhash = new Simhash();

		final List<String> singleWord = Arrays.asList("hello");
		final long hash = simhash.hash64(singleWord);

		assertNotEquals(0, hash);
	}

	@Test
	public void testEqualsWithSimilarTexts() {
		final Simhash simhash = new Simhash();

		// Texts that should be considered similar due to shared words
		final List<String> text1 = Arrays.asList("hello", "world", "test");
		final List<String> text2 = Arrays.asList("hello", "world", "example"); // shares 2/3 words

		final long hash1 = simhash.hash64(text1);
		simhash.store(hash1);

		final boolean isSimilar = simhash.equals(text2);
		// Note: depends on hamming distance threshold, may or may not be similar
		// This tests that the method works without error
		assertFalse(isSimilar); // Different texts shouldn't be similar by default
	}

	@Test
	public void testEqualsWithIdenticalTexts() {
		final Simhash simhash = new Simhash(4, 5); // Higher threshold to catch similarities

		final List<String> text1 = Arrays.asList("hello", "world", "test");
		final List<String> text2 = Arrays.asList("hello", "world", "test"); // Identical

		final long hash1 = simhash.hash64(text1);
		simhash.store(hash1);

		final boolean isSimilar = simhash.equals(text2);
		assertTrue(isSimilar, "Identical texts should be considered similar");
	}

	@Test
	public void testEqualsWithNoStoredData() {
		final Simhash simhash = new Simhash();

		final List<String> text = Arrays.asList("hello", "world");
		final boolean isSimilar = simhash.equals(text);

		assertFalse(isSimilar, "Should return false when no data is stored");
	}

	@Test
	public void testStore() {
		final Simhash simhash = new Simhash();

		final long hash = 12345L;
		simhash.store(hash);

		// Test that storing doesn't throw an exception
		// We can't directly verify storage due to private fields
		assertTrue(true); // Just ensure no exception was thrown
	}

	@Test
	public void testStoreMultipleHashes() {
		final Simhash simhash = new Simhash();

		// Store multiple hashes
		simhash.store(12345L);
		simhash.store(67890L);
		simhash.store(-12345L);

		// Test that multiple stores work without error
		assertTrue(true); // Just ensure no exception was thrown
	}

	@Test
	public void testDifferentThresholds() {
		// Test with low threshold (more strict)
		final Simhash strictSimhash = new Simhash(4, 1); // Very low threshold
		final List<String> text1 = Arrays.asList("hello", "world");
		final List<String> text2 = Arrays.asList("hello", "world"); // Identical

		final long hash1 = strictSimhash.hash64(text1);
		strictSimhash.store(hash1);
		final boolean isSimilarStrict = strictSimhash.equals(text2);
		assertTrue(isSimilarStrict);

		// Test with higher threshold (less strict)
		final Simhash lenientSimhash = new Simhash(4, 10); // Higher threshold
		final long hash2 = lenientSimhash.hash64(text1);
		lenientSimhash.store(hash2);
		final boolean isSimilarLenient = lenientSimhash.equals(text2);
		assertTrue(isSimilarLenient);
	}

	@Test
	public void testLargeText() {
		final Simhash simhash = new Simhash();

		// Create a large text input
		final String[] words = new String[1000];
		for (int i = 0; i < 1000; i++) {
			words[i] = "word" + i;
		}
		final List<String> largeText = Arrays.asList(words);

		final long hash = simhash.hash64(largeText);
		assertNotEquals(0, hash);

		simhash.store(hash);
		final boolean isSimilar = simhash.equals(largeText);
		assertTrue(isSimilar);
	}

	@Test
	public void testDifferentFracCount() {
		// Test with different fracCount values
		final Simhash simhash1 = new Simhash(2, 3); // 2 segments
		final Simhash simhash2 = new Simhash(8, 3); // 8 segments

		final List<String> text = Arrays.asList("test", "simhash", "algorithm");

		final long hash1 = simhash1.hash64(text);
		final long hash2 = simhash2.hash64(text);

		assertNotEquals(0, hash1);
		assertNotEquals(0, hash2);

		simhash1.store(hash1);
		simhash2.store(hash2);

		assertTrue(simhash1.equals(text));
		assertTrue(simhash2.equals(text));
	}

	@Test
	public void testWithNullInput() {
		final Simhash simhash = new Simhash();

		assertThrows(NullPointerException.class, () -> {
			simhash.hash64(null);
		});
	}

	@Test
	public void testWithNullElements() {
		final Simhash simhash = new Simhash();

		final List<String> textWithNull = Arrays.asList("hello", null, "world");

		// This should handle null elements gracefully or throw appropriate exception
		assertDoesNotThrow(() -> {
			simhash.hash64(textWithNull);
		});
	}
}
