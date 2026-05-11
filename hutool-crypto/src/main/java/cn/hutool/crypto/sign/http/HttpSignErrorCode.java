package cn.hutool.crypto.sign.http;

/**
 * HTTP签名错误码。
 *
 * @author mumu
 * @since 5.8.45
 */
public enum HttpSignErrorCode {
	/**
	 * 缺少访问身份ID。
	 */
	SIGN_MISSING_ACCESS_KEY_ID,
	/**
	 * 缺少时间戳。
	 */
	SIGN_MISSING_TIMESTAMP,
	/**
	 * 缺少Nonce。
	 */
	SIGN_MISSING_NONCE,
	/**
	 * 缺少签名算法。
	 */
	SIGN_MISSING_ALGORITHM,
	/**
	 * 缺少签名值。
	 */
	SIGN_MISSING_SIGNATURE,
	/**
	 * 不支持的HTTP方法。
	 */
	SIGN_UNSUPPORTED_METHOD,
	/**
	 * 不支持或不允许的签名算法。
	 */
	SIGN_UNSUPPORTED_ALGORITHM,
	/**
	 * 未找到访问身份ID对应的密钥。
	 */
	SIGN_ACCESS_KEY_NOT_FOUND,
	/**
	 * 访问密钥已禁用。
	 */
	SIGN_ACCESS_KEY_DISABLED,
	/**
	 * 访问密钥已过期。
	 */
	SIGN_SECRET_EXPIRED,
	/**
	 * 时间戳格式非法。
	 */
	SIGN_TIMESTAMP_INVALID,
	/**
	 * 时间戳偏移超过允许范围。
	 */
	SIGN_TIMESTAMP_SKEW,
	/**
	 * Nonce重复。
	 */
	SIGN_NONCE_REPLAY,
	/**
	 * Body超过允许签名大小。
	 */
	SIGN_BODY_TOO_LARGE,
	/**
	 * 规范化请求失败。
	 */
	SIGN_CANONICALIZE_FAILED,
	/**
	 * 签名不匹配。
	 */
	SIGN_MISMATCH
}
