package cn.hutool.crypto.sign.http;

/**
 * 访问密钥提供器。
 *
 * @author mumu
 * @since 5.8.45
 */
public interface SecretProvider {

	/**
	 * 根据访问身份ID加载密钥信息。
	 *
	 * @param accessKeyId 访问身份ID
	 * @return 密钥信息，未找到返回{@code null}
	 */
	SecretInfo loadSecret(String accessKeyId);
}
