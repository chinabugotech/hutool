package cn.hutool.core.util;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

/**
 * HexUtil单元测试
 * @author Looly
 *
 */
public class HexUtilTest {

	@Test
	public void hexStrTest(){
		final String str = "我是一个字符串";

		final String hex = HexUtil.encodeHexStr(str, CharsetUtil.CHARSET_UTF_8);
		final String decodedStr = HexUtil.decodeHexStr(hex);

		assertEquals(str, decodedStr);
	}

	@Test
	public void issueI50MI6Test(){
		final String s = HexUtil.encodeHexStr("烟".getBytes(StandardCharsets.UTF_16BE));
		assertEquals("70df", s);
	}

	@Test
	public void toUnicodeHexTest() {
		String unicodeHex = HexUtil.toUnicodeHex('\u2001');
		assertEquals("\\u2001", unicodeHex);

		unicodeHex = HexUtil.toUnicodeHex('你');
		assertEquals("\\u4f60", unicodeHex);
	}

	@Test
	public void isHexNumberTest() {
		assertTrue(HexUtil.isHexNumber("0"));
		assertTrue(HexUtil.isHexNumber("002c"));

		String a = "0x3544534F444";
		assertTrue(HexUtil.isHexNumber(a));

		// https://gitee.com/chinabugotech/hutool/issues/I62H7K
		a = "0x0000000000000001158e460913d00000";
		assertTrue(HexUtil.isHexNumber(a));

		// 错误的
		a = "0x0000001000T00001158e460913d00000";
		assertFalse(HexUtil.isHexNumber(a));

		// 错误的,https://github.com/chinabugotech/hutool/issues/2857
		a = "-1";
		assertFalse(HexUtil.isHexNumber(a));
	}

	@Test
	public void isHexNumberTest2() {
		assertFalse(HexUtil.isHexNumber(""));
		assertFalse(HexUtil.isHexNumber(null));
	}

	@Test
	public void decodeTest(){
		final String str = "e8c670380cb220095268f40221fc748fa6ac39d6e930e63c30da68bad97f885d";
		assertArrayEquals(HexUtil.decodeHex(str),
				HexUtil.decodeHex(str.toUpperCase()));
	}

	@Test
	public void formatHexTest(){
		final String hex = "e8c670380cb220095268f40221fc748fa6ac39d6e930e63c30da68bad97f885d";
		final String formatHex = HexUtil.format(hex);
		assertEquals("e8 c6 70 38 0c b2 20 09 52 68 f4 02 21 fc 74 8f a6 ac 39 d6 e9 30 e6 3c 30 da 68 ba d9 7f 88 5d", formatHex);
	}

	@Test
	public void formatHexTest2(){
		final String hex = "e8c670380cb220095268f40221fc748fa6";
		final String formatHex = HexUtil.format(hex, "0x");
		assertEquals("0xe8 0xc6 0x70 0x38 0x0c 0xb2 0x20 0x09 0x52 0x68 0xf4 0x02 0x21 0xfc 0x74 0x8f 0xa6", formatHex);
	}

	@Test
	public void decodeHexTest(){
		final String s = HexUtil.encodeHexStr("6");
		final String s1 = HexUtil.decodeHexStr(s);
		assertEquals("6", s1);
	}

	@Test
	public void hexToIntTest() {
		final String hex1 = "FF";
		assertEquals(255, HexUtil.hexToInt(hex1));
		final String hex2 = "0xFF";
		assertEquals(255, HexUtil.hexToInt(hex2));
		final String hex3 = "#FF";
		assertEquals(255, HexUtil.hexToInt(hex3));
	}

	@Test
	public void hexToLongTest() {
		final String hex1 = "FF";
		assertEquals(255L, HexUtil.hexToLong(hex1));
		final String hex2 = "0xFF";
		assertEquals(255L, HexUtil.hexToLong(hex2));
		final String hex3 = "#FF";
		assertEquals(255L, HexUtil.hexToLong(hex3));
	}

	@Test
	public void hexToFloatTest() {
		//测试正常浮点数值
		float value1 = 1.5f;
		String hex1 = HexUtil.toHex(value1);
		assertEquals(value1, HexUtil.hexToFloat(hex1));

		//测试负数
		float value2 = -1.5f;
		String hex2 = HexUtil.toHex(value2);
		assertEquals(value2, HexUtil.hexToFloat(hex2));

		//测试科学计数法值
		float value3 = 1.23456789e-5f;
		String hex3 = HexUtil.toHex(value3);
		assertEquals(value3, HexUtil.hexToFloat(hex3));

		//测试十六进制前缀
		assertEquals(1.5f, HexUtil.hexToFloat("0x3fc00000"));
		assertEquals(1.5f, HexUtil.hexToFloat("#3fc00000"));
	}

	@Test
	public void hexToDoubleTest() {
		//测试正常双精度浮点数值
		double value1 = 1.5;
		String hex1 = HexUtil.toHex(value1);
		assertEquals(value1, HexUtil.hexToDouble(hex1));

		//测试负数
		double value3 = -1.5;
		String hex3 = HexUtil.toHex(value3);
		assertEquals(value3, HexUtil.hexToDouble(hex3));

		//测试高精度数值
		double value4 = Math.PI;
		String hex4 = HexUtil.toHex(value4);
		assertEquals(value4, HexUtil.hexToDouble(hex4));

		//测试科学计数法值
		double value5 = 1.23456789012345e-10;
		String hex5 = HexUtil.toHex(value5);
		assertEquals(value5, HexUtil.hexToDouble(hex5));

		//测试十六进制前缀
		assertEquals(1.5, HexUtil.hexToDouble("0x3ff8000000000000"));
		assertEquals(1.5, HexUtil.hexToDouble("#3ff8000000000000"));
	}

	@Test
	public void toBigIntegerTest() {
		final String hex1 = "FF";
		assertEquals(new BigInteger("FF", 16), HexUtil.toBigInteger(hex1));
		final String hex2 = "0xFF";
		assertEquals(new BigInteger("FF", 16), HexUtil.toBigInteger(hex2));
		final String hex3 = "#FF";
		assertEquals(new BigInteger("FF", 16), HexUtil.toBigInteger(hex3));
	}

	@Test
	public void testFormatEmpty() {
		String result = HexUtil.format("");
		assertEquals("", result);
	}

	@Test
	public void testFormatSingleChar() {
		String result = HexUtil.format("1");
		assertEquals("1", result);
	}

	@Test
	public void testFormatOddLength() {
		String result = HexUtil.format("123");
		assertEquals("12 3", result);
	}

	@Test
	public void testFormatWithPrefixSingleChar() {
		String result = HexUtil.format("1", "0x");
		assertEquals("0x1", result);
	}

	@Test
	public void testFormatWithPrefixOddLength() {
		String result = HexUtil.format("123", "0x");
		assertEquals("0x12 0x3", result);
	}

}
