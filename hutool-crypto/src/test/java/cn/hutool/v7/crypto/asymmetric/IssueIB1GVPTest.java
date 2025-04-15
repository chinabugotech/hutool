/*
 * Copyright (c) 2025 Hutool Team and hutool.cn
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

import cn.hutool.v7.core.text.StrUtil;
import cn.hutool.v7.core.util.ByteUtil;
import cn.hutool.v7.core.util.CharsetUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IssueIB1GVPTest {

	/**
	 * https://stackoverflow.com/questions/50298687/bouncy-castle-vs-java-default-rsa-with-oaep
	 */
	@Test
	void rsaOaepTest() {
		final RSA rsa = new RSA("RSA/NONE/OAEPWithSHA256AndMGF1Padding");
		// 公钥加密，私钥解密
		final byte[] encrypt = rsa.encrypt(ByteUtil.toBytes("我是一段测试aaaa", CharsetUtil.UTF_8), KeyType.PublicKey);
		final byte[] decrypt = rsa.decrypt(encrypt, KeyType.PrivateKey);
		assertEquals("我是一段测试aaaa", StrUtil.str(decrypt, CharsetUtil.UTF_8));

		// 私钥加密，公钥解密
		final byte[] encrypt2 = rsa.encrypt(ByteUtil.toBytes("我是一段测试aaaa", CharsetUtil.UTF_8), KeyType.PrivateKey);
		final byte[] decrypt2 = rsa.decrypt(encrypt2, KeyType.PublicKey);
		assertEquals("我是一段测试aaaa", StrUtil.str(decrypt2, CharsetUtil.UTF_8));
	}
}
