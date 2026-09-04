package cn.hutool.v7.core.codec.binary;


import cn.hutool.v7.core.io.IoUtil;
import cn.hutool.v7.core.io.file.FileUtil;
import cn.hutool.v7.core.text.StrUtil;
import cn.hutool.v7.core.util.ByteUtil;
import cn.hutool.v7.core.util.CharsetUtil;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * Base85工具类，提供Base85(Ascii85)的编码和解码方案<br>
 * 相比Base64节省约7%空间，相比Base62编码/解码速度更快（无需大数运算）
 *
 * @author 红茶
 */
public class Base85 {

	private static final Charset DEFAULT_CHARSET = CharsetUtil.UTF_8;

	// -------------------------------------------------------------------- encode

	/**
	 * Base85编码
	 *
	 * @param source 被编码的字符串
	 * @return 编码后的字符串
	 */
	public static String encode(final CharSequence source) {
		return encode(source, DEFAULT_CHARSET);
	}

	/**
	 * Base85编码
	 *
	 * @param source  被编码的字符串
	 * @param charset 字符集
	 * @return 编码后的字符串
	 */
	public static String encode(final CharSequence source, final Charset charset) {
		return encode(ByteUtil.toBytes(source, charset));
	}

	/**
	 * Base85编码
	 *
	 * @param source 被编码的字节数组
	 * @return 编码后的字符串
	 */
	public static String encode(final byte[] source) {
		return new String(Base85Codec.INSTANCE.encode(source));
	}

	/**
	 * Base85编码
	 *
	 * @param in 被编码的流（一般为图片流或者文件流）
	 * @return 编码后的字符串
	 */
	public static String encode(final InputStream in) {
		return encode(IoUtil.readBytes(in));
	}

	/**
	 * Base85编码
	 *
	 * @param file 被编码的文件
	 * @return 编码后的字符串
	 */
	public static String encode(final File file) {
		return encode(FileUtil.readBytes(file));
	}

	/**
	 * Base85编码（Z85模式，ZeroMQ安全字母表）
	 *
	 * @param source 被编码的字符串
	 * @return 编码后的字符串
	 */
	public static String encodeZ85(final CharSequence source) {
		return encodeZ85(source, DEFAULT_CHARSET);
	}

	/**
	 * Base85编码（Z85模式）
	 *
	 * @param source  被编码的字符串
	 * @param charset 字符集
	 * @return 编码后的字符串
	 */
	public static String encodeZ85(final CharSequence source, final Charset charset) {
		return encodeZ85(ByteUtil.toBytes(source, charset));
	}

	/**
	 * Base85编码（Z85模式）
	 *
	 * @param source 被编码的字节数组
	 * @return 编码后的字符串
	 */
	public static String encodeZ85(final byte[] source) {
		return new String(Base85Codec.INSTANCE.encode(source, true));
	}

	/**
	 * Base85编码（Z85模式）
	 *
	 * @param in 被编码的流
	 * @return 编码后的字符串
	 */
	public static String encodeZ85(final InputStream in) {
		return encodeZ85(IoUtil.readBytes(in));
	}

	/**
	 * Base85编码（Z85模式）
	 *
	 * @param file 被编码的文件
	 * @return 编码后的字符串
	 */
	public static String encodeZ85(final File file) {
		return encodeZ85(FileUtil.readBytes(file));
	}

	// -------------------------------------------------------------------- decode

	/**
	 * Base85解码为字符串
	 *
	 * @param source 被解码的Base85字符串
	 * @return 解码后的字符串
	 */
	public static String decodeStr(final CharSequence source) {
		return decodeStr(source, DEFAULT_CHARSET);
	}

	/**
	 * Base85解码为字符串
	 *
	 * @param source  被解码的Base85字符串
	 * @param charset 字符集
	 * @return 解码后的字符串
	 */
	public static String decodeStr(final CharSequence source, final Charset charset) {
		return StrUtil.str(decode(source), charset);
	}

	/**
	 * Base85解码为字符串（GBK字符集）
	 *
	 * @param source 被解码的Base85字符串
	 * @return 解码后的字符串
	 */
	public static String decodeStrGbk(final CharSequence source) {
		return decodeStr(source, CharsetUtil.GBK);
	}

	/**
	 * Base85解码到文件
	 *
	 * @param base85   被解码的Base85字符串
	 * @param destFile 目标文件
	 * @return 目标文件
	 */
	public static File decodeToFile(final CharSequence base85, final File destFile) {
		return FileUtil.writeBytes(decode(base85), destFile);
	}

	/**
	 * Base85解码到流
	 *
	 * @param base85Str  被解码的Base85字符串
	 * @param out        写出到的流
	 * @param isCloseOut 是否关闭输出流
	 */
	public static void decodeToStream(final CharSequence base85Str, final OutputStream out, final boolean isCloseOut) {
		IoUtil.write(out, isCloseOut, decode(base85Str));
	}

	/**
	 * Base85解码
	 *
	 * @param base85Str 被解码的Base85字符串
	 * @return 解码后的字节数组
	 */
	public static byte[] decode(final CharSequence base85Str) {
		return decode(ByteUtil.toBytes(base85Str, DEFAULT_CHARSET));
	}

	/**
	 * Base85解码
	 *
	 * @param base85Bytes Base85字节数组
	 * @return 解码后的字节数组
	 */
	public static byte[] decode(final byte[] base85Bytes) {
		return Base85Codec.INSTANCE.decode(base85Bytes);
	}

	/**
	 * Base85解码为字符串（Z85模式）
	 *
	 * @param source 被解码的Z85字符串
	 * @return 解码后的字符串
	 */
	public static String decodeStrZ85(final CharSequence source) {
		return decodeStrZ85(source, DEFAULT_CHARSET);
	}

	/**
	 * Base85解码为字符串（Z85模式）
	 *
	 * @param source  被解码的Z85字符串
	 * @param charset 字符集
	 * @return 解码后的字符串
	 */
	public static String decodeStrZ85(final CharSequence source, final Charset charset) {
		return StrUtil.str(decodeZ85(source), charset);
	}

	/**
	 * Base85解码到文件（Z85模式）
	 *
	 * @param base85   被解码的Z85字符串
	 * @param destFile 目标文件
	 * @return 目标文件
	 */
	public static File decodeToFileZ85(final CharSequence base85, final File destFile) {
		return FileUtil.writeBytes(decodeZ85(base85), destFile);
	}

	/**
	 * Base85解码到流（Z85模式）
	 *
	 * @param base85Str  被解码的Z85字符串
	 * @param out        写出到的流
	 * @param isCloseOut 是否关闭输出流
	 */
	public static void decodeToStreamZ85(final CharSequence base85Str, final OutputStream out, final boolean isCloseOut) {
		IoUtil.write(out, isCloseOut, decodeZ85(base85Str));
	}

	/**
	 * Base85解码（Z85模式）
	 *
	 * @param base85Str 被解码的Z85字符串
	 * @return 解码后的字节数组
	 */
	public static byte[] decodeZ85(final CharSequence base85Str) {
		return decodeZ85(ByteUtil.toBytes(base85Str, DEFAULT_CHARSET));
	}

	/**
	 * Base85解码（Z85模式）
	 *
	 * @param base85Bytes Z85字节数组
	 * @return 解码后的字节数组
	 */
	public static byte[] decodeZ85(final byte[] base85Bytes) {
		return Base85Codec.INSTANCE.decode(base85Bytes, true);
	}
}
