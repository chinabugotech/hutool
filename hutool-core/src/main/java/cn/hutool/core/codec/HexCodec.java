package cn.hutool.core.codec;

import java.util.Arrays;

/**
 * HexCodec 类实现了字节序列与十六进制字符串之间的编码和解码功能。
 * 实现了 Encoder 和 Decoder 接口，提供了十六进制编码和解码的核心功能。
 *
 * @author 红茶
 */
public class HexCodec implements Encoder<byte[], String>,Decoder<CharSequence, byte[]>{

    // 单例模式的 HexCodec 实例
	public static HexCodec INSTANCE = new HexCodec();

    /**
     * 默认编码方法，将字节数组编码为小写十六进制字符串
     * @param data 要编码的字节数组
     * @return 编码后的十六进制字符串
     */
	@Override
	public String encode(byte[] data) {
		return encode(data, false);
	}

	/**
	 * 编码数据
	 *
	 * @param data      数据
	 * @param upperCase 是否使用大写字母表
	 * @return 编码后的十六进制字符串
	 */
	public String encode(byte[] data, boolean upperCase) {
		final HexEncoder encoder = upperCase ? HexEncoder.UPPER_ENCODER : HexEncoder.LOWER_ENCODER;
		return encoder.encode(data);
	}

	@Override
	public byte[] decode(CharSequence encoded) {
		return decode(encoded, false);
	}

	/**
	 * 解码数据
	 *
	 * @param encoded   十六进制字符串
	 * @param upperCase 此参数保留以与encode对称，解码时自动兼容大小写
	 * @return 解码后的内容
	 */
	public byte[] decode(CharSequence encoded, boolean upperCase) {
		// 解码器已内置大小写兼容，upperCase参数仅为API对称性保留
		return HexDecoder.DECODER.decode(encoded);
	}

	public static class HexEncoder implements Encoder<byte[], String>{

		private static final String LOWER_ALPHABET = "0123456789abcdef";
		private static final String UPPER_ALPHABET = "0123456789ABCDEF";

		public static final HexEncoder LOWER_ENCODER = new HexEncoder(LOWER_ALPHABET);
		public static final HexEncoder UPPER_ENCODER = new HexEncoder(UPPER_ALPHABET);

		private final char[] alphabet;

		public HexEncoder(String alphabet) {
			this.alphabet = alphabet.toCharArray();
		}

		@Override
		public String encode(byte[] data) {
			final int len = data.length;
			final char[] out = new char[len << 1];
			for (int i = 0, j = 0; i < len; i++) {
				out[j++] = alphabet[(0xF0 & data[i]) >>> 4];
				out[j++] = alphabet[0x0F & data[i]];
			}
			return new String(out);
		}
	}

	public static class HexDecoder implements Decoder<CharSequence, byte[]> {
		private static final char BASE_CHAR = '0';

		public static final HexDecoder DECODER = new HexDecoder(HexEncoder.LOWER_ALPHABET);

		private final byte[] lookupTable;

		/**
		 * 构造
		 *
		 * @param alphabet 编码字母表（用于构建查找表，解码时自动兼容大小写）
		 */
		public HexDecoder(String alphabet) {
			lookupTable = new byte[128];
			Arrays.fill(lookupTable, (byte) -1);

			final int length = alphabet.length();
			char c;
			for (int i = 0; i < length; i++) {
				c = alphabet.charAt(i);
				lookupTable[c - BASE_CHAR] = (byte) i;
				// 支持大写字母解码
				if (c >= 'a' && c <= 'f') {
					lookupTable[Character.toUpperCase(c) - BASE_CHAR] = (byte) i;
				}
				// 支持小写字母解码（当alphabet为大写时）
				if (c >= 'A' && c <= 'F') {
					lookupTable[Character.toLowerCase(c) - BASE_CHAR] = (byte) i;
				}
			}
		}

		@Override
		public byte[] decode(CharSequence encoded) {
			final String hex = encoded.toString();
			final int strLen = hex.length();

			if ((strLen & 0x01) != 0) {
				throw new IllegalArgumentException("Hex string must have an even length: " + strLen);
			}

			final int byteLen = strLen >> 1;
			final byte[] bytes = new byte[byteLen];

			for (int i = 0, offset = 0; i < strLen; i += 2, offset++) {
				final int high = lookupTable[hex.charAt(i) - BASE_CHAR];
				final int low = lookupTable[hex.charAt(i + 1) - BASE_CHAR];

				if (high < 0 || low < 0) {
					throw new IllegalArgumentException(
						"Invalid hex character in string at position " + i);
				}

				bytes[offset] = (byte) ((high << 4) | low);
			}
			return bytes;
		}
	}

}
