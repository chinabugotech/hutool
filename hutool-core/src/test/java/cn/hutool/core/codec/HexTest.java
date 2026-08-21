package cn.hutool.core.codec;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Hex编解码单元测试
 *
 * @author 红茶
 */
public class HexTest {

	@Test
	public void encodeTest() {
		final String src = "Hello World";
		final byte[] bytes = src.getBytes();

		// 默认小写编码
		final String encoded = Hex.encode(bytes);
		assertEquals("48656c6c6f20576f726c64", encoded);

		// 大写编码
		final String encodedUpper = Hex.encode(bytes, true);
		assertEquals("48656C6C6F20576F726C64", encodedUpper);
	}

	/**
	 * 解码测试
	 *
	 */
	@Test
	public void decodeTest() {
		final String hex = "48656c6c6f20576f726c64";
		final byte[] decoded = Hex.decode(hex);
		assertArrayEquals("Hello World".getBytes(), decoded);

		// 大写输入也应正常解码
		final byte[] decodedUpper = Hex.decode("48656C6C6F20576F726C64");
		assertArrayEquals("Hello World".getBytes(), decodedUpper);

		// 大小写混合输入
		final byte[] decodedMixed = Hex.decode("48656C6c6F20576f726C64");
		assertArrayEquals("Hello World".getBytes(), decodedMixed);
	}

	/**
	 * 解码STR测试
	 *
	 */
	@Test
	public void decodeStrTest() {
		final String result = Hex.decodeStr("48656c6c6f20576f726c64");
		assertEquals("Hello World", result);
	}

	/**
	 * 编码字符串测试
	 *
	 */
	@Test
	public void encodeStringTest() {
		final String encoded = Hex.encode("Hello World");
		assertEquals("48656c6c6f20576f726c64", encoded);

		final String encodedUpper = Hex.encode("Hello World", true);
		assertEquals("48656C6C6F20576F726C64", encodedUpper);
	}

	/**
	 * 编码空测试
	 *
	 */
	@Test
	public void encodeEmptyTest() {
		assertEquals("", Hex.encode(new byte[0]));
		assertEquals("", Hex.encode(""));
	}

	/**
	 * 解码空测试
	 *
	 */
	@Test
	public void decodeEmptyTest() {
		assertArrayEquals(new byte[0], Hex.decode(""));
	}

	/**
	 * 编码单字节测试
	 *
	 */
	@Test
	public void encodeSingleByteTest() {
		// 单字节边界值
		assertEquals("00", Hex.encode(new byte[]{0x00}));
		assertEquals("ff", Hex.encode(new byte[]{(byte) 0xFF}));
		assertEquals("80", Hex.encode(new byte[]{(byte) 0x80}));
	}

	/**
	 * 编解码器往返测试
	 *
	 */
	@Test
	public void codecRoundTripTest() {
		// 通过Codec直接调用验证往返一致性
		final byte[] original = "Hutool Hex Codec Round Trip Test!@#$%^&*()".getBytes();
		final String encoded = HexCodec.INSTANCE.encode(original);
		final byte[] decoded = HexCodec.INSTANCE.decode(encoded);
		assertArrayEquals(original, decoded);
	}

	/**
	 * 编码器大写往返测试
	 *
	 */
	@Test
	public void encoderUpperCaseRoundTripTest() {
		final byte[] original = "Upper Case Encoder Test".getBytes();
		final String encoded = HexCodec.HexEncoder.UPPER_ENCODER.encode(original);
		assertTrue(encoded.equals(encoded.toUpperCase()));

		// 大写编码结果仍可被解码器正确还原
		final byte[] decoded = HexCodec.HexDecoder.DECODER.decode(encoded);
		assertArrayEquals(original, decoded);
	}

	/**
	 * 解码奇数长度测试
	 *
	 */
	@Test
	public void decodeOddLengthTest() {
		// 奇数长度应抛出异常
		assertThrows(IllegalArgumentException.class, () -> Hex.decode("abc"));
	}

	/**
	 * 解码无效字符测试
	 *
	 */
	@Test
	public void decodeInvalidCharTest() {
		// 非法十六进制字符应抛出异常
		assertThrows(IllegalArgumentException.class, () -> Hex.decode("ggzz"));
	}

	/**
	 * 十六进制数测试
	 *
	 */
	@Test
	public void isHexNumberTest() {
		assertTrue(Hex.isHexNumber("48656c6c6f"));
		assertTrue(Hex.isHexNumber("ABCDEF"));
		assertTrue(Hex.isHexNumber("0123456789abcdefABCDEF"));

		assertFalse(Hex.isHexNumber(null));
		assertFalse(Hex.isHexNumber(""));
		assertFalse(Hex.isHexNumber("xyz"));
		assertFalse(Hex.isHexNumber("48656c6c6")); // 奇数长度
		assertFalse(Hex.isHexNumber("ghij"));
	}

	/**
	 * 编码十六进制STR别名测试
	 *
	 */
	@Test
	public void encodeHexStrAliasTest() {
		// 验证别名方法与主方法结果一致
		final byte[] data = "alias test".getBytes();
		assertEquals(Hex.encode(data), Hex.encodeHexStr(data));
		assertEquals(Hex.encode(data, true), Hex.encodeHexStr(data, true));
		assertEquals(Hex.encode("alias"), Hex.encodeHexStr("alias"));
		assertEquals(Hex.encode("alias", true), Hex.encodeHexStr("alias", true));
	}
}
