package cn.hutool.core.io;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Regression test: the AAC, M4A, AMR, MP3 and DOC matchers must return {@code false} for byte arrays too short
 * to contain their magic number, instead of throwing {@link ArrayIndexOutOfBoundsException}.
 */
class FileMagicNumberShortArrayTest {

	/**
	 * OLE2复合文档头，DOC/XLS/PPT共用
	 */
	private static final byte[] OLE_HEADER = {
			(byte) 0xd0, (byte) 0xcf, 0x11, (byte) 0xe0, (byte) 0xa1, (byte) 0xb1, 0x1a, (byte) 0xe1};

	@Test
	void shortArraysReturnFalseInsteadOfThrowing() {
		for (int len = 0; len <= 12; len++) {
			final byte[] bytes = new byte[len];
			assertFalse(FileMagicNumber.AAC.match(bytes), "AAC len=" + len);
			assertFalse(FileMagicNumber.M4A.match(bytes), "M4A len=" + len);
			assertFalse(FileMagicNumber.AMR.match(bytes), "AMR len=" + len);
		}
	}

	@Test
	void shortId3TagReturnFalseInsteadOfThrowing() {
		// MP3的ID3分支读取bytes[2]，只有2字节的"ID"开头时会越界
		assertFalse(FileMagicNumber.MP3.match(new byte[]{0x49, 0x44}), "MP3 len=2");

		// 0字节填充的数组在任意长度下都应返回false，而不是抛出异常
		for (int len = 2; len <= 12; len++) {
			final byte[] bytes = new byte[len];
			bytes[0] = 0x49;
			bytes[1] = 0x44;
			assertFalse(FileMagicNumber.MP3.match(bytes), "MP3 ID len=" + len);
		}

		// 补长度保护后，正常的ID3标签仍应被识别
		assertTrue(FileMagicNumber.MP3.match(new byte[]{0x49, 0x44, 0x33}), "ID3 tag");
	}

	@Test
	void shortOleDocumentReturnFalseInsteadOfThrowing() {
		// DOC会切片bytes[2075,2142)，但此前只要求长度大于515
		for (int len = 516; len <= 2142; len++) {
			final byte[] bytes = new byte[len];
			System.arraycopy(OLE_HEADER, 0, bytes, 0, OLE_HEADER.length);
			assertFalse(FileMagicNumber.DOC.match(bytes), "DOC len=" + len);
		}
	}

	@Test
	void getMagicNumberMustNotThrowOnTruncatedInput() {
		// 公共入口对截断的输入应返回UNKNOWN，而不是抛出越界异常
		assertDoesNotThrow(() -> FileMagicNumber.getMagicNumber(new byte[]{0x49, 0x44}));

		final byte[] ole = new byte[600];
		System.arraycopy(OLE_HEADER, 0, ole, 0, OLE_HEADER.length);
		assertDoesNotThrow(() -> FileMagicNumber.getMagicNumber(ole));
	}
}
