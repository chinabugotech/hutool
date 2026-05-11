package cn.hutool.v7.crypto.sign.http;

import cn.hutool.v7.crypto.digest.DigestUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * HTTP签名规范化测试。
 *
 * @author mumu
 */
public class HttpSignCanonicalTest {

	private final HttpSignCanonical canonicalizer = new HttpSignCanonical();

	@Test
	public void canonicalizeQueryTest() {
		final HttpSignRequest request = HttpSignRequest.create()
			.addQueryParam("b", "2")
			.addQueryParam("a", "张三")
			.addQueryParam("a", "abc")
			.addQueryParam("empty", null)
			.addQueryParam("space", "a b")
			.addQueryParam("plus", "a+b")
			.addQueryParam("slash", "a/b")
			.addQueryParam("emoji", "\uD83D\uDE42");

		assertEquals(
			"a=abc&a=%E5%BC%A0%E4%B8%89&b=2&emoji=%F0%9F%99%82&empty=&plus=a%2Bb&slash=a%2Fb&space=a%20b",
			canonicalizer.canonicalizeQuery(request, HttpSignConfig.create().getCharset())
		);
	}

	@Test
	public void canonicalizeQueryOrderIndependentTest() {
		final HttpSignRequest request1 = HttpSignRequest.create()
			.addQueryParam("b", "2")
			.addQueryParam("a", "张三")
			.addQueryParam("a", "abc");
		final HttpSignRequest request2 = HttpSignRequest.create()
			.addQueryParam("a", "abc")
			.addQueryParam("b", "2")
			.addQueryParam("a", "张三");

		assertEquals(
			canonicalizer.canonicalizeQuery(request1, HttpSignConfig.create().getCharset()),
			canonicalizer.canonicalizeQuery(request2, HttpSignConfig.create().getCharset())
		);
	}

	@Test
	public void canonicalizePathTest() {
		assertEquals(
			"/api/%E5%BC%A0%20%E4%B8%89",
			canonicalizer.canonicalizePath("api/%E5%BC%A0 三", HttpSignConfig.create().getCharset())
		);
	}

	@Test
	public void canonicalizeHeadersTest() {
		final HttpSignRequest request = HttpSignRequest.create()
			.setHeader("X-Sign-Timestamp", "  1731312000  ")
			.setHeader("X-Access-Key-Id", "ak_test")
			.setHeader("X-Sign-Algorithm", "HmacSHA256")
			.addHeader("X-Sign-Nonce", " n1  ")
			.addHeader("x-sign-nonce", "n2")
			.setHeader("X-Signature", "ignored")
			.setHeader("Content-Length", "10")
			.setHeader("User-Agent", "client");

		assertEquals(
			"x-access-key-id=ak_test&x-sign-algorithm=HmacSHA256&x-sign-nonce=n1,n2&x-sign-timestamp=1731312000",
			canonicalizer.canonicalizeHeaders(request, HttpSignConfig.create())
		);
	}

	@Test
	public void canonicalizeHeaderCrlfTest() {
		final HttpSignRequest request = HttpSignRequest.create()
			.setHeader("X-Access-Key-Id", "ak\r\nbad");

		final HttpSignException exception = assertThrows(HttpSignException.class,
			() -> canonicalizer.canonicalizeHeaders(request, HttpSignConfig.create()));
		assertEquals(HttpSignErrorCode.SIGN_CANONICALIZE_FAILED, exception.getErrorCode());
	}

	@Test
	public void hashBodyTest() {
		final HttpSignConfig config = HttpSignConfig.create();

		assertEquals(HttpSignCanonical.EMPTY_BODY_SHA256, DigestUtil.sha256Hex(new byte[0]));
		assertEquals(HttpSignCanonical.EMPTY_BODY_SHA256, canonicalizer.hashBody("GET", "abc".getBytes(config.getCharset()), config));
		assertEquals(DigestUtil.sha256Hex("abc".getBytes(config.getCharset())), canonicalizer.hashBody("POST", "abc".getBytes(config.getCharset()), config));
		assertEquals(DigestUtil.sha256Hex("abc".getBytes(config.getCharset())), canonicalizer.hashBody("PUT", "abc".getBytes(config.getCharset()), config));
		assertEquals(HttpSignCanonical.EMPTY_BODY_SHA256, canonicalizer.hashBody("DELETE", null, config));
	}

	@Test
	public void emptyBodyDigestAlgorithmsTest() {
		assertEmptyBodyHash(HttpSignDigestAlgorithm.SHA1, "da39a3ee5e6b4b0d3255bfef95601890afd80709");
		assertEmptyBodyHash(HttpSignDigestAlgorithm.SHA256, HttpSignCanonical.EMPTY_BODY_SHA256);
		assertEmptyBodyHash(HttpSignDigestAlgorithm.SHA512,
			"cf83e1357eefb8bdf1542850d66d8007d620e4050b5715dc83f4a921d36ce9ce"
				+ "47d0d13c5d85f2b0ff8318d2877eec2f63b931bd47417a81a538327af927da3e");
		assertEmptyBodyHash(HttpSignDigestAlgorithm.MD5, "d41d8cd98f00b204e9800998ecf8427e");
		assertEmptyBodyHash(HttpSignDigestAlgorithm.SM3, "1ab21d8355cfa17f8e61194831e81a8f22bec8c728fefb747ed035eb5082aa2b");
	}

	@Test
	public void bodyTooLargeTest() {
		final HttpSignConfig config = HttpSignConfig.create().setMaxBodySignBytes(1);

		final HttpSignException exception = assertThrows(HttpSignException.class,
			() -> canonicalizer.hashBody("POST", "12".getBytes(config.getCharset()), config));
		assertEquals(HttpSignErrorCode.SIGN_BODY_TOO_LARGE, exception.getErrorCode());
	}

	@Test
	public void unsupportedMethodTest() {
		final HttpSignException exception = assertThrows(HttpSignException.class,
			() -> canonicalizer.canonicalize(HttpSignRequest.create().setMethod("PATCH"), HttpSignConfig.create()));
		assertEquals(HttpSignErrorCode.SIGN_UNSUPPORTED_METHOD, exception.getErrorCode());
	}

	private void assertEmptyBodyHash(final HttpSignDigestAlgorithm digestAlgorithm, final String expectedHash) {
		final HttpSignConfig config = HttpSignConfig.create().setBodyDigestAlgorithm(digestAlgorithm);

		assertEquals(expectedHash, canonicalizer.hashBody("POST", null, config));
		assertEquals(expectedHash, canonicalizer.hashBody("GET", "abc".getBytes(config.getCharset()), config));
	}
}
