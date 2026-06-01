package cn.hutool.v7.core.codec.binary;

import cn.hutool.v7.core.util.CharsetUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

public class HexUtilTest {

	@Test
	public void testEncodeColor() {
		final Color red = new Color(255, 0, 0);
		assertEquals("#ff0000", HexUtil.encodeColor(red));

		final Color green = new Color(0, 255, 0);
		assertEquals("#00ff00", HexUtil.encodeColor(green));

		final Color blue = new Color(0, 0, 255);
		assertEquals("#0000ff", HexUtil.encodeColor(blue));

		final Color black = new Color(0, 0, 0);
		assertEquals("#000000", HexUtil.encodeColor(black));

		final Color white = new Color(255, 255, 255);
		assertEquals("#ffffff", HexUtil.encodeColor(white));

		// Test with single digit values (should be padded with 0)
		final Color testColor = new Color(1, 16, 255);
		assertEquals("#0110ff", HexUtil.encodeColor(testColor));
	}

	@Test
	public void testEncodeColorWithPrefix() {
		final Color red = new Color(255, 0, 0);
		assertEquals("0xff0000", HexUtil.encodeColor(red, "0x"));
		assertEquals("#ff0000", HexUtil.encodeColor(red, "#"));
		assertEquals("ff0000", HexUtil.encodeColor(red, ""));
	}

	@Test
	public void testDecodeColor() {
		assertEquals(new Color(255, 0, 0), HexUtil.decodeColor("#ff0000"));
		assertEquals(new Color(0, 255, 0), HexUtil.decodeColor("#00ff00"));
		assertEquals(new Color(0, 0, 255), HexUtil.decodeColor("#0000ff"));
		assertEquals(new Color(255, 0, 0), HexUtil.decodeColor("0xff0000"));
	}

	@Test
	public void testIsHexNumber() {
		assertTrue(HexUtil.isHexNumber("ff"));
		assertTrue(HexUtil.isHexNumber("FF"));
		assertTrue(HexUtil.isHexNumber("0xff"));
		assertTrue(HexUtil.isHexNumber("0XFF"));
		assertTrue(HexUtil.isHexNumber("#ff"));
		assertTrue(HexUtil.isHexNumber("123abc"));
		assertTrue(HexUtil.isHexNumber("0x123abc"));
		assertTrue(HexUtil.isHexNumber("#123abc"));

		assertFalse(HexUtil.isHexNumber(""));
		assertFalse(HexUtil.isHexNumber(null));
		assertFalse(HexUtil.isHexNumber("gg")); // g is not hex digit
		assertFalse(HexUtil.isHexNumber("-ff"));
		assertFalse(HexUtil.isHexNumber("ff-"));
		assertFalse(HexUtil.isHexNumber("12 34")); // space not allowed
	}

	@Test
	public void testToUnicodeHex() {
		assertEquals("\\u4f60", HexUtil.toUnicodeHex('你'));
		assertEquals("\\u0048", HexUtil.toUnicodeHex('H'));
		assertEquals("\\u0065", HexUtil.toUnicodeHex('e'));
		assertEquals("\\u006c", HexUtil.toUnicodeHex('l'));
		assertEquals("\\u006f", HexUtil.toUnicodeHex('o'));

		// Test with integer values
		assertEquals("\\u0041", HexUtil.toUnicodeHex(65)); // 'A'
		assertEquals("\\u0000", HexUtil.toUnicodeHex(0));
		assertEquals("\\uffff", HexUtil.toUnicodeHex(65535)); // max char value
	}

	@Test
	public void testToHexFromInt() {
		assertEquals("ff", HexUtil.toHex(255));
		assertEquals("0", HexUtil.toHex(0));
		assertEquals("10", HexUtil.toHex(16));
		assertEquals("64", HexUtil.toHex(100));
		assertEquals("ffff", HexUtil.toHex(65535));
	}

	@Test
	public void testHexToInt() {
		assertEquals(255, HexUtil.hexToInt("ff"));
		assertEquals(255, HexUtil.hexToInt("0xff"));
		assertEquals(255, HexUtil.hexToInt("#ff"));
		assertEquals(0, HexUtil.hexToInt("0"));
		assertEquals(16, HexUtil.hexToInt("10"));
		assertEquals(100, HexUtil.hexToInt("64"));
		assertEquals(65535, HexUtil.hexToInt("ffff"));
		assertEquals(65535, HexUtil.hexToInt("0xffff"));
	}

	@Test
	public void testToHexFromLong() {
		assertEquals("ff", HexUtil.toHex(255L));
		assertEquals("0", HexUtil.toHex(0L));
		assertEquals("10", HexUtil.toHex(16L));
		assertEquals("ffffffffffffffff", HexUtil.toHex(-1L));
	}

	@Test
	public void testHexToLong() {
		assertEquals(255L, HexUtil.hexToLong("ff"));
		assertEquals(255L, HexUtil.hexToLong("0xff"));
		assertEquals(0L, HexUtil.hexToLong("0"));
		assertEquals(16L, HexUtil.hexToLong("10"));
		assertThrows(NumberFormatException.class, ()-> HexUtil.hexToLong("ffffffffffffffff"));
	}

	@Test
	public void testToHexFromFloat() {
		assertEquals("40490fdb", HexUtil.toHex((float) Math.PI));
		assertEquals("0", HexUtil.toHex(0.0f));
		assertEquals("3f800000", HexUtil.toHex(1.0f));
	}

	@Test
	public void testHexToFloat() {
		assertEquals(Math.PI, HexUtil.hexToFloat("40490fdb"), 0.0001f);
		assertEquals(0.0f, HexUtil.hexToFloat("0"), 0.0001f);
		assertEquals(1.0f, HexUtil.hexToFloat("3f800000"), 0.0001f);
	}

	@Test
	public void testToHexFromDouble() {
		assertEquals("400921fb54442d18", HexUtil.toHex(Math.PI));
		assertEquals("0", HexUtil.toHex(0.0));
		assertEquals("3ff0000000000000", HexUtil.toHex(1.0));
	}

	@Test
	public void testHexToDouble() {
		assertEquals(Math.PI, HexUtil.hexToDouble("400921fb54442d18"), 0.0001);
		assertEquals(0.0, HexUtil.hexToDouble("0"), 0.0001);
		assertEquals(1.0, HexUtil.hexToDouble("3ff0000000000000"), 0.0001);
	}

	@Test
	public void testAppendHex() {
		StringBuilder sb = new StringBuilder();
		HexUtil.appendHex(sb, (byte) 255, true); // lowercase
		assertEquals("ff", sb.toString());

		sb = new StringBuilder();
		HexUtil.appendHex(sb, (byte) 255, false); // uppercase
		assertEquals("FF", sb.toString());

		sb = new StringBuilder();
		HexUtil.appendHex(sb, (byte) 0, true);
		assertEquals("00", sb.toString());

		sb = new StringBuilder();
		HexUtil.appendHex(sb, (byte) 16, true);
		assertEquals("10", sb.toString());
	}

	@Test
	public void testToBigInteger() {
		assertNull(HexUtil.toBigInteger(null));
		assertEquals(new java.math.BigInteger("ff", 16), HexUtil.toBigInteger("ff"));
		assertEquals(new java.math.BigInteger("ff", 16), HexUtil.toBigInteger("0xff"));
		assertEquals(new java.math.BigInteger("ff", 16), HexUtil.toBigInteger("#ff"));
		assertEquals(new java.math.BigInteger("0", 16), HexUtil.toBigInteger("0"));
		assertEquals(new java.math.BigInteger("1234abcd", 16), HexUtil.toBigInteger("1234abcd"));
	}

	@Test
	public void testFormat() {
		assertEquals("", HexUtil.format(""));
		assertEquals("0a", HexUtil.format("a"));
		assertEquals("ab", HexUtil.format("ab"));
		assertEquals("ab cd", HexUtil.format("abcd"));
		assertEquals("ab cd ef", HexUtil.format("abcdef"));
		assertEquals("ab cd ef 12", HexUtil.format("abcdef12"));
	}

	@Test
	public void testFormatWithPrefix() {
		assertEquals("0xab", HexUtil.format("ab", "0x"));
		assertEquals("0xab 0xcd", HexUtil.format("abcd", "0x"));
		assertEquals("#ab", HexUtil.format("ab", "#"));
		assertEquals("#ab #cd", HexUtil.format("abcd", "#"));
	}

	@Test
	public void testFormatWithPrefixAndSeparator() {
		assertEquals("0xab 0xcd", HexUtil.format("abcd", "0x", " "));
		assertEquals("0xab:0xcd", HexUtil.format("abcd", "0x", ":"));
		assertEquals("ab-cd", HexUtil.format("abcd", "", "-"));
		assertEquals("ab cd ef 12", HexUtil.format("abcdef12", null, null));
	}

	@Test
	public void hexStrTest(){
		final String str = "我是一个字符串";

		final String hex = HexUtil.encodeStr(str, CharsetUtil.UTF_8);
		final String decodedStr = HexUtil.decodeStr(hex);

		assertEquals(str, decodedStr);
	}

	@Test
	public void issueI50MI6Test(){
		final String s = HexUtil.encodeStr("烟".getBytes(StandardCharsets.UTF_16BE));
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
	public void isHexNumberTestForEmpty() {
		assertFalse(HexUtil.isHexNumber(""));
		assertFalse(HexUtil.isHexNumber(null));
	}

	@Test
	public void decodeTest(){
		final String str = "e8c670380cb220095268f40221fc748fa6ac39d6e930e63c30da68bad97f885d";
		Assertions.assertArrayEquals(HexUtil.decode(str),
			HexUtil.decode(str.toUpperCase()));
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
		final String s = HexUtil.encodeStr("6");
		final String s1 = HexUtil.decodeStr(s);
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
		final String result = HexUtil.format("");
		assertEquals("", result);
	}

	@Test
	public void testFormatSingleChar() {
		final String result = HexUtil.format("1");
		assertEquals("01", result);
	}

	@Test
	public void testFormatOddLength() {
		final String result = HexUtil.format("123");
		assertEquals("01 23", result);
	}

	@Test
	public void testFormatWithPrefixSingleChar() {
		final String result = HexUtil.format("1", "0x");
		assertEquals("0x01", result);
	}

	@Test
	public void testFormatWithPrefixOddLength() {
		final String result = HexUtil.format("123", "0x");
		assertEquals("0x01 0x23", result);
	}

	@Test
	public void hexToFloatTest() {
		//测试正常浮点数值
		final float value1 = 1.5f;
		final String hex1 = HexUtil.toHex(value1);
		assertEquals(value1, HexUtil.hexToFloat(hex1));

		//测试负数
		final float value2 = -1.5f;
		final String hex2 = HexUtil.toHex(value2);
		assertEquals(value2, HexUtil.hexToFloat(hex2));

		//测试科学计数法值
		final float value3 = 1.23456789e-5f;
		final String hex3 = HexUtil.toHex(value3);
		assertEquals(value3, HexUtil.hexToFloat(hex3));

		//测试十六进制前缀
		assertEquals(1.5f, HexUtil.hexToFloat("0x3fc00000"));
		assertEquals(1.5f, HexUtil.hexToFloat("#3fc00000"));
	}

	@Test
	public void hexToDoubleTest() {
		//测试正常双精度浮点数值
		final double value1 = 1.5;
		final String hex1 = HexUtil.toHex(value1);
		assertEquals(value1, HexUtil.hexToDouble(hex1));

		//测试负数
		final double value3 = -1.5;
		final String hex3 = HexUtil.toHex(value3);
		assertEquals(value3, HexUtil.hexToDouble(hex3));

		//测试高精度数值
		final double value4 = Math.PI;
		final String hex4 = HexUtil.toHex(value4);
		assertEquals(value4, HexUtil.hexToDouble(hex4));

		//测试科学计数法值
		final double value5 = 1.23456789012345e-10;
		final String hex5 = HexUtil.toHex(value5);
		assertEquals(value5, HexUtil.hexToDouble(hex5));

		//测试十六进制前缀
		assertEquals(1.5, HexUtil.hexToDouble("0x3ff8000000000000"));
		assertEquals(1.5, HexUtil.hexToDouble("#3ff8000000000000"));
	}
}
