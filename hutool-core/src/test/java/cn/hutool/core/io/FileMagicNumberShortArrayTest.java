package cn.hutool.core.io;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Regression test: the AAC, M4A and AMR matchers must return {@code false} for byte arrays too short
 * to contain their magic number, instead of throwing {@link ArrayIndexOutOfBoundsException}.
 */
class FileMagicNumberShortArrayTest {

	@Test
	void shortArraysReturnFalseInsteadOfThrowing() {
		for (int len = 0; len <= 12; len++) {
			final byte[] bytes = new byte[len];
			assertFalse(FileMagicNumber.AAC.match(bytes), "AAC len=" + len);
			assertFalse(FileMagicNumber.M4A.match(bytes), "M4A len=" + len);
			assertFalse(FileMagicNumber.AMR.match(bytes), "AMR len=" + len);
		}
	}
}
