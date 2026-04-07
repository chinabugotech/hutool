package cn.hutool.jwt.signers;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.HMac;

import java.nio.charset.Charset;
import java.security.Key;

/**
 * HMac算法签名实现
 *
 * @author looly
 * @since 5.7.0
 */
public class HMacJWTSigner implements JWTSigner {

	private Charset charset = CharsetUtil.CHARSET_UTF_8;
	private final HMac hMac;
	private final Object lock = new Object();

	/**
	 * 构造
	 *
	 * @param algorithm HMAC签名算法
	 * @param key       密钥
	 */
	public HMacJWTSigner(String algorithm, byte[] key) {
		this.hMac = new HMac(algorithm, key);
	}

	/**
	 * 构造
	 *
	 * @param algorithm HMAC签名算法
	 * @param key       密钥
	 */
	public HMacJWTSigner(String algorithm, Key key) {
		this.hMac = new HMac(algorithm, key);
	}

	/**
	 * 设置编码
	 *
	 * @param charset 编码
	 * @return 编码
	 */
	public HMacJWTSigner setCharset(Charset charset) {
		synchronized (this.lock) {
			this.charset = charset;
		}
		return this;
	}

	@Override
	public String sign(String headerBase64, String payloadBase64) {
		synchronized (this.lock) {
			return hMac.digestBase64(StrUtil.format("{}.{}", headerBase64, payloadBase64), charset, true);
		}
	}

	@Override
	public boolean verify(String headerBase64, String payloadBase64, String signBase64) {
		synchronized (this.lock) {
			final String sign = hMac.digestBase64(StrUtil.format("{}.{}", headerBase64, payloadBase64), charset, true);
			return hMac.verify(
					StrUtil.bytes(sign, charset),
					StrUtil.bytes(signBase64, charset));
		}
	}

	@Override
	public String getAlgorithm() {
		return this.hMac.getAlgorithm();
	}
}
