package cn.hutool.crypto.sign.http;

/**
 * HTTP规范请求。
 *
 * @author mumu
 * @since 5.8.45
 */
public class CanonicalRequest {

	/**
	 * 默认签名版本。
	 */
	public static final String VERSION = "HTTP-SIGN-V1";

	/**
	 * 签名规范版本。
	 */
	private final String version;
	/**
	 * 规范化后的HTTP方法。
	 */
	private final String method;
	/**
	 * 规范化后的URI路径。
	 */
	private final String canonicalUri;
	/**
	 * 规范化后的Query字符串。
	 */
	private final String canonicalQuery;
	/**
	 * 规范化后的Header字符串。
	 */
	private final String canonicalHeaders;
	/**
	 * 请求体SHA-256摘要。
	 */
	private final String bodyHash;

	/**
	 * 构造规范请求。
	 *
	 * @param version          签名规范版本
	 * @param method           HTTP方法
	 * @param canonicalUri     规范化URI路径
	 * @param canonicalQuery   规范化Query字符串
	 * @param canonicalHeaders 规范化Header字符串
	 * @param bodyHash         请求体摘要
	 */
	public CanonicalRequest(final String version, final String method, final String canonicalUri,
							final String canonicalQuery, final String canonicalHeaders, final String bodyHash) {
		this.version = version;
		this.method = method;
		this.canonicalUri = canonicalUri;
		this.canonicalQuery = canonicalQuery;
		this.canonicalHeaders = canonicalHeaders;
		this.bodyHash = bodyHash;
	}

	/**
	 * 获取签名规范版本。
	 *
	 * @return 签名规范版本
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * 获取HTTP方法。
	 *
	 * @return HTTP方法
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * 获取规范化URI路径。
	 *
	 * @return 规范化URI路径
	 */
	public String getCanonicalUri() {
		return canonicalUri;
	}

	/**
	 * 获取规范化Query字符串。
	 *
	 * @return 规范化Query字符串
	 */
	public String getCanonicalQuery() {
		return canonicalQuery;
	}

	/**
	 * 获取规范化Header字符串。
	 *
	 * @return 规范化Header字符串
	 */
	public String getCanonicalHeaders() {
		return canonicalHeaders;
	}

	/**
	 * 获取请求体SHA-256摘要。
	 *
	 * @return 请求体摘要
	 */
	public String getBodyHash() {
		return bodyHash;
	}

	/**
	 * 转为待签名字符串。
	 *
	 * @return 待签名字符串
	 */
	public String toStringToSign() {
		return version + '\n'
			+ method + '\n'
			+ canonicalUri + '\n'
			+ canonicalQuery + '\n'
			+ canonicalHeaders + '\n'
			+ bodyHash;
	}

	@Override
	public String toString() {
		return toStringToSign();
	}
}
