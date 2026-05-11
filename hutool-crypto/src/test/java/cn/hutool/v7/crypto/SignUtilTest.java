/*
 * Copyright (c) 2013-2026 Hutool Team.
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

package cn.hutool.v7.crypto;

import cn.hutool.v7.core.codec.binary.Base64;
import cn.hutool.v7.core.io.IoUtil;
import cn.hutool.v7.core.lang.Assert;
import cn.hutool.v7.crypto.asymmetric.Sign;
import cn.hutool.v7.crypto.asymmetric.SignAlgorithm;
import cn.hutool.v7.crypto.bc.PemUtil;
import cn.hutool.v7.crypto.sign.SignUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.PublicKey;

public class SignUtilTest {

	@Test
	void signTest() {
		final Sign sign = SignUtil.sign(SignAlgorithm.SHA256withRSA);

		// 验证base64转换
		final String base64 = KeyUtil.toBase64(sign.getPublicKey());
		final PublicKey publicKey = KeyUtil.generatePublicKey(SignAlgorithm.SHA256withRSA.getValue(), Base64.decode(base64));
		Assertions.assertEquals(sign.getPublicKey(), publicKey);

		final String signHex = sign.signHex("abcd".getBytes(StandardCharsets.UTF_8));
		Assert.notNull(signHex);
	}

	@Test
	void issueI6SRUFTest() {
		// pem格式
		final String publicKey = """
			-----BEGIN CERTIFICATE-----
			MIIEMTCCAxmgAwIBAgIGAXRTgcMnMA0GCSqGSIb3DQEBCwUAMHYxCzAJBgNVBAYT
			AkNOMRAwDgYDVQQIDAdCZWlKaW5nMRAwDgYDVQQHDAdCZWlKaW5nMRcwFQYDVQQK
			DA5MYWthbGEgQ28uLEx0ZDEqMCgGA1UEAwwhTGFrYWxhIE9yZ2FuaXphdGlvbiBW
			YWxpZGF0aW9uIENBMB4XDTIwMTAxMDA1MjQxNFoXDTMwMTAwODA1MjQxNFowZTEL
			MAkGA1UEBhMCQ04xEDAOBgNVBAgMB0JlaUppbmcxEDAOBgNVBAcMB0JlaUppbmcx
			FzAVBgNVBAoMDkxha2FsYSBDby4sTHRkMRkwFwYDVQQDDBBBUElHVy5MQUtBTEEu
			Q09NMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAt1zHL54HiI8d2sLJ
			lwoQji3/ln0nsvfZ/XVpOjuB+1YR6/0LdxEDMC/hxI6iH2Rm5MjwWz3dmN/6BZeI
			gwGeTOWJUZFARo8UduKrlhC6gWMRpAiiGC8wA8stikc5gYB+UeFVZi/aJ0WN0cpP
			JYCvPBhxhMvhVDnd4hNohnR1L7k0ypuWg0YwGjC25FaNAEFBYP9EYUyCJjE//9Z7
			sMzHR9SJYCqqo6r9bOH9G6sWKuEp+osuAh+kJIxJMHfipw7w3tEcWG0hce9u/el4
			cYJtg8/PPMVoccKmeCzMvarr7jdKP4lenJbtwlgyfs+JgNu60KMUJH8RS72wC9NY
			uFz09wIDAQABo4HVMIHSMIGSBgNVHSMEgYowgYeAFCnH4DkZPR6CZxRn/kIqVsMo
			dJHpoWekZTBjMQswCQYDVQQGEwJDTjEQMA4GA1UECAwHQmVpSmluZzEQMA4GA1UE
			BwwHQmVpSmluZzEXMBUGA1UECgwOTGFrYWxhIENvLixMdGQxFzAVBgNVBAMMDkxh
			a2FsYSBSb290IENBggYBaiUALIowHQYDVR0OBBYEFJ2Kx9YZfmWpkKFnC33C0r5D
			K3rFMAwGA1UdEwEB/wQCMAAwDgYDVR0PAQH/BAQDAgeAMA0GCSqGSIb3DQEBCwUA
			A4IBAQBZoeU0XyH9O0LGF9R+JyGwfU/O5amoB97VeM+5n9v2z8OCiIJ8eXVGKN9L
			tl9QkpTEanYwK30KkpHcJP1xfVkhPi/cCMgfTWQ5eKYC7Zm16zk7n4CP6IIgZIqm
			TVGsIGKk8RzWseyWPB3lfqMDR52V1tdA1S8lJ7a2Xnpt5M2jkDXoArl3SVSwCb4D
			AmThYhak48M++fUJNYII9JBGRdRGbfJ2GSFdPXgesUL2CwlReQwbW4GZkYGOg9LK
			CNPK6XShlNdvgPv0CCR08KCYRwC3HZ0y1F0NjaKzYdGNPrvOq9lA495ONZCvzYDo
			gmsu/kd6eqxTs/JwdaIYr4sCMg8Z
			-----END CERTIFICATE-----""";

		final PublicKey key = (PublicKey) PemUtil.readPemKey(IoUtil.toUtf8Stream(publicKey));
		new Sign(SignAlgorithm.SHA256withRSA, new KeyPair(key, null));
	}
}
