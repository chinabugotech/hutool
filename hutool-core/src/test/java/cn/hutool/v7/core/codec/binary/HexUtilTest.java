package cn.hutool.v7.core.codec.binary;

import org.junit.jupiter.api.Test;

import java.awt.Color;

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
		assertEquals("a", HexUtil.format("a"));
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
}
