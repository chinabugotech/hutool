package cn.hutool.v7.crypto.sign.http;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * HTTP验签器测试。
 *
 * @author mumu
 */
public class HttpSignVerifierTest {

	private static final String AK = "ak_test_123";
	private static final String SK = "sk_test_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
	private static final long NOW = 1731312000L;

	@Test
	public void verifySuccessAndReplayTest() {
		final HttpSignConfig config = fixedConfig();
		final HttpSignRequest request = signedRequest(baseRequest(), config);
		final HttpSignVerifier verifier = HttpSignVerifier.create(secretProvider(true, SK, HttpSignAlgorithm.HMAC_SHA256), MemoryNonceStore.create(), config);

		assertTrue(verifier.verify(request).isSuccess());
		assertError(verifier.verify(request), HttpSignErrorCode.SIGN_NONCE_REPLAY);
	}

	@Test
	public void verifyMissingHeaderTest() {
		final HttpSignVerifier verifier = HttpSignVerifier.create(secretProvider(true, SK, HttpSignAlgorithm.HMAC_SHA256), MemoryNonceStore.create(), fixedConfig());
		final HttpSignRequest request = baseRequest();

		assertError(verifier.verify(request), HttpSignErrorCode.SIGN_MISSING_ACCESS_KEY_ID);
		request.setHeader("X-Access-Key-Id", AK);
		assertError(verifier.verify(request), HttpSignErrorCode.SIGN_MISSING_TIMESTAMP);
		request.setHeader("X-Sign-Timestamp", String.valueOf(NOW));
		assertError(verifier.verify(request), HttpSignErrorCode.SIGN_MISSING_NONCE);
		request.setHeader("X-Sign-Nonce", "n8K3pV9");
		assertError(verifier.verify(request), HttpSignErrorCode.SIGN_MISSING_ALGORITHM);
		request.setHeader("X-Sign-Algorithm", "HmacSHA256");
		assertError(verifier.verify(request), HttpSignErrorCode.SIGN_MISSING_SIGNATURE);
	}

	@Test
	public void verifyTimestampInvalidAndSkewTest() {
		final HttpSignRequest invalidRequest = baseRequest()
			.setHeader("X-Access-Key-Id", AK)
			.setHeader("X-Sign-Timestamp", "bad")
			.setHeader("X-Sign-Nonce", "n8K3pV9")
			.setHeader("X-Sign-Algorithm", "HmacSHA256")
			.setHeader("X-Signature", "invalid");

		assertError(verifier(fixedConfig()).verify(invalidRequest), HttpSignErrorCode.SIGN_TIMESTAMP_INVALID);

		final HttpSignRequest expiredRequest = signedRequest(baseRequest(), fixedConfig());
		assertError(verifier(fixedConfig(NOW + 301)).verify(expiredRequest), HttpSignErrorCode.SIGN_TIMESTAMP_SKEW);
	}

	@Test
	public void verifySecretStateTest() {
		final HttpSignConfig config = fixedConfig();
		final HttpSignRequest request = signedRequest(baseRequest(), config);

		assertError(HttpSignVerifier.create(accessKeyId -> null, MemoryNonceStore.create(), config).verify(request), HttpSignErrorCode.SIGN_ACCESS_KEY_NOT_FOUND);
		assertError(HttpSignVerifier.create(secretProvider(false, SK, HttpSignAlgorithm.HMAC_SHA256), MemoryNonceStore.create(), config).verify(request), HttpSignErrorCode.SIGN_ACCESS_KEY_DISABLED);
		assertError(HttpSignVerifier.create(expiredSecretProvider(), MemoryNonceStore.create(), config).verify(request), HttpSignErrorCode.SIGN_SECRET_EXPIRED);
		assertError(HttpSignVerifier.create(secretProvider(true, "wrong_secret_xxxxxxxxxxxxxxxxxxxxxxxxxxx", HttpSignAlgorithm.HMAC_SHA256), MemoryNonceStore.create(), config).verify(request), HttpSignErrorCode.SIGN_MISMATCH);
	}

	@Test
	public void verifyAlgorithmAllowedTest() {
		final HttpSignConfig signConfig = fixedConfig()
			.setDefaultAlgorithm(HttpSignAlgorithm.HMAC_SHA1)
			.allowAlgorithms(HttpSignAlgorithm.HMAC_SHA1);
		final HttpSignRequest request = signedRequest(baseRequest(), signConfig);

		assertError(HttpSignVerifier.create(secretProvider(true, SK, HttpSignAlgorithm.HMAC_SHA1), MemoryNonceStore.create(), fixedConfig()).verify(request),
			HttpSignErrorCode.SIGN_UNSUPPORTED_ALGORITHM);

		final HttpSignConfig verifyConfig = fixedConfig().allowAlgorithms(HttpSignAlgorithm.HMAC_SHA1);
		assertError(HttpSignVerifier.create(secretProvider(true, SK, HttpSignAlgorithm.HMAC_SHA256), MemoryNonceStore.create(), verifyConfig).verify(request),
			HttpSignErrorCode.SIGN_UNSUPPORTED_ALGORITHM);
	}

	@Test
	public void verifyBodyChangedTest() {
		final HttpSignConfig config = fixedConfig();
		final HttpSignRequest request = signedRequest(baseRequest(), config);
		request.setBody("{\"name\":\"李四\"}");

		assertError(verifier(config).verify(request), HttpSignErrorCode.SIGN_MISMATCH);
	}

	@Test
	public void verifyQueryChangedTest() {
		final HttpSignConfig config = fixedConfig();
		final HttpSignRequest request = signedRequest(baseRequest().addQueryParam("a", "1"), config);
		request.addQueryParam("a", "2");

		assertError(verifier(config).verify(request), HttpSignErrorCode.SIGN_MISMATCH);
	}

	@Test
	public void verifyNullNonceStoreTest() {
		final HttpSignConfig config = fixedConfig();
		final HttpSignRequest request = signedRequest(baseRequest(), config);
		final HttpSignVerifier verifier = HttpSignVerifier.create(secretProvider(true, SK, HttpSignAlgorithm.HMAC_SHA256), null, config);

		assertTrue(verifier.verify(request).isSuccess());
		assertTrue(verifier.verify(request).isSuccess());
	}

	@Test
	public void verifierSecretProviderRequiredTest() {
		assertThrows(IllegalArgumentException.class,
			() -> HttpSignVerifier.create(null, MemoryNonceStore.create(), fixedConfig()));
	}

	private static HttpSignVerifier verifier(final HttpSignConfig config) {
		return HttpSignVerifier.create(secretProvider(true, SK, HttpSignAlgorithm.HMAC_SHA256), MemoryNonceStore.create(), config);
	}

	private static HttpSignRequest signedRequest(final HttpSignRequest request, final HttpSignConfig config) {
		final HttpSignResult signResult = HttpSigner.sign(request, AkSkCredentials.of(AK, SK), config);
		applyHeaders(request, signResult.getHeaders());
		return request;
	}

	private static HttpSignRequest baseRequest() {
		return HttpSignRequest.create()
			.setMethod("POST")
			.setPath("/api/user/create")
			.setHeader("Content-Type", "application/json")
			.setBody("{\"name\":\"张三\"}");
	}

	private static HttpSignConfig fixedConfig() {
		return fixedConfig(NOW);
	}

	private static HttpSignConfig fixedConfig(final long now) {
		return HttpSignConfig.create()
			.setTimestampProvider(() -> now)
			.setNonceGenerator(() -> "n8K3pV9");
	}

	private static SecretProvider secretProvider(final boolean enabled, final String secret, final HttpSignAlgorithm allowedAlgorithm) {
		return accessKeyId -> {
			if (AK.equals(accessKeyId)) {
				return SecretInfo.of(AK, secret)
					.setEnabled(enabled)
					.allowAlgorithms(allowedAlgorithm);
			}
			return null;
		};
	}

	private static SecretProvider expiredSecretProvider() {
		return accessKeyId -> {
			if (AK.equals(accessKeyId)) {
				return SecretInfo.of(AK, SK)
					.setExpireAt(NOW - 1)
					.allowAlgorithms(HttpSignAlgorithm.HMAC_SHA256);
			}
			return null;
		};
	}

	private static void applyHeaders(final HttpSignRequest request, final Map<String, String> headers) {
		for (final Map.Entry<String, String> entry : headers.entrySet()) {
			request.setHeader(entry.getKey(), entry.getValue());
		}
	}

	private static void assertError(final HttpSignVerifyResult result, final HttpSignErrorCode errorCode) {
		assertFalse(result.isSuccess());
		assertEquals(errorCode, result.getErrorCode());
	}
}
