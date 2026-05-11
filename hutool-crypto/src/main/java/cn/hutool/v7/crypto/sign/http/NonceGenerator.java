package cn.hutool.v7.crypto.sign.http;

import cn.hutool.v7.core.data.id.IdUtil;

import java.util.UUID;

/**
 * Nonce生成器。
 *
 * @author mumu
 * @since 7.0.0
 */
public interface NonceGenerator {

	/**
	 * UUID Nonce生成器。
	 */
	NonceGenerator UUID_GENERATOR = IdUtil::randomUUID7;

	/**
	 * 生成Nonce。
	 *
	 * @return Nonce
	 */
	String generateNonce();
}
