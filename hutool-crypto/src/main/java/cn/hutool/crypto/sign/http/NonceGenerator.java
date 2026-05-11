package cn.hutool.crypto.sign.http;

import java.util.UUID;

/**
 * Nonce生成器。
 *
 * @author mumu
 * @since 5.8.45
 */
public interface NonceGenerator {

	/**
	 * UUID Nonce生成器。
	 */
	NonceGenerator UUID_GENERATOR = new NonceGenerator() {
		@Override
		public String generateNonce() {
			return UUID.randomUUID().toString().replace("-", "");
		}
	};

	/**
	 * 生成Nonce。
	 *
	 * @return Nonce
	 */
	String generateNonce();
}
