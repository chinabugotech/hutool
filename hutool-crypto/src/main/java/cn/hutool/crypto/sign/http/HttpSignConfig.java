package cn.hutool.crypto.sign.http;

import cn.hutool.core.util.CharsetUtil;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

/**
 * HTTP签名配置。
 * <p>
 * 此对象为可变配置对象，适合在启动阶段或调用前组装参数；不建议在多个线程中同时修改同一个实例。
 * {@link HttpSigner}和{@link HttpSignVerifier}会在构造时复制配置快照，构造完成后可安全复用。
 *
 * @author mumu
 * @since 5.8.45
 */
public class HttpSignConfig {

	/**
	 * 默认Body签名大小上限：1MB。
	 */
	public static final long DEFAULT_MAX_BODY_SIGN_BYTES = 1024L * 1024L;
	/**
	 * 默认时间戳允许偏移秒数。
	 */
	public static final long DEFAULT_TIMESTAMP_SKEW_SECONDS = 300L;

	/**
	 * 客户端默认使用的签名算法。
	 */
	private HttpSignAlgorithm defaultAlgorithm = HttpSignAlgorithm.HMAC_SHA256;
	/**
	 * Body摘要算法。
	 */
	private HttpSignDigestAlgorithm bodyDigestAlgorithm = HttpSignDigestAlgorithm.SHA256;
	/**
	 * 服务端全局允许使用的签名算法白名单。
	 */
	private Set<HttpSignAlgorithm> allowedAlgorithmSet = new LinkedHashSet<>();
	/**
	 * 签名结果编码方式。
	 */
	private SignatureEncoding signatureEncoding = SignatureEncoding.BASE64;
	/**
	 * 签名字符串和密钥转字节时使用的字符集。
	 */
	private Charset charset = CharsetUtil.CHARSET_UTF_8;
	/**
	 * 服务端验签允许的时间戳偏移秒数。
	 */
	private long timestampSkewSeconds = DEFAULT_TIMESTAMP_SKEW_SECONDS;
	/**
	 * 请求体参与摘要计算的最大字节数。
	 */
	private long maxBodySignBytes = DEFAULT_MAX_BODY_SIGN_BYTES;
	/**
	 * 签名相关Header名称配置。
	 */
	private SignHeaderNames signHeaderNames = SignHeaderNames.defaultHeaders();
	/**
	 * 时间戳提供器。
	 */
	private TimestampProvider timestampProvider = TimestampProvider.SYSTEM;
	/**
	 * Nonce生成器。
	 */
	private NonceGenerator nonceGenerator = NonceGenerator.UUID_GENERATOR;
	/**
	 * 业务额外要求参与签名的Header名称集合，小写保存。
	 */
	private Set<String> extraSignedHeaderNameSet = new LinkedHashSet<>();
	/**
	 * 明确排除签名的Header名称集合，小写保存。
	 */
	private Set<String> excludedHeaderNameSet = defaultExcludedHeaderNames();

	/**
	 * 创建默认配置。
	 *
	 * @return 签名配置
	 */
	public static HttpSignConfig create() {
		return new HttpSignConfig();
	}

	/**
	 * 构造。
	 */
	public HttpSignConfig() {
		this.allowedAlgorithmSet.add(HttpSignAlgorithm.HMAC_SHA256);
	}

	/**
	 * 复制配置。
	 *
	 * @return 配置副本
	 */
	public HttpSignConfig copy() {
		return create()
			.setDefaultAlgorithm(this.defaultAlgorithm)
			.setBodyDigestAlgorithm(this.bodyDigestAlgorithm)
			.setAllowedAlgorithms(this.allowedAlgorithmSet)
			.setSignatureEncoding(this.signatureEncoding)
			.setCharset(this.charset)
			.setTimestampSkewSeconds(this.timestampSkewSeconds)
			.setMaxBodySignBytes(this.maxBodySignBytes)
			.setSignHeaderNames(null == this.signHeaderNames ? null : this.signHeaderNames.copy())
			.setTimestampProvider(this.timestampProvider)
			.setNonceGenerator(this.nonceGenerator)
			.setSignedHeaderNames(this.extraSignedHeaderNameSet)
			.setExcludedHeaderNames(this.excludedHeaderNameSet);
	}

	/**
	 * 获取默认不参与签名的Header名称。
	 *
	 * @return 小写Header名称集合
	 */
	public static Set<String> defaultExcludedHeaderNames() {
		final Set<String> excludedHeaderNames = new LinkedHashSet<>();
		excludedHeaderNames.add("authorization");
		excludedHeaderNames.add("content-length");
		excludedHeaderNames.add("host");
		excludedHeaderNames.add("user-agent");
		excludedHeaderNames.add("connection");
		excludedHeaderNames.add("accept");
		excludedHeaderNames.add("accept-encoding");
		return excludedHeaderNames;
	}

	/**
	 * 获取客户端默认签名算法。
	 *
	 * @return 签名算法
	 */
	public HttpSignAlgorithm getDefaultAlgorithm() {
		return defaultAlgorithm;
	}

	/**
	 * 设置客户端默认签名算法。
	 *
	 * @param defaultAlgorithm 签名算法
	 * @return this
	 */
	public HttpSignConfig setDefaultAlgorithm(final HttpSignAlgorithm defaultAlgorithm) {
		this.defaultAlgorithm = defaultAlgorithm;
		return this;
	}

	/**
	 * 获取Body摘要算法。
	 *
	 * @return Body摘要算法
	 */
	public HttpSignDigestAlgorithm getBodyDigestAlgorithm() {
		return null == bodyDigestAlgorithm ? HttpSignDigestAlgorithm.SHA256 : bodyDigestAlgorithm;
	}

	/**
	 * 设置Body摘要算法。
	 *
	 * @param bodyDigestAlgorithm Body摘要算法
	 * @return this
	 */
	public HttpSignConfig setBodyDigestAlgorithm(final HttpSignDigestAlgorithm bodyDigestAlgorithm) {
		this.bodyDigestAlgorithm = bodyDigestAlgorithm;
		return this;
	}

	/**
	 * 获取服务端允许使用的签名算法集合。
	 *
	 * @return 签名算法集合
	 */
	public Set<HttpSignAlgorithm> getAllowedAlgorithms() {
		return Collections.unmodifiableSet(allowedAlgorithmSet);
	}

	/**
	 * 设置服务端允许使用的签名算法集合。
	 *
	 * @param allowedAlgorithms 签名算法集合
	 * @return this
	 */
	public HttpSignConfig setAllowedAlgorithms(final Set<HttpSignAlgorithm> allowedAlgorithms) {
		this.allowedAlgorithmSet = new LinkedHashSet<>();
		if (null != allowedAlgorithms) {
			this.allowedAlgorithmSet.addAll(allowedAlgorithms);
		}
		return this;
	}

	/**
	 * 设置服务端允许使用的签名算法。
	 *
	 * @param algorithms 签名算法
	 * @return this
	 */
	public HttpSignConfig allowAlgorithms(final HttpSignAlgorithm... algorithms) {
		this.allowedAlgorithmSet.clear();
		if (null != algorithms) {
			Collections.addAll(this.allowedAlgorithmSet, algorithms);
		}
		return this;
	}

	/**
	 * 获取签名输出编码。
	 *
	 * @return 签名输出编码
	 */
	public SignatureEncoding getSignatureEncoding() {
		return null == signatureEncoding ? SignatureEncoding.BASE64 : signatureEncoding;
	}

	/**
	 * 设置签名输出编码。
	 *
	 * @param signatureEncoding 签名输出编码
	 * @return this
	 */
	public HttpSignConfig setSignatureEncoding(final SignatureEncoding signatureEncoding) {
		this.signatureEncoding = signatureEncoding;
		return this;
	}

	/**
	 * 获取签名字符串字符集。
	 *
	 * @return 字符集
	 */
	public Charset getCharset() {
		return null == charset ? CharsetUtil.CHARSET_UTF_8 : charset;
	}

	/**
	 * 设置签名字符串字符集。
	 *
	 * @param charset 字符集
	 * @return this
	 */
	public HttpSignConfig setCharset(final Charset charset) {
		this.charset = charset;
		return this;
	}

	/**
	 * 获取时间戳允许偏移秒数。
	 *
	 * @return 秒数
	 */
	public long getTimestampSkewSeconds() {
		return timestampSkewSeconds;
	}

	/**
	 * 设置时间戳允许偏移秒数。
	 *
	 * @param timestampSkewSeconds 秒数
	 * @return this
	 */
	public HttpSignConfig setTimestampSkewSeconds(final long timestampSkewSeconds) {
		this.timestampSkewSeconds = timestampSkewSeconds;
		return this;
	}

	/**
	 * 获取参与签名的Body最大字节数。
	 *
	 * @return 最大字节数
	 */
	public long getMaxBodySignBytes() {
		return maxBodySignBytes;
	}

	/**
	 * 设置参与签名的Body最大字节数。
	 *
	 * @param maxBodySignBytes 最大字节数
	 * @return this
	 */
	public HttpSignConfig setMaxBodySignBytes(final long maxBodySignBytes) {
		this.maxBodySignBytes = maxBodySignBytes;
		return this;
	}

	/**
	 * 获取签名Header名称配置。
	 *
	 * @return 签名Header名称配置
	 */
	public SignHeaderNames getSignHeaderNames() {
		return null == signHeaderNames ? SignHeaderNames.defaultHeaders() : signHeaderNames;
	}

	/**
	 * 设置签名Header名称配置。
	 *
	 * @param signHeaderNames 签名Header名称配置
	 * @return this
	 */
	public HttpSignConfig setSignHeaderNames(final SignHeaderNames signHeaderNames) {
		this.signHeaderNames = signHeaderNames;
		return this;
	}

	/**
	 * 获取时间戳提供器。
	 *
	 * @return 时间戳提供器
	 */
	public TimestampProvider getTimestampProvider() {
		return null == timestampProvider ? TimestampProvider.SYSTEM : timestampProvider;
	}

	/**
	 * 设置时间戳提供器。
	 *
	 * @param timestampProvider 时间戳提供器
	 * @return this
	 */
	public HttpSignConfig setTimestampProvider(final TimestampProvider timestampProvider) {
		this.timestampProvider = timestampProvider;
		return this;
	}

	/**
	 * 获取Nonce生成器。
	 *
	 * @return Nonce生成器
	 */
	public NonceGenerator getNonceGenerator() {
		return null == nonceGenerator ? NonceGenerator.UUID_GENERATOR : nonceGenerator;
	}

	/**
	 * 设置Nonce生成器。
	 *
	 * @param nonceGenerator Nonce生成器
	 * @return this
	 */
	public HttpSignConfig setNonceGenerator(final NonceGenerator nonceGenerator) {
		this.nonceGenerator = nonceGenerator;
		return this;
	}

	/**
	 * 获取额外参与签名的Header名称。
	 *
	 * @return 小写Header名称集合
	 */
	public Set<String> getSignedHeaderNames() {
		return Collections.unmodifiableSet(extraSignedHeaderNameSet);
	}

	/**
	 * 设置额外参与签名的Header名称。
	 *
	 * @param signedHeaderNames Header名称集合
	 * @return this
	 */
	public HttpSignConfig setSignedHeaderNames(final Set<String> signedHeaderNames) {
		this.extraSignedHeaderNameSet = normalizeHeaderNameSet(signedHeaderNames);
		return this;
	}

	/**
	 * 增加额外参与签名的Header名称。
	 *
	 * @param headerName Header名称
	 * @return this
	 */
	public HttpSignConfig addSignedHeaderName(final String headerName) {
		final String normalizedHeaderName = normalizeHeaderName(headerName);
		if (null != normalizedHeaderName) {
			this.extraSignedHeaderNameSet.add(normalizedHeaderName);
		}
		return this;
	}

	/**
	 * 获取不参与签名的Header名称。
	 *
	 * @return 小写Header名称集合
	 */
	public Set<String> getExcludedHeaderNames() {
		return Collections.unmodifiableSet(excludedHeaderNameSet);
	}

	/**
	 * 设置不参与签名的Header名称。
	 *
	 * @param excludedHeaderNames Header名称集合
	 * @return this
	 */
	public HttpSignConfig setExcludedHeaderNames(final Set<String> excludedHeaderNames) {
		this.excludedHeaderNameSet = normalizeHeaderNameSet(excludedHeaderNames);
		return this;
	}

	/**
	 * 增加不参与签名的Header名称。
	 *
	 * @param headerName Header名称
	 * @return this
	 */
	public HttpSignConfig addExcludedHeaderName(final String headerName) {
		final String normalizedHeaderName = normalizeHeaderName(headerName);
		if (null != normalizedHeaderName) {
			this.excludedHeaderNameSet.add(normalizedHeaderName);
		}
		return this;
	}

	/**
	 * 判断Header是否参与签名。
	 *
	 * @param headerName Header名称
	 * @return 是否参与签名
	 */
	public boolean isSignedHeader(final String headerName) {
		if (null == headerName) {
			return false;
		}
		final String lowerName = headerName.trim().toLowerCase(Locale.ROOT);
		return !isExcludedHeader(lowerName)
			&& (getSignHeaderNames().signedHeaderNameSet().contains(lowerName) || extraSignedHeaderNameSet.contains(lowerName));
	}

	/**
	 * 判断Header是否排除签名。
	 *
	 * @param headerName Header名称
	 * @return 是否排除签名
	 */
	public boolean isExcludedHeader(final String headerName) {
		final String normalizedHeaderName = normalizeHeaderName(headerName);
		return null != normalizedHeaderName && excludedHeaderNameSet.contains(normalizedHeaderName);
	}

	private static Set<String> normalizeHeaderNameSet(final Set<String> rawHeaderNameSet) {
		final Set<String> normalizedHeaderNames = new LinkedHashSet<>();
		if (null != rawHeaderNameSet) {
			for (final String headerName : rawHeaderNameSet) {
				final String normalizedHeaderName = normalizeHeaderName(headerName);
				if (null != normalizedHeaderName) {
					normalizedHeaderNames.add(normalizedHeaderName);
				}
			}
		}
		return normalizedHeaderNames;
	}

	private static String normalizeHeaderName(final String headerName) {
		if (null == headerName) {
			return null;
		}
		if (headerName.indexOf('\r') >= 0 || headerName.indexOf('\n') >= 0) {
			throw new HttpSignException(HttpSignErrorCode.SIGN_CANONICALIZE_FAILED, "Header name contains CR/LF.");
		}
		final String trimmedHeaderName = headerName.trim();
		return trimmedHeaderName.isEmpty() ? null : trimmedHeaderName.toLowerCase(Locale.ROOT);
	}
}
