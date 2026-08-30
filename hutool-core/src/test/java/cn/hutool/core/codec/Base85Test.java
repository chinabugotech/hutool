package cn.hutool.core.codec;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Base85单元测试
 *
 * @author 红茶
 */
public class Base85Test {

	private static final String TEST_STR = "伦家是一个非常长的字符串66";
	private File tempFile;

	@AfterEach
	public void cleanup() {
		if (tempFile != null && tempFile.exists()) {
			FileUtil.del(tempFile);
		}
	}

	// ==================== 标准模式 - 固定值往返 ====================

	@Test
	public void encodeAndDecodeTest() {
		String encode = Base85.encode(TEST_STR);
		assertNotNull(encode);
		assertFalse(encode.isEmpty());

		String decodeStr = Base85.decodeStr(encode);
		assertEquals(TEST_STR, decodeStr);
	}

	// ==================== Z85模式 - 固定值往返 ====================

	@Test
	public void encodeAndDecodeZ85Test() {
		String encode = Base85.encodeZ85(TEST_STR);
		assertNotNull(encode);
		assertFalse(encode.isEmpty());

		String decodeStr = Base85.decodeStrZ85(encode);
		assertEquals(TEST_STR, decodeStr);
	}

	// ==================== 标准模式 - 随机字符串往返 ====================

	@Test
	public void encodeAndDecodeRandomTest() {
		String a = RandomUtil.randomString(RandomUtil.randomInt(1000));
		String encode = Base85.encode(a);
		String decodeStr = Base85.decodeStr(encode);
		assertEquals(a, decodeStr);
	}

	// ==================== Z85模式 - 随机字符串往返 ====================

	@Test
	public void encodeAndDecodeZ85RandomTest() {
		String a = RandomUtil.randomString(RandomUtil.randomInt(1000));
		String encode = Base85.encodeZ85(a);
		String decodeStr = Base85.decodeStrZ85(encode);
		assertEquals(a, decodeStr);
	}

	// ==================== 字符集相关 ====================

	@Test
	public void encodeWithCharsetTest() {
		String encode = Base85.encode(TEST_STR, CharsetUtil.CHARSET_GBK);
		String decodeStr = Base85.decodeStr(encode, CharsetUtil.CHARSET_GBK);
		assertEquals(TEST_STR, decodeStr);
	}

	@Test
	public void encodeZ85WithCharsetTest() {
		String encode = Base85.encodeZ85(TEST_STR, CharsetUtil.CHARSET_GBK);
		String decodeStr = Base85.decodeStrZ85(encode, CharsetUtil.CHARSET_GBK);
		assertEquals(TEST_STR, decodeStr);
	}

	@Test
	public void decodeStrGbkTest() {
		// 先用GBK编码，再用GBK解码
		byte[] gbkBytes = TEST_STR.getBytes(CharsetUtil.CHARSET_GBK);
		String encoded = Base85.encode(gbkBytes);
		String decoded = Base85.decodeStrGbk(encoded);
		assertEquals(TEST_STR, decoded);
	}

	// ==================== byte[] 直接编解码 ====================

	@Test
	public void encodeAndDecodeBytesTest() {
		byte[] source = TEST_STR.getBytes(StandardCharsets.UTF_8);
		byte[] encoded = Base85.encode(source).getBytes(StandardCharsets.UTF_8);
		byte[] decoded = Base85.decode(encoded);
		assertArrayEquals(source, decoded);
	}

	@Test
	public void encodeAndDecodeZ85BytesTest() {
		byte[] source = TEST_STR.getBytes(StandardCharsets.UTF_8);
		byte[] encoded = Base85.encodeZ85(source).getBytes(StandardCharsets.UTF_8);
		byte[] decoded = Base85.decodeZ85(encoded);
		assertArrayEquals(source, decoded);
	}

	// ==================== InputStream 编码 ====================

	@Test
	public void encodeInputStreamTest() {
		byte[] source = TEST_STR.getBytes(StandardCharsets.UTF_8);
		ByteArrayInputStream in = new ByteArrayInputStream(source);

		String encoded = Base85.encode(in);
		String decoded = Base85.decodeStr(encoded);
		assertEquals(TEST_STR, decoded);
	}

	@Test
	public void encodeZ85InputStreamTest() {
		byte[] source = TEST_STR.getBytes(StandardCharsets.UTF_8);
		ByteArrayInputStream in = new ByteArrayInputStream(source);

		String encoded = Base85.encodeZ85(in);
		String decoded = Base85.decodeStrZ85(encoded);
		assertEquals(TEST_STR, decoded);
	}

	// ==================== File 编码 ====================

	@Test
	public void encodeFileTest() {
		tempFile = FileUtil.createTempFile();
		FileUtil.writeUtf8String(TEST_STR, tempFile);

		String encoded = Base85.encode(tempFile);
		String decoded = Base85.decodeStr(encoded);
		assertEquals(TEST_STR, decoded);
	}

	@Test
	public void encodeZ85FileTest() {
		tempFile = FileUtil.createTempFile();
		FileUtil.writeUtf8String(TEST_STR, tempFile);

		String encoded = Base85.encodeZ85(tempFile);
		String decoded = Base85.decodeStrZ85(encoded);
		assertEquals(TEST_STR, decoded);
	}

	// ==================== decodeToFile ====================

	@Test
	public void decodeToFileTest() {
		String encoded = Base85.encode(TEST_STR);
		tempFile = FileUtil.createTempFile();

		File result = Base85.decodeToFile(encoded, tempFile);
		assertTrue(result.exists());
		assertEquals(TEST_STR, FileUtil.readUtf8String(result));
	}

	@Test
	public void decodeToFileZ85Test() {
		String encoded = Base85.encodeZ85(TEST_STR);
		tempFile = FileUtil.createTempFile();

		File result = Base85.decodeToFileZ85(encoded, tempFile);
		assertTrue(result.exists());
		assertEquals(TEST_STR, FileUtil.readUtf8String(result));
	}

	// ==================== decodeToStream ====================

	@Test
	public void decodeToStreamTest() {
		String encoded = Base85.encode(TEST_STR);
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		Base85.decodeToStream(encoded, out, false);
		assertEquals(TEST_STR, StrUtil.str(out.toByteArray(), StandardCharsets.UTF_8));
	}

	@Test
	public void decodeToStreamZ85Test() {
		String encoded = Base85.encodeZ85(TEST_STR);
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		Base85.decodeToStreamZ85(encoded, out, false);
		assertEquals(TEST_STR, StrUtil.str(out.toByteArray(), StandardCharsets.UTF_8));
	}

	// ==================== 边界情况 ====================

	@Test
	public void encodeEmptyTest() {
		assertEquals("", Base85.encode(""));
		assertEquals("", Base85.encode(new byte[0]));
	}

	@Test
	public void decodeEmptyTest() {
		assertArrayEquals(new byte[0], Base85.decode(""));
		assertArrayEquals(new byte[0], Base85.decode(new byte[0]));
	}

	@Test
	public void encodeZ85EmptyTest() {
		assertEquals("", Base85.encodeZ85(""));
		assertEquals("", Base85.encodeZ85(new byte[0]));
	}

	@Test
	public void decodeZ85EmptyTest() {
		assertArrayEquals(new byte[0], Base85.decodeZ85(""));
		assertArrayEquals(new byte[0], Base85.decodeZ85(new byte[0]));
	}

	@Test
	public void encodeSingleByteTest() {
		// 1字节 → 不足4字节的尾部处理
		byte[] single = {0x42};
		String encoded = Base85.encode(single);
		assertArrayEquals(single, Base85.decode(encoded));
	}

	@Test
	public void encodeTwoBytesTest() {
		// 2字节 → 尾部处理
		byte[] two = {0x42, 0x43};
		String encoded = Base85.encode(two);
		assertArrayEquals(two, Base85.decode(encoded));
	}

	@Test
	public void encodeThreeBytesTest() {
		// 3字节 → 尾部处理
		byte[] three = {0x42, 0x43, 0x44};
		String encoded = Base85.encode(three);
		assertArrayEquals(three, Base85.decode(encoded));
	}

	@Test
	public void encodeExactFourBytesTest() {
		// 恰好4字节 → 完整块，无尾部
		byte[] four = {0x42, 0x43, 0x44, 0x45};
		String encoded = Base85.encode(four);
		assertArrayEquals(four, Base85.decode(encoded));
	}

	@Test
	public void encodeFiveBytesTest() {
		// 5字节 → 1个完整块 + 1字节尾部
		byte[] five = {0x42, 0x43, 0x44, 0x45, 0x46};
		String encoded = Base85.encode(five);
		assertArrayEquals(five, Base85.decode(encoded));
	}

	// ==================== Codec层直接调用 ====================

	@Test
	public void codecStandardEncodeDecodeTest() {
		byte[] source = TEST_STR.getBytes(StandardCharsets.UTF_8);
		byte[] encoded = Base85Codec.INSTANCE.encode(source);
		byte[] decoded = Base85Codec.INSTANCE.decode(encoded);
		assertArrayEquals(source, decoded);
	}

	@Test
	public void codecZ85EncodeDecodeTest() {
		byte[] source = TEST_STR.getBytes(StandardCharsets.UTF_8);
		byte[] encoded = Base85Codec.INSTANCE.encode(source, true);
		byte[] decoded = Base85Codec.INSTANCE.decode(encoded, true);
		assertArrayEquals(source, decoded);
	}

	// ==================== 非法输入校验 ====================

	@Test
	public void decodeInvalidCharacterThrowsTest() {
		// 标准模式中 'z'(122) 超出字母表范围 '!'(33)~'u'(117)
		assertThrows(IllegalArgumentException.class, () ->
			Base85.decode("zzzzz")
		);
	}

	// ==================== 两种模式结果不同 ====================

	@Test
	public void standardAndZ85ProduceDifferentResultTest() {
		String standard = Base85.encode(TEST_STR);
		String z85 = Base85.encodeZ85(TEST_STR);
		assertNotEquals(standard, z85);
	}

	// ==================== 大量数据压力往返 ====================

	@Test
	public void largeDataRoundTripTest() {
		byte[] large = RandomUtil.randomBytes(100_000);
		String encoded = Base85.encode(large);
		byte[] decoded = Base85.decode(encoded);
		assertArrayEquals(large, decoded);
	}

	@Test
	public void largeDataZ85RoundTripTest() {
		byte[] large = RandomUtil.randomBytes(100_000);
		String encoded = Base85.encodeZ85(large);
		byte[] decoded = Base85.decodeZ85(encoded);
		assertArrayEquals(large, decoded);
	}
}
