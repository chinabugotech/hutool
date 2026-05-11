package cn.hutool.v7.crypto.sign.http;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 访问密钥信息，由业务系统通过{@link SecretProvider}提供。
 * <p>
 * 本类只承载验签所需的密钥状态，不负责密钥生成、加密存储、轮换和权限校验。
 *
 * @author mumu
 * @since 7.0.0
 */
public class SecretInfo {

	/**
	 * 访问身份ID。
	 */
	private String accessKeyId;
	/**
	 * 访问密钥明文或业务解密后的密钥。
	 */
	private String accessKeySecret;
	/**
	 * 密钥是否启用。
	 */
	private boolean enabled = true;
	/**
	 * 此密钥允许使用的签名算法，为空表示跟随全局配置。
	 */
	private Set<HttpSignAlgorithm> allowedAlgorithmSet;
	/**
	 * 密钥过期时间，Unix秒。
	 */
	private Long expireAt;

	/**
	 * 创建密钥信息。
	 *
	 * @param accessKeyId     访问身份ID
	 * @param accessKeySecret 访问密钥
	 * @return 密钥信息
	 */
	public static SecretInfo of(final String accessKeyId, final String accessKeySecret) {
		return new SecretInfo().setAccessKeyId(accessKeyId).setAccessKeySecret(accessKeySecret);
	}

	/**
	 * 获取访问身份ID。
	 *
	 * @return 访问身份ID
	 */
	public String getAccessKeyId() {
		return accessKeyId;
	}

	/**
	 * 设置访问身份ID。
	 *
	 * @param accessKeyId 访问身份ID
	 * @return this
	 */
	public SecretInfo setAccessKeyId(final String accessKeyId) {
		this.accessKeyId = accessKeyId;
		return this;
	}

	/**
	 * 获取访问密钥。
	 *
	 * @return 访问密钥
	 */
	public String getAccessKeySecret() {
		return accessKeySecret;
	}

	/**
	 * 设置访问密钥。
	 *
	 * @param accessKeySecret 访问密钥
	 * @return this
	 */
	public SecretInfo setAccessKeySecret(final String accessKeySecret) {
		this.accessKeySecret = accessKeySecret;
		return this;
	}

	/**
	 * 判断密钥是否启用。
	 *
	 * @return 是否启用
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * 设置密钥是否启用。
	 *
	 * @param enabled 是否启用
	 * @return this
	 */
	public SecretInfo setEnabled(final boolean enabled) {
		this.enabled = enabled;
		return this;
	}

	/**
	 * 获取此密钥允许使用的签名算法。
	 *
	 * @return 签名算法集合
	 */
	public Set<HttpSignAlgorithm> getAllowedAlgorithms() {
		return null == allowedAlgorithmSet ? Collections.<HttpSignAlgorithm>emptySet() : Collections.unmodifiableSet(allowedAlgorithmSet);
	}

	/**
	 * 设置此密钥允许使用的签名算法。
	 *
	 * @param allowedAlgorithms 签名算法集合
	 * @return this
	 */
	public SecretInfo setAllowedAlgorithms(final Set<HttpSignAlgorithm> allowedAlgorithms) {
		this.allowedAlgorithmSet = null == allowedAlgorithms ? null : new LinkedHashSet<>(allowedAlgorithms);
		return this;
	}

	/**
	 * 增加此密钥允许使用的签名算法。
	 *
	 * @param algorithms 签名算法
	 * @return this
	 */
	public SecretInfo allowAlgorithms(final HttpSignAlgorithm... algorithms) {
		if (null == this.allowedAlgorithmSet) {
			this.allowedAlgorithmSet = new LinkedHashSet<>();
		}
		if (null != algorithms) {
			Collections.addAll(this.allowedAlgorithmSet, algorithms);
		}
		return this;
	}

	/**
	 * 获取密钥过期时间。
	 *
	 * @return Unix秒时间戳
	 */
	public Long getExpireAt() {
		return expireAt;
	}

	/**
	 * 设置密钥过期时间。
	 *
	 * @param expireAt Unix秒时间戳
	 * @return this
	 */
	public SecretInfo setExpireAt(final Long expireAt) {
		this.expireAt = expireAt;
		return this;
	}

}
