package cn.hutool.crypto.sign.http;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;

import java.security.MessageDigest;
import java.util.Set;

/**
 * HTTP AK/SK验签器。
 * <p>
 * 验签器构造时会复制{@link HttpSignConfig}配置快照。只要传入的{@link SecretProvider}
 * 与{@link NonceStore}本身可并发使用，验签器实例可在多个线程中复用。
 *
 * @author mumu
 * @since 5.8.45
 */
public class HttpSignVerifier {

	private final SecretProvider secretProvider;
	private final NonceStore nonceStore;
	private final HttpSignConfig config;
	private final HttpSignCanonicalizer canonicalizer;

	/**
	 * 创建验签器。
	 *
	 * @param secretProvider 密钥提供器
	 * @param nonceStore     Nonce存储
	 * @param config         签名配置
	 * @return 验签器
	 */
	public static HttpSignVerifier create(final SecretProvider secretProvider, final NonceStore nonceStore, final HttpSignConfig config) {
		return new HttpSignVerifier(secretProvider, nonceStore, config);
	}

	/**
	 * 构造验签器。
	 *
	 * @param secretProvider 密钥提供器
	 * @param nonceStore     Nonce存储，传{@code null}表示不做防重放
	 * @param config         签名配置
	 */
	public HttpSignVerifier(final SecretProvider secretProvider, final NonceStore nonceStore, final HttpSignConfig config) {
		Assert.notNull(secretProvider, "SecretProvider must be not null!");
		this.secretProvider = secretProvider;
		this.nonceStore = nonceStore;
		this.config = null == config ? HttpSignConfig.create() : config.copy();
		this.canonicalizer = new HttpSignCanonicalizer();
	}

	/**
	 * 验签。
	 *
	 * @param request 请求
	 * @return 验签结果
	 */
	public HttpSignVerifyResult verify(final HttpSignRequest request) {
		final SignHeaderNames signHeaderNames = config.getSignHeaderNames();
		final String accessKeyId = request.getHeader(signHeaderNames.getAccessKeyIdHeaderName());
		if (StrUtil.isBlank(accessKeyId)) {
			return HttpSignVerifyResult.fail(HttpSignErrorCode.SIGN_MISSING_ACCESS_KEY_ID, "Missing access key id.");
		}
		final String timestampValue = request.getHeader(signHeaderNames.getTimestampHeaderName());
		if (StrUtil.isBlank(timestampValue)) {
			return HttpSignVerifyResult.fail(HttpSignErrorCode.SIGN_MISSING_TIMESTAMP, "Missing timestamp.");
		}
		final String nonce = request.getHeader(signHeaderNames.getNonceHeaderName());
		if (StrUtil.isBlank(nonce)) {
			return HttpSignVerifyResult.fail(HttpSignErrorCode.SIGN_MISSING_NONCE, "Missing nonce.");
		}
		final String algorithmValue = request.getHeader(signHeaderNames.getAlgorithmHeaderName());
		if (StrUtil.isBlank(algorithmValue)) {
			return HttpSignVerifyResult.fail(HttpSignErrorCode.SIGN_MISSING_ALGORITHM, "Missing algorithm.");
		}
		final String signature = request.getHeader(signHeaderNames.getSignatureHeaderName());
		if (StrUtil.isBlank(signature)) {
			return HttpSignVerifyResult.fail(HttpSignErrorCode.SIGN_MISSING_SIGNATURE, "Missing signature.");
		}

		final HttpSignAlgorithm algorithm = HttpSignAlgorithm.of(algorithmValue);
		if (null == algorithm || false == isAlgorithmAllowed(config.getAllowedAlgorithms(), algorithm)) {
			return HttpSignVerifyResult.fail(HttpSignErrorCode.SIGN_UNSUPPORTED_ALGORITHM, "Unsupported sign algorithm.");
		}

		final long timestamp;
		try {
			timestamp = Long.parseLong(timestampValue);
		} catch (final NumberFormatException e) {
			return HttpSignVerifyResult.fail(HttpSignErrorCode.SIGN_TIMESTAMP_INVALID, "Invalid timestamp.");
		}
		final long now = config.getTimestampProvider().currentTimestamp();
		if (Math.abs(now - timestamp) > config.getTimestampSkewSeconds()) {
			return HttpSignVerifyResult.fail(HttpSignErrorCode.SIGN_TIMESTAMP_SKEW, "Timestamp skew is too large.");
		}

		final SecretInfo secretInfo = secretProvider.loadSecret(accessKeyId);
		if (null == secretInfo) {
			return HttpSignVerifyResult.fail(HttpSignErrorCode.SIGN_ACCESS_KEY_NOT_FOUND, "Access key not found.");
		}
		if (false == secretInfo.isEnabled()) {
			return HttpSignVerifyResult.fail(HttpSignErrorCode.SIGN_ACCESS_KEY_DISABLED, "Access key is disabled.");
		}
		if (null != secretInfo.getExpireAt() && secretInfo.getExpireAt() < now) {
			return HttpSignVerifyResult.fail(HttpSignErrorCode.SIGN_SECRET_EXPIRED, "Secret is expired.");
		}
		if (false == secretInfo.getAllowedAlgorithms().isEmpty()
			&& false == secretInfo.getAllowedAlgorithms().contains(algorithm)) {
			return HttpSignVerifyResult.fail(HttpSignErrorCode.SIGN_UNSUPPORTED_ALGORITHM, "Algorithm is not allowed by secret.");
		}

		final HttpSignResult expectedSignResult;
		try {
			expectedSignResult = HttpSigner.signPrepared(request, secretInfo.getAccessKeySecret(), algorithm, config, canonicalizer);
		} catch (final HttpSignException e) {
			return HttpSignVerifyResult.fail(e.getErrorCode(), e.getMessage());
		}
		if (false == constantTimeEquals(expectedSignResult.getSignature(), signature)) {
			return HttpSignVerifyResult.fail(HttpSignErrorCode.SIGN_MISMATCH, "Signature mismatch.",
				expectedSignResult.getCanonicalRequest(), expectedSignResult.getStringToSign());
		}
		final long ttlSeconds = Math.max(1L, config.getTimestampSkewSeconds() * 2);
		if (null != nonceStore && false == nonceStore.putIfAbsent(accessKeyId, nonce, ttlSeconds)) {
			return HttpSignVerifyResult.fail(HttpSignErrorCode.SIGN_NONCE_REPLAY, "Nonce replay.");
		}
		return HttpSignVerifyResult.success(expectedSignResult.getCanonicalRequest(), expectedSignResult.getStringToSign());
	}

	/**
	 * 判断算法是否在服务端配置白名单中。
	 *
	 * @param allowedAlgorithmSet 允许的算法集合
	 * @param algorithm         待判断算法
	 * @return 是否允许
	 */
	private static boolean isAlgorithmAllowed(final Set<HttpSignAlgorithm> allowedAlgorithmSet, final HttpSignAlgorithm algorithm) {
		return null != allowedAlgorithmSet && allowedAlgorithmSet.contains(algorithm);
	}

	/**
	 * 常量时间比较签名字符串。
	 *
	 * @param expected 服务端计算签名
	 * @param actual   客户端传入签名
	 * @return 是否一致
	 */
	private boolean constantTimeEquals(final String expected, final String actual) {
		return MessageDigest.isEqual(
			expected.getBytes(config.getCharset()),
			actual.getBytes(config.getCharset())
		);
	}
}
