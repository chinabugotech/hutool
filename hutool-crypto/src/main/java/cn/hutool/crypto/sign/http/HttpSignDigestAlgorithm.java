package cn.hutool.crypto.sign.http;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.DigestUtil;

/**
 * HTTP签名Body摘要算法。
 *
 * @author mumu
 * @since 5.8.45
 */
public enum HttpSignDigestAlgorithm {
	/**
	 * SHA-256，默认推荐算法。
	 */
	SHA256("SHA-256", DigestAlgorithm.SHA256),
	/**
	 * SHA-1，仅用于兼容老系统。
	 */
	SHA1("SHA-1", DigestAlgorithm.SHA1),
	/**
	 * SHA-512。
	 */
	SHA512("SHA-512", DigestAlgorithm.SHA512),
	/**
	 * MD5，仅用于兼容老系统。
	 */
	MD5("MD5", DigestAlgorithm.MD5),
	/**
	 * 国密SM3摘要算法。
	 */
	SM3("SM3", null);

	/**
	 * 算法名称。
	 */
	private final String algorithmName;
	/**
	 * Hutool摘要算法枚举，SM3单独使用Hutool SM3实现。
	 */
	private final DigestAlgorithm digestAlgorithm;

	HttpSignDigestAlgorithm(final String algorithmName, final DigestAlgorithm digestAlgorithm) {
		this.algorithmName = algorithmName;
		this.digestAlgorithm = digestAlgorithm;
	}

	/**
	 * 获取算法名称。
	 *
	 * @return 算法名称
	 */
	public String getAlgorithmName() {
		return algorithmName;
	}

	String digestHex(final byte[] data) {
		if (SM3.equals(this)) {
			return cn.hutool.crypto.digest.SM3.create().digestHex(data);
		}
		return DigestUtil.digester(this.digestAlgorithm).digestHex(data);
	}
}
