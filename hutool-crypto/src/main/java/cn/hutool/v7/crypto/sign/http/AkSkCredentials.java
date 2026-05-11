package cn.hutool.v7.crypto.sign.http;

import cn.hutool.v7.core.lang.Assert;

/**
 * AK/SK凭证。
 *
 * @author mumu
 * @since 7.0.0
 */
public class AkSkCredentials {

	private final String accessKeyId;
	private final String accessKeySecret;

	/**
	 * 构造AK/SK凭证。
	 *
	 * @param accessKeyId     访问身份ID
	 * @param accessKeySecret 访问密钥
	 * @return AK/SK凭证
	 */
	public static AkSkCredentials of(final String accessKeyId, final String accessKeySecret) {
		return ofAccessKey(accessKeyId, accessKeySecret);
	}

	/**
	 * 构造AK/SK凭证。
	 *
	 * @param accessKeyId     访问身份ID
	 * @param accessKeySecret 访问密钥
	 * @return AK/SK凭证
	 */
	public static AkSkCredentials ofAccessKey(final String accessKeyId, final String accessKeySecret) {
		return new AkSkCredentials(accessKeyId, accessKeySecret);
	}

	/**
	 * 通过AppId/AppSecret别名构造凭证。
	 *
	 * @param appId     应用ID
	 * @param appSecret 应用密钥
	 * @return AK/SK凭证
	 */
	public static AkSkCredentials ofAppIdAndAppSecret(final String appId, final String appSecret) {
		return new AkSkCredentials(appId, appSecret);
	}

	/**
	 * 构造。
	 *
	 * @param accessKeyId     访问身份ID
	 * @param accessKeySecret 访问密钥
	 */
	public AkSkCredentials(final String accessKeyId, final String accessKeySecret) {
		Assert.notBlank(accessKeyId, "AccessKeyId must be not blank!");
		Assert.notBlank(accessKeySecret, "AccessKeySecret must be not blank!");
		this.accessKeyId = accessKeyId;
		this.accessKeySecret = accessKeySecret;
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
	 * 获取访问密钥。
	 *
	 * @return 访问密钥
	 */
	public String getAccessKeySecret() {
		return accessKeySecret;
	}
}
