package cn.hutool.crypto.sign.http;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.digest.DigestUtil;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * HTTP AK/SK签名器。
 * <p>
 * 签名器构造时会复制{@link HttpSignConfig}配置快照。只要自定义的{@link TimestampProvider}
 * 和{@link NonceGenerator}本身可并发使用，签名器实例可在多个线程中复用。
 *
 * @author mumu
 * @since 5.8.45
 */
public class HttpSigner {

	private final HttpSignConfig config;
	private final HttpSignCanonicalizer canonicalizer;

	/**
	 * 创建签名器。
	 *
	 * @param config 签名配置
	 * @return 签名器
	 */
	public static HttpSigner create(final HttpSignConfig config) {
		return new HttpSigner(config);
	}

	/**
	 * 使用默认配置签名。
	 *
	 * @param request     请求
	 * @param credentials 凭证
	 * @return 签名结果
	 */
	public static HttpSignResult sign(final HttpSignRequest request, final AkSkCredentials credentials) {
		return create(HttpSignConfig.create()).signRequest(request, credentials);
	}

	/**
	 * 使用指定配置签名。
	 *
	 * @param request     请求
	 * @param credentials 凭证
	 * @param config      签名配置
	 * @return 签名结果
	 */
	public static HttpSignResult sign(final HttpSignRequest request, final AkSkCredentials credentials, final HttpSignConfig config) {
		return create(config).signRequest(request, credentials);
	}

	/**
	 * 构造。
	 *
	 * @param config 签名配置
	 */
	public HttpSigner(final HttpSignConfig config) {
		this.config = null == config ? HttpSignConfig.create() : config.copy();
		this.canonicalizer = new HttpSignCanonicalizer();
	}

	/**
	 * 签名请求。
	 *
	 * @param request     请求
	 * @param credentials 凭证
	 * @return 签名结果
	 */
	public HttpSignResult signRequest(final HttpSignRequest request, final AkSkCredentials credentials) {
		final SignHeaderNames signHeaderNames = config.getSignHeaderNames();
		final HttpSignAlgorithm signAlgorithm = null == config.getDefaultAlgorithm() ? HttpSignAlgorithm.HMAC_SHA256 : config.getDefaultAlgorithm();
		final Map<String, String> signatureHeaders = new LinkedHashMap<>();
		signatureHeaders.put(signHeaderNames.getAccessKeyIdHeaderName(), credentials.getAccessKeyId());
		signatureHeaders.put(signHeaderNames.getTimestampHeaderName(), String.valueOf(config.getTimestampProvider().currentTimestamp()));
		signatureHeaders.put(signHeaderNames.getNonceHeaderName(), config.getNonceGenerator().generateNonce());
		signatureHeaders.put(signHeaderNames.getAlgorithmHeaderName(), signAlgorithm.getAlgorithmName());

		final HttpSignRequest preparedRequest = request.copy();
		for (final Map.Entry<String, String> entry : signatureHeaders.entrySet()) {
			preparedRequest.setHeader(entry.getKey(), entry.getValue());
		}

		final HttpSignResult signResult = signPrepared(preparedRequest, credentials.getAccessKeySecret(), signAlgorithm, config, canonicalizer);
		signatureHeaders.put(signHeaderNames.getSignatureHeaderName(), signResult.getSignature());
		return new HttpSignResult(signatureHeaders, signResult.getSignature(), signResult.getCanonicalRequest(), signResult.getStringToSign());
	}

	/**
	 * 对已经包含签名相关Header的请求计算签名。
	 *
	 * @param request             签名请求
	 * @param accessKeySecret     访问密钥
	 * @param algorithm           签名算法
	 * @param config              签名配置
	 * @param canonicalizer       规范化工具
	 * @return 签名结果
	 */
	static HttpSignResult signPrepared(final HttpSignRequest request, final String accessKeySecret,
									   final HttpSignAlgorithm algorithm, final HttpSignConfig config,
									   final HttpSignCanonicalizer canonicalizer) {
		final CanonicalRequest canonicalRequest = canonicalizer.canonicalize(request, config);
		final String stringToSign = canonicalRequest.toStringToSign();
		final byte[] signBytes = DigestUtil.hmac(algorithm.toHmacAlgorithm(), accessKeySecret.getBytes(config.getCharset()))
			.digest(stringToSign, config.getCharset());
		final String signature = encodeSignature(signBytes, config.getSignatureEncoding());
		return new HttpSignResult(new LinkedHashMap<>(), signature, canonicalRequest, stringToSign);
	}

	/**
	 * 按配置编码签名字节。
	 *
	 * @param signBytes 签名字节
	 * @param encoding  输出编码
	 * @return 签名字符串
	 */
	static String encodeSignature(final byte[] signBytes, final SignatureEncoding encoding) {
		if (SignatureEncoding.HEX.equals(encoding)) {
			return HexUtil.encodeHexStr(signBytes);
		}
		return Base64.encode(signBytes);
	}
}
