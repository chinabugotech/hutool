package cn.hutool.crypto.sign.http;

/**
 * Nonce存储，用于防重放。
 *
 * @author mumu
 * @since 5.8.45
 */
public interface NonceStore {

	/**
	 * 在指定TTL内写入Nonce，若已存在返回{@code false}。
	 *
	 * @param accessKeyId 访问身份ID
	 * @param nonce       Nonce
	 * @param ttlSeconds  过期秒数
	 * @return 是否写入成功
	 */
	boolean putIfAbsent(String accessKeyId, String nonce, long ttlSeconds);
}
