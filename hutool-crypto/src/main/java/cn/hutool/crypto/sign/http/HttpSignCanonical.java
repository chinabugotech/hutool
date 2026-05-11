package cn.hutool.crypto.sign.http;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

/**
 * HTTP签名规范化工具。
 *
 * @author mumu
 * @since 5.8.45
 */
class HttpSignCanonical {

	private static final byte[] EMPTY_BODY = new byte[0];

	/**
	 * 空字节数组的SHA-256摘要，HTTP无请求体时使用。
	 */
	static final String EMPTY_BODY_SHA256 = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";

	/**
	 * 创建规范请求。
	 *
	 * @param request 签名请求
	 * @param config  签名配置
	 * @return 规范请求
	 */
	CanonicalRequest canonicalize(final HttpSignRequest request, final HttpSignConfig config) {
		final String method = canonicalMethod(request.getMethod());
		return new CanonicalRequest(
			CanonicalRequest.VERSION,
			method,
			canonicalizePath(request.getPath(), config.getCharset()),
			canonicalizeQuery(request, config.getCharset()),
			canonicalizeHeaders(request, config),
			hashBody(method, request.getBodyBytes(), config)
		);
	}

	private String canonicalMethod(final String method) {
		final String upperMethod = nullToEmpty(method).trim().toUpperCase(Locale.ROOT);
		if ("GET".equals(upperMethod) || "POST".equals(upperMethod)
			|| "PUT".equals(upperMethod) || "DELETE".equals(upperMethod)) {
			return upperMethod;
		}
		throw new HttpSignException(HttpSignErrorCode.SIGN_UNSUPPORTED_METHOD, "Unsupported HTTP method: " + method);
	}

	/**
	 * 规范化URI路径。
	 *
	 * @param path    URI路径
	 * @param charset 字符集
	 * @return 规范化URI路径
	 */
	String canonicalizePath(final String path, final Charset charset) {
		final String target = StrUtil.isBlank(path) ? "/" : path;
		return percentEncode(target.startsWith("/") ? target : "/" + target, charset, true, true);
	}

	/**
	 * 规范化查询参数。
	 *
	 * @param request 签名请求
	 * @param charset 字符集
	 * @return 规范化查询参数
	 */
	String canonicalizeQuery(final HttpSignRequest request, final Charset charset) {
		if (CollUtil.isEmpty(request.getQueryParams())) {
			return StrUtil.EMPTY;
		}
		final List<CanonicalQueryParam> canonicalQueryParams = new ArrayList<>();
		for (final HttpSignRequest.QueryParam queryParam : request.getQueryParams()) {
			final String rawName = nullToEmpty(queryParam.getName());
			final String rawValue = nullToEmpty(queryParam.getValue());
			canonicalQueryParams.add(new CanonicalQueryParam(
				rawName,
				rawValue,
				percentEncode(rawName, charset, false, false),
				percentEncode(rawValue, charset, false, false)
			));
		}
		Collections.sort(canonicalQueryParams, new Comparator<CanonicalQueryParam>() {
			@Override
			public int compare(final CanonicalQueryParam param1, final CanonicalQueryParam param2) {
				final int nameCompare = param1.rawName.compareTo(param2.rawName);
				return 0 == nameCompare ? param1.rawValue.compareTo(param2.rawValue) : nameCompare;
			}
		});
		final StringBuilder canonicalQuery = new StringBuilder();
		for (final CanonicalQueryParam queryParam : canonicalQueryParams) {
			if (canonicalQuery.length() > 0) {
				canonicalQuery.append('&');
			}
			canonicalQuery.append(queryParam.encodedName).append('=').append(queryParam.encodedValue);
		}
		return canonicalQuery.toString();
	}

	/**
	 * 规范化Header。
	 *
	 * @param request 签名请求
	 * @param config  签名配置
	 * @return 规范化Header
	 */
	String canonicalizeHeaders(final HttpSignRequest request, final HttpSignConfig config) {
		final TreeMap<String, List<String>> signedHeaders = new TreeMap<>();
		for (final Map.Entry<String, List<String>> entry : request.getHeaders().entrySet()) {
			final String name = entry.getKey();
			checkHeader(name, null);
			final String lowerName = name.toLowerCase(Locale.ROOT);
			if (config.getSignHeaderNames().isSignatureHeader(name) || false == config.isSignedHeader(lowerName)) {
				continue;
			}
			List<String> values = signedHeaders.get(lowerName);
			if (null == values) {
				values = new ArrayList<>();
				signedHeaders.put(lowerName, values);
			}
			if (null != entry.getValue()) {
				for (final String value : entry.getValue()) {
					checkHeader(name, value);
					values.add(normalizeHeaderValue(value));
				}
			}
		}
		final StringBuilder canonicalHeaders = new StringBuilder();
		for (final Map.Entry<String, List<String>> entry : signedHeaders.entrySet()) {
			if (canonicalHeaders.length() > 0) {
				canonicalHeaders.append('&');
			}
			canonicalHeaders.append(entry.getKey()).append('=').append(CollUtil.join(entry.getValue(), ","));
		}
		return canonicalHeaders.toString();
	}

	/**
	 * 计算Body摘要。
	 *
	 * @param method    HTTP方法
	 * @param bodyBytes Body字节
	 * @param config    签名配置
	 * @return SHA-256摘要16进制
	 */
	String hashBody(final String method, final byte[] bodyBytes, final HttpSignConfig config) {
		// GET请求按无请求体处理，避免客户端误传Body导致签名不稳定。
		if ("GET".equals(method)) {
			return config.getBodyDigestAlgorithm().digestHex(EMPTY_BODY);
		}
		final byte[] bytes = null == bodyBytes ? EMPTY_BODY : bodyBytes;
		if (bytes.length > config.getMaxBodySignBytes()) {
			throw new HttpSignException(HttpSignErrorCode.SIGN_BODY_TOO_LARGE, "Body is too large to sign.");
		}
		return config.getBodyDigestAlgorithm().digestHex(bytes);
	}

	private static String normalizeHeaderValue(final String value) {
		return nullToEmpty(value).trim().replaceAll("\\s+", " ");
	}

	private static void checkHeader(final String name, final String value) {
		if (containsCrlf(name) || containsCrlf(value)) {
			throw new HttpSignException(HttpSignErrorCode.SIGN_CANONICALIZE_FAILED, "Header contains CR/LF.");
		}
	}

	private static boolean containsCrlf(final String value) {
		return null != value && (value.indexOf('\r') >= 0 || value.indexOf('\n') >= 0);
	}

	private static String nullToEmpty(final String value) {
		return null == value ? "" : value;
	}

	private static String percentEncode(final String value, Charset charset, final boolean keepSlash, final boolean keepEncodedPercent) {
		if (null == charset) {
			charset = CharsetUtil.CHARSET_UTF_8;
		}
		final StringBuilder builder = new StringBuilder();
		for (int i = 0; i < value.length();) {
			final int codePoint = value.codePointAt(i);
			if (isUnreserved(codePoint) || (keepSlash && '/' == codePoint)) {
				builder.appendCodePoint(codePoint);
				i += Character.charCount(codePoint);
			} else if (keepEncodedPercent && '%' == codePoint && i + 2 < value.length()
				&& isHex(value.charAt(i + 1)) && isHex(value.charAt(i + 2))) {
				builder.append('%')
					.append(Character.toUpperCase(value.charAt(i + 1)))
					.append(Character.toUpperCase(value.charAt(i + 2)));
				i += 3;
			} else {
				final byte[] bytes = new String(Character.toChars(codePoint)).getBytes(charset);
				for (final byte b : bytes) {
					builder.append('%');
					HexUtil.appendHex(builder, b, false);
				}
				i += Character.charCount(codePoint);
			}
		}
		return builder.toString();
	}

	private static boolean isUnreserved(final int c) {
		return c >= 'A' && c <= 'Z'
			|| c >= 'a' && c <= 'z'
			|| c >= '0' && c <= '9'
			|| '-' == c || '_' == c || '.' == c || '~' == c;
	}

	private static boolean isHex(final char c) {
		return c >= '0' && c <= '9' || c >= 'A' && c <= 'F' || c >= 'a' && c <= 'f';
	}

	private static class CanonicalQueryParam {
		private final String rawName;
		private final String rawValue;
		private final String encodedName;
		private final String encodedValue;

		private CanonicalQueryParam(final String rawName, final String rawValue,
									final String encodedName, final String encodedValue) {
			this.rawName = rawName;
			this.rawValue = rawValue;
			this.encodedName = encodedName;
			this.encodedValue = encodedValue;
		}
	}
}
