package cn.hutool.crypto.sign.http;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.HmacAlgorithm;

/**
 * HTTP签名算法。
 *
 * @author mumu
 * @since 5.8.45
 */
public enum HttpSignAlgorithm {
	/**
	 * HmacSHA256，默认推荐算法。
	 */
	HMAC_SHA256("HmacSHA256", HmacAlgorithm.HmacSHA256),
	/**
	 * HmacSHA1，仅用于兼容老系统。
	 */
	HMAC_SHA1("HmacSHA1", HmacAlgorithm.HmacSHA1),
	/**
	 * HmacSHA512。
	 */
	HMAC_SHA512("HmacSHA512", HmacAlgorithm.HmacSHA512),
	/**
	 * HmacMD5，仅用于兼容老系统。
	 */
	HMAC_MD5("HmacMD5", HmacAlgorithm.HmacMD5),
	/**
	 * HmacSM3，需要运行环境支持BouncyCastle。
	 */
	HMAC_SM3("HmacSM3", HmacAlgorithm.HmacSM3);

	/**
	 * Header中传输的标准算法名称。
	 */
	private final String algorithmName;
	/**
	 * Hutool HMAC算法枚举。
	 */
	private final HmacAlgorithm hmacAlgorithm;

	HttpSignAlgorithm(final String algorithmName, final HmacAlgorithm hmacAlgorithm) {
		this.algorithmName = algorithmName;
		this.hmacAlgorithm = hmacAlgorithm;
	}

	/**
	 * 获取请求头中使用的算法名。
	 *
	 * @return 算法名
	 */
	public String getAlgorithmName() {
		return algorithmName;
	}

	/**
	 * 转为Hutool HMAC算法。
	 *
	 * @return Hutool HMAC算法
	 */
	public HmacAlgorithm toHmacAlgorithm() {
		return hmacAlgorithm;
	}

	/**
	 * 根据算法名解析。
	 *
	 * @param value 算法名
	 * @return 算法，无法解析返回{@code null}
	 */
	public static HttpSignAlgorithm of(final String value) {
		if (StrUtil.isBlank(value)) {
			return null;
		}
		for (final HttpSignAlgorithm algorithm : values()) {
			if (algorithm.algorithmName.equalsIgnoreCase(value) || algorithm.name().equalsIgnoreCase(value)) {
				return algorithm;
			}
		}
		return null;
	}
}
