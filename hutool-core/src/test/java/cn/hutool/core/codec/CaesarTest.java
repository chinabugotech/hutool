package cn.hutool.core.codec;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * {@link Caesar}单元测试
 *
 * @author looly
 */
public class CaesarTest {

	@Test
	public void caesarTest() {
		String str = "1f2e9df6131b480b9fdddc633cf24996";

		String encode = Caesar.encode(str, 3);
		assertEquals("1H2G9FH6131D480D9HFFFE633EH24996", encode);

		String decode = Caesar.decode(encode, 3);
		assertEquals(str, decode);
	}

	@Test
	public void encodeNegativeOffsetTest() {
		// 负偏移量表示反向位移，{@link Caesar#encode}应和{@link Caesar#decode}一样支持
		assertEquals("ZAB", Caesar.encode("abc", -3));
		assertEquals("ZAB", Caesar.decode("abc", 3));
		assertEquals("zTTACKzTcAWN", Caesar.encode("AttackAtDawn", -1));
	}

	@Test
	public void encodeDecodeSymmetryTest() {
		// encode(-k) 与 decode(k) 位移方向一致，结果应完全相同
		final String message = "Hello, Hutool!";
		for (int offset = -60; offset <= 60; offset++) {
			assertEquals(Caesar.decode(message, offset), Caesar.encode(message, -offset),
					"offset=" + offset);
		}
	}

	@Test
	public void roundTripTest() {
		final String message = "AttackAtDawn, Hutool 5.8!";
		for (int offset : new int[]{0, 1, 3, 25, 26, 51, 52, 53, -1, -3, -26, -52, -53, -100}) {
			final String encoded = Caesar.encode(message, offset);
			assertEquals(message, Caesar.decode(encoded, offset), "offset=" + offset);
		}
	}

	@Test
	public void offsetPeriodTest() {
		// 字母表长度为52，偏移量应按52循环
		assertEquals("abc", Caesar.encode("abc", 0));
		assertEquals("abc", Caesar.encode("abc", 52));
		assertEquals("abc", Caesar.encode("abc", -52));
		assertEquals("CDE", Caesar.encode("abc", 3));
		assertEquals("CDE", Caesar.encode("abc", 55));
		assertEquals("ZAB", Caesar.encode("abc", -3));
		assertEquals("ZAB", Caesar.encode("abc", -55));
	}

	@Test
	public void nonCaesarCharKeepTest() {
		// 中文等不在字母表中的字符无法参与位移，应保持原样，而不是被替换成字母表中其它字符
		assertEquals("中文", Caesar.encode("中文", 0));
		assertEquals("中文", Caesar.encode("中文", 3));
		assertEquals("中文", Caesar.decode("中文", 0));
		assertEquals("中文", Caesar.decode("中文", 3));

		// 带音标的拉丁字母同样不在字母表中
		assertEquals("café", Caesar.encode("café", 0));
		assertEquals("dbgé", Caesar.encode("café", 2));
		assertEquals("café", Caesar.decode("dbgé", 2));
	}

	@Test
	public void mixedTextTest() {
		// 仅字母表中的字符发生位移，其它字符（中文、数字、标点、空格）保持原样
		assertEquals("DEF中文GHI", Caesar.encode("abc中文def", 5));
		assertEquals("你好, jXWRRO! 123", Caesar.encode("你好, Hutool! 123", 5));
		assertEquals("你好, Hutool! 123", Caesar.decode("你好, jXWRRO! 123", 5));
	}

	@Test
	public void nonLetterTextTest() {
		// 数字和符号不参与位移
		assertEquals("123!@#", Caesar.encode("123!@#", -9));
		assertEquals("123!@#", Caesar.decode("123!@#", 9));
		assertEquals("", Caesar.encode("", 3));
		assertEquals("", Caesar.decode("", 3));
	}

	@Test
	public void allOffsetTest() {
		// 任意偏移量都不应抛出异常（修复前负偏移量会抛出StringIndexOutOfBoundsException）
		final String message = "The quick brown fox jumps over the lazy dog. 中文测试! 0123456789";
		for (int offset = -520; offset <= 520; offset++) {
			final int currentOffset = offset;
			assertDoesNotThrow(() -> Caesar.encode(message, currentOffset), "offset=" + currentOffset);
			assertDoesNotThrow(() -> Caesar.decode(message, currentOffset), "offset=" + currentOffset);
		}
	}
}
