package cn.hutool.v7.crypto.sign.http;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * HTTP签名结果，包含需要写入请求的签名Header、签名值和调试用规范请求信息。
 *
 * @author mumu
 * @since 7.0.0
 */
public class HttpSignResult {

	/**
	 * 待写入HTTP请求的签名Header。
	 */
	private final Map<String, String> headers;
	/**
	 * 编码后的签名值。
	 */
	private final String signature;
	/**
	 * 规范请求。
	 */
	private final CanonicalRequest canonicalRequest;
	/**
	 * 待签名字符串。
	 */
	private final String stringToSign;

	/**
	 * 构造签名结果。
	 *
	 * @param headers          签名Header
	 * @param signature        签名值
	 * @param canonicalRequest 规范请求
	 * @param stringToSign     待签名字符串
	 */
	public HttpSignResult(final Map<String, String> headers, final String signature,
						  final CanonicalRequest canonicalRequest, final String stringToSign) {
		this.headers = new LinkedHashMap<>(headers);
		this.signature = signature;
		this.canonicalRequest = canonicalRequest;
		this.stringToSign = stringToSign;
	}

	/**
	 * 获取待写入HTTP请求的签名Header。
	 *
	 * @return 签名Header
	 */
	public Map<String, String> getHeaders() {
		return Collections.unmodifiableMap(headers);
	}

	/**
	 * 获取签名值。
	 *
	 * @return 签名值
	 */
	public String getSignature() {
		return signature;
	}

	/**
	 * 获取规范请求。
	 *
	 * @return 规范请求
	 */
	public CanonicalRequest getCanonicalRequest() {
		return canonicalRequest;
	}

	/**
	 * 获取待签名字符串。
	 *
	 * @return 待签名字符串
	 */
	public String getStringToSign() {
		return stringToSign;
	}
}
