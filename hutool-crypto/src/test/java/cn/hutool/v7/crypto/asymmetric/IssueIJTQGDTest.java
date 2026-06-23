/*
 * Copyright (c) 2026 Hutool Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.hutool.v7.crypto.asymmetric;

import cn.hutool.v7.crypto.SecureUtil;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IssueIJTQGDTest {
	@Test
	public void testDecryptWithOnlyPrivateKey() {
		// 测试只传入私钥（第二个参数为null）的场景
		final RSA rsaGenerator = new RSA();
		final String privateKeyBase64 = rsaGenerator.getPrivateKeyBase64();
		final String publicKeyBase64 = rsaGenerator.getPublicKeyBase64();

		final String originalText = "仅使用私钥解密测试";

		// 使用公钥加密
		final RSA rsaEncryptor = new RSA(null, publicKeyBase64);
		final String encryptedBase64 = rsaEncryptor.encryptBase64(originalText, KeyType.PublicKey);

		// 使用您的代码方式解密（只传私钥，公钥为null）
		final RSA rsa = SecureUtil.rsa(privateKeyBase64, null);
		final String decryptedText = rsa.decryptStr(encryptedBase64, KeyType.PrivateKey, StandardCharsets.UTF_8);

		assertEquals(originalText, decryptedText);
	}
}
