package cn.hutool.v7.crypto.sign.http;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 内存Nonce存储，适合单机测试或单机应用。
 * <p>
 * 该实现基于{@link ConcurrentHashMap}，单实例可被多线程并发调用。分布式部署应自行实现
 * {@link NonceStore}并使用Redis等共享存储。
 *
 * @author mumu
 * @since 7.0.0
 */
public class MemoryNonceStore implements NonceStore {

	/**
	 * Nonce键与过期时间映射，过期时间单位为毫秒。
	 */
	private final ConcurrentHashMap<String, Long> nonceMap = new ConcurrentHashMap<>();

	/**
	 * 创建内存Nonce存储。
	 *
	 * @return 内存Nonce存储
	 */
	public static MemoryNonceStore create() {
		return new MemoryNonceStore();
	}

	@Override
	public boolean putIfAbsent(final String accessKeyId, final String nonce, final long ttlSeconds) {
		final long now = System.currentTimeMillis();
		final String nonceKey = accessKeyId + ':' + nonce;
		final long expireAt = now + Math.max(1L, ttlSeconds) * 1000L;
		final Long existsExpireAt = nonceMap.putIfAbsent(nonceKey, expireAt);
		if (null == existsExpireAt) {
			cleanExpired(now);
			return true;
		}
		return existsExpireAt <= now && nonceMap.replace(nonceKey, existsExpireAt, expireAt);
	}

	/**
	 * 清理已过期的Nonce。
	 *
	 * @param now 当前时间戳，单位毫秒
	 */
	private void cleanExpired(final long now) {
		nonceMap.entrySet().removeIf(entry -> entry.getValue() <= now);
	}
}
