package cn.hutool.v7.crypto.sign.http;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * HTTP签名器测试。
 *
 * @author mumu
 */
public class HttpSignerTest {

	private static final String AK = "ak_test_123";
	private static final String SK = "sk_test_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
	private static final long NOW = 1731312000L;

	@Test
	public void defaultHeaderNamesTest() {
		final HttpSignResult result = HttpSigner.sign(baseRequest(), AkSkCredentials.of(AK, SK), fixedConfig());

		assertEquals(AK, result.getHeaders().get("X-Access-Key-Id"));
		assertEquals(String.valueOf(NOW), result.getHeaders().get("X-Sign-Timestamp"));
		assertEquals("n8K3pV9", result.getHeaders().get("X-Sign-Nonce"));
		assertEquals("HmacSHA256", result.getHeaders().get("X-Sign-Algorithm"));
		assertNotNull(result.getHeaders().get("X-Signature"));
	}

	@Test
	public void openapiHeaderNamesTest() {
		final HttpSignConfig config = fixedConfig().setSignHeaderNames(SignHeaderNames.openapi());
		final HttpSignResult result = HttpSigner.sign(baseRequest(), AkSkCredentials.of(AK, SK), config);

		assertEquals(AK, result.getHeaders().get("X-Openapi-App-Id"));
		assertTrue(result.getHeaders().containsKey("X-Openapi-Signature"));
	}

	@Test
	public void customHeaderNamesTest() {
		final SignHeaderNames channelHeaderNames = SignHeaderNames.of(
			"X-Channel-Ak",
			"X-Channel-Time",
			"X-Channel-Nonce",
			"X-Channel-Algorithm",
			"X-Channel-Sign"
		);
		final HttpSignConfig config = fixedConfig().setSignHeaderNames(channelHeaderNames);
		final HttpSignResult signResult = HttpSigner.sign(baseRequest(), AkSkCredentials.of(AK, SK), config);
		final HttpSignRequest request = baseRequest();
		applyHeaders(request, signResult.getHeaders());

		assertTrue(signResult.getHeaders().containsKey("X-Channel-Ak"));
		assertTrue(signResult.getHeaders().containsKey("X-Channel-Sign"));
		assertTrue(signResult.getCanonicalRequest().getCanonicalHeaders().contains("x-channel-ak=" + AK));
		assertTrue(signResult.getCanonicalRequest().getCanonicalHeaders().contains("x-channel-time=" + NOW));
		assertTrue(HttpSignVerifier.create(secretProvider(HttpSignAlgorithm.HMAC_SHA256), MemoryNonceStore.create(), config).verify(request).isSuccess());
	}

	@Test
	public void customSignedAndExcludedHeaderTest() {
		final HttpSignRequest request = baseRequest()
			.setHeader("X-Channel-Id", "  channel-a  ")
			.setHeader("X-Trace-Id", "trace-1");
		final HttpSignConfig config = fixedConfig()
			.addSignedHeaderName(" X-Channel-Id ")
			.addExcludedHeaderName(" X-Trace-Id ");

		final HttpSignResult result = HttpSigner.sign(request, AkSkCredentials.of(AK, SK), config);

		assertTrue(result.getCanonicalRequest().getCanonicalHeaders().contains("x-channel-id=channel-a"));
		assertFalse(result.getCanonicalRequest().getCanonicalHeaders().contains("x-trace-id"));
	}

	@Test
	public void hexSignatureEncodingTest() {
		final HttpSignConfig config = fixedConfig().setSignatureEncoding(SignatureEncoding.HEX);
		final HttpSignResult result = HttpSigner.sign(baseRequest(), AkSkCredentials.of(AK, SK), config);

		assertEquals(64, result.getSignature().length());
		assertTrue(result.getSignature().matches("[0-9a-f]+"));
	}

	@Test
	public void signAlgorithmsTest() {
		assertAlgorithm(HttpSignAlgorithm.HMAC_SHA1);
		assertAlgorithm(HttpSignAlgorithm.HMAC_SHA256);
		assertAlgorithm(HttpSignAlgorithm.HMAC_SHA512);
		assertAlgorithm(HttpSignAlgorithm.HMAC_MD5);
		assertAlgorithm(HttpSignAlgorithm.HMAC_SM3);
	}

	@Test
	public void bodyDigestAlgorithmTest() {
		final HttpSignConfig config = fixedConfig().setBodyDigestAlgorithm(HttpSignDigestAlgorithm.SM3);
		final HttpSignResult signResult = HttpSigner.sign(baseRequest(), AkSkCredentials.of(AK, SK), config);

		assertTrue(HttpSignVerifier.create(secretProvider(HttpSignAlgorithm.HMAC_SHA256), MemoryNonceStore.create(), config)
			.verify(signedRequest(signResult)).isSuccess());
		assertFalse(HttpSignVerifier.create(secretProvider(HttpSignAlgorithm.HMAC_SHA256), MemoryNonceStore.create(), fixedConfig())
			.verify(signedRequest(signResult)).isSuccess());
	}

	@Test
	public void headerNameInvalidTest() {
		assertThrows(IllegalArgumentException.class,
			() -> SignHeaderNames.create().setAccessKeyIdHeaderName(" "));

		final HttpSignException exception = assertThrows(HttpSignException.class,
			() -> SignHeaderNames.create().setAccessKeyIdHeaderName("X-Ak\r\n"));
		assertEquals(HttpSignErrorCode.SIGN_CANONICALIZE_FAILED, exception.getErrorCode());

		final HttpSignException configException = assertThrows(HttpSignException.class,
			() -> HttpSignConfig.create().addSignedHeaderName("X-Channel\r\n"));
		assertEquals(HttpSignErrorCode.SIGN_CANONICALIZE_FAILED, configException.getErrorCode());
	}

	private static void assertAlgorithm(final HttpSignAlgorithm algorithm) {
		final HttpSignConfig config = fixedConfig()
			.setDefaultAlgorithm(algorithm)
			.allowAlgorithms(algorithm);
		final HttpSignResult signResult = HttpSigner.sign(baseRequest(), AkSkCredentials.of(AK, SK), config);

		assertEquals(algorithm.getAlgorithmName(), signResult.getHeaders().get("X-Sign-Algorithm"));
		assertTrue(HttpSignVerifier.create(secretProvider(algorithm), MemoryNonceStore.create(), config)
			.verify(signedRequest(signResult)).isSuccess());
	}

	private static HttpSignRequest signedRequest(final HttpSignResult signResult) {
		final HttpSignRequest request = baseRequest();
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
		return HttpSignConfig.create()
			.setTimestampProvider(() -> NOW)
			.setNonceGenerator(() -> "n8K3pV9");
	}

	private static SecretProvider secretProvider(final HttpSignAlgorithm algorithm) {
		return accessKeyId -> {
			if (AK.equals(accessKeyId)) {
				return SecretInfo.of(AK, SK).allowAlgorithms(algorithm);
			}
			return null;
		};
	}

	private static void applyHeaders(final HttpSignRequest request, final Map<String, String> headers) {
		for (final Map.Entry<String, String> entry : headers.entrySet()) {
			request.setHeader(entry.getKey(), entry.getValue());
		}
	}
}
