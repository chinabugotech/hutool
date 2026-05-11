package cn.hutool.v7.crypto.sign.http;

import cn.hutool.v7.core.lang.Assert;
import cn.hutool.v7.core.text.StrUtil;

import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

/**
 * HTTP签名Header名称配置，用于控制签名字段在HTTP Header中的具体名称。
 * <p>
 * 此对象为可变配置对象，建议在构造签名器或验签器前完成配置，不要在并发签名过程中继续修改。
 *
 * @author mumu
 * @since 7.0.0
 */
public class SignHeaderNames {

	/**
	 * 访问身份ID Header名称。
	 */
	private String accessKeyIdHeaderName = "X-Access-Key-Id";
	/**
	 * Unix秒级时间戳 Header名称。
	 */
	private String timestampHeaderName = "X-Sign-Timestamp";
	/**
	 * 防重放随机串 Header名称。
	 */
	private String nonceHeaderName = "X-Sign-Nonce";
	/**
	 * 签名算法 Header名称。
	 */
	private String algorithmHeaderName = "X-Sign-Algorithm";
	/**
	 * 签名结果 Header名称。
	 */
	private String signatureHeaderName = "X-Signature";

	/**
	 * 创建默认Header名称。
	 *
	 * @return Header名称配置
	 */
	public static SignHeaderNames create() {
		return new SignHeaderNames();
	}

	/**
	 * 创建默认Header名称。
	 *
	 * @return Header名称配置
	 */
	public static SignHeaderNames defaultHeaders() {
		return create();
	}

	/**
	 * 创建自定义Header名称。
	 *
	 * @param accessKeyIdHeaderName 访问身份ID Header名称
	 * @param timestampHeaderName   Unix秒级时间戳 Header名称
	 * @param nonceHeaderName       防重放随机串 Header名称
	 * @param algorithmHeaderName   签名算法 Header名称
	 * @param signatureHeaderName   签名结果 Header名称
	 * @return Header名称配置
	 */
	public static SignHeaderNames of(final String accessKeyIdHeaderName, final String timestampHeaderName,
									 final String nonceHeaderName, final String algorithmHeaderName,
									 final String signatureHeaderName) {
		return create()
			.setAccessKeyIdHeaderName(accessKeyIdHeaderName)
			.setTimestampHeaderName(timestampHeaderName)
			.setNonceHeaderName(nonceHeaderName)
			.setAlgorithmHeaderName(algorithmHeaderName)
			.setSignatureHeaderName(signatureHeaderName);
	}

	/**
	 * 创建OpenAPI风格Header名称。
	 *
	 * @return Header名称配置
	 */
	public static SignHeaderNames openapi() {
		return of(
			"X-Openapi-App-Id",
			"X-Openapi-Timestamp",
			"X-Openapi-Nonce",
			"X-Openapi-Algorithm",
			"X-Openapi-Signature"
		);
	}

	/**
	 * 复制Header名称配置。
	 *
	 * @return Header名称配置副本
	 */
	public SignHeaderNames copy() {
		return create()
			.setAccessKeyIdHeaderName(this.accessKeyIdHeaderName)
			.setTimestampHeaderName(this.timestampHeaderName)
			.setNonceHeaderName(this.nonceHeaderName)
			.setAlgorithmHeaderName(this.algorithmHeaderName)
			.setSignatureHeaderName(this.signatureHeaderName);
	}

	/**
	 * 获取访问身份ID Header名称。
	 *
	 * @return Header名称
	 */
	public String getAccessKeyIdHeaderName() {
		return accessKeyIdHeaderName;
	}

	/**
	 * 设置访问身份ID Header名称。
	 *
	 * @param accessKeyIdHeaderName Header名称
	 * @return this
	 */
	public SignHeaderNames setAccessKeyIdHeaderName(final String accessKeyIdHeaderName) {
		this.accessKeyIdHeaderName = normalizeHeaderName(accessKeyIdHeaderName);
		return this;
	}

	/**
	 * 获取时间戳 Header名称。
	 *
	 * @return Header名称
	 */
	public String getTimestampHeaderName() {
		return timestampHeaderName;
	}

	/**
	 * 设置时间戳 Header名称。
	 *
	 * @param timestampHeaderName Header名称
	 * @return this
	 */
	public SignHeaderNames setTimestampHeaderName(final String timestampHeaderName) {
		this.timestampHeaderName = normalizeHeaderName(timestampHeaderName);
		return this;
	}

	/**
	 * 获取随机串 Header名称。
	 *
	 * @return Header名称
	 */
	public String getNonceHeaderName() {
		return nonceHeaderName;
	}

	/**
	 * 设置随机串 Header名称。
	 *
	 * @param nonceHeaderName Header名称
	 * @return this
	 */
	public SignHeaderNames setNonceHeaderName(final String nonceHeaderName) {
		this.nonceHeaderName = normalizeHeaderName(nonceHeaderName);
		return this;
	}

	/**
	 * 获取签名算法 Header名称。
	 *
	 * @return Header名称
	 */
	public String getAlgorithmHeaderName() {
		return algorithmHeaderName;
	}

	/**
	 * 设置签名算法 Header名称。
	 *
	 * @param algorithmHeaderName Header名称
	 * @return this
	 */
	public SignHeaderNames setAlgorithmHeaderName(final String algorithmHeaderName) {
		this.algorithmHeaderName = normalizeHeaderName(algorithmHeaderName);
		return this;
	}

	/**
	 * 获取签名结果 Header名称。
	 *
	 * @return Header名称
	 */
	public String getSignatureHeaderName() {
		return signatureHeaderName;
	}

	/**
	 * 设置签名结果 Header名称。
	 *
	 * @param signatureHeaderName Header名称
	 * @return this
	 */
	public SignHeaderNames setSignatureHeaderName(final String signatureHeaderName) {
		this.signatureHeaderName = normalizeHeaderName(signatureHeaderName);
		return this;
	}

	/**
	 * 获取默认参与签名的Header名称。
	 *
	 * @return 小写Header名称集合
	 */
	Set<String> signedHeaderNameSet() {
		final Set<String> set = new LinkedHashSet<>();
		addLower(set, accessKeyIdHeaderName);
		addLower(set, timestampHeaderName);
		addLower(set, nonceHeaderName);
		addLower(set, algorithmHeaderName);
		return set;
	}

	/**
	 * 判断是否为签名结果Header。
	 *
	 * @param name Header名称
	 * @return 是否为签名结果Header
	 */
	boolean isSignatureHeader(final String name) {
		return equalsHeader(signatureHeaderName, name);
	}

	private static void addLower(final Set<String> set, final String name) {
		if (StrUtil.isNotBlank(name)) {
			set.add(name.trim().toLowerCase(Locale.ROOT));
		}
	}

	private static boolean equalsHeader(final String name1, final String name2) {
		return null != name1 && name1.equalsIgnoreCase(name2);
	}

	private static String normalizeHeaderName(final String headerName) {
		Assert.notBlank(headerName, "Header name must be not blank!");
		if (containsCrlf(headerName)) {
			throw new HttpSignException(HttpSignErrorCode.SIGN_CANONICALIZE_FAILED, "Header name contains CR/LF.");
		}
		return headerName.trim();
	}

	private static boolean containsCrlf(final String value) {
		return null != value && (value.indexOf('\r') >= 0 || value.indexOf('\n') >= 0);
	}
}
