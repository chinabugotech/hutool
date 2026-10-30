package cn.hutool.core.codec;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class HashidsTest {
	@Test
	public void hexEncodeDecode() {
		final Hashids hashids = Hashids.create("my awesome salt".toCharArray());
		final String encoded1 = hashids.encodeFromHex("507f1f77bcf86cd799439011");
		final String encoded2 = hashids.encodeFromHex("0x507f1f77bcf86cd799439011");
		final String encoded3 = hashids.encodeFromHex("0X507f1f77bcf86cd799439011");

		assertEquals("R2qnd2vkOJTXm7XV7yq4", encoded1);
		assertEquals(encoded1, encoded2);
		assertEquals(encoded1, encoded3);
		final String decoded = hashids.decodeToHex(encoded1);
		assertEquals("507f1f77bcf86cd799439011", decoded);
	}

	/**
	 * 测试自定义字母表下分隔符阈值的判断。
	 *
	 * <p>该字母表共 19 个字符，其中 c/f/h/i 是默认分隔符，过滤后 tmpAlphabet 为 15 个字符，
	 * 真实比值 15 / 4 = 3.75，大于阈值 3.5，应当补 1 个分隔符后再编码。
	 * 若按 int 整除计算得到 3，会跳过补齐分支，导致编码结果错误。</p>
	 */
	@Test
	public void separatorThresholdTest() {
		// 空盐，避免 shuffle 影响断言
		final char[] alphabet = "cfhiabdegjklmnopqrv".toCharArray();
		final Hashids hashids = Hashids.create(new char[0], alphabet, -1);

		assertEquals("gr", hashids.encode(1));
		assertEquals("njfrhe", hashids.encode(1, 2, 3));
	}
}
