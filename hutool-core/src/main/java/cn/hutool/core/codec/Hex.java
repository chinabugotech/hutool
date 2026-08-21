package cn.hutool.core.codec;

/**
 * Hex类提供十六进制编码和解码的工具方法
 * 可以将字节数组或字符串编码为十六进制字符串，也可以将十六进制字符串解码为字节数组或字符串
 *
 * @author 红茶
 */
public class Hex {

	/**
	 * 编码为小写十六进制字符串
	 * @param data 数据
	 * @return 编码后的十六进制字符串
	 */
	public static String encode(byte[] data) {
		return HexCodec.INSTANCE.encode(data);
	}

	/**
	 * 编码为十六进制字符串
	 *
	 * @param data      数据
	 * @param upperCase 是否使用大写字母
	 * @return 编码后的十六进制字符串
	 */
	public static String encode(byte[] data, boolean upperCase) {
		return HexCodec.INSTANCE.encode(data, upperCase);
	}

	/**
	 * 编码为小写十六进制字符串
	 *
	 * @param data 字符串数据
	 * @return 编码后的十六进制字符串
	 */
	public static String encode(String data) {
		return encode(data.getBytes());
	}

	/**
	 * 编码为十六进制字符串
	 *
	 * @param data      字符串数据
	 * @param upperCase 是否使用大写字母
	 * @return 编码后的十六进制字符串
	 */
	public static String encode(String data, boolean upperCase) {
		return encode(data.getBytes(), upperCase);
	}

	/**
	 * 解码十六进制字符串
	 *
	 * @param encoded 十六进制字符串
	 * @return 解码后的byte[]
	 */
	public static byte[] decode(String encoded) {
		return HexCodec.INSTANCE.decode(encoded);
	}

	/**
	 * 解码十六进制字符串为字符串
	 *
	 * @param encoded 十六进制字符串
	 * @return 解码后的字符串
	 */
	public static String decodeStr(String encoded) {
		return new String(decode(encoded));
	}

	/**
	 * 将byte[]转为十六进制字符串（小写）
	 *
	 * @param data byte[]
	 * @return 十六进制字符串
	 */
	public static String encodeHexStr(byte[] data) {
		return encode(data);
	}

	/**
	 * 将byte[]转为十六进制字符串
	 *
	 * @param data      byte[]
	 * @param upperCase 是否使用大写字母
	 * @return 十六进制字符串
	 */
	public static String encodeHexStr(byte[] data, boolean upperCase) {
		return encode(data, upperCase);
	}

	/**
	 * 将字符串转为十六进制字符串（小写）
	 *
	 * @param data 字符串
	 * @return 十六进制字符串
	 */
	public static String encodeHexStr(String data) {
		return encode(data);
	}

	/**
	 * 将字符串转为十六进制字符串
	 *
	 * @param data      字符串
	 * @param upperCase 是否使用大写字母
	 * @return 十六进制字符串
	 */
	public static String encodeHexStr(String data, boolean upperCase) {
		return encode(data, upperCase);
	}

	/**
	 * 判断是否为合法的十六进制字符串
	 *
	 * @param str 待检测的字符串
	 * @return 是否为合法的十六进制字符串
	 */
	public static boolean isHexNumber(String str) {
		if (str == null || str.isEmpty() || (str.length() & 0x01) != 0) {
			return false;
		}
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (!((c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F'))) {
				return false;
			}
		}
		return true;
	}
}
