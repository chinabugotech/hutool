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

package cn.hutool.v7.crypto.asymmetric;

import cn.hutool.v7.core.codec.binary.Base64;
import cn.hutool.v7.crypto.KeyUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.PrivateKey;

public class IssueID1EIKTest {
	@Test
	void rsaTest(){
		// 1. Base64解码
		final String keyXmlBase64Str = "PFJTQUtleVZhbHVlPjxNb2R1bHVzPnVscHlkSXJydHJUMzJBSnFDV0FFMHQxNXdHYjBKUTJqSnpBUW1FakpRRzhkcnUr" +
			"dDhyQUtzekVoNXRRL2x4eTdnMFVMR3dzWjNmekQrdm12d2lKWkx5d1dncmszMDdRbFpXSkU3dWIxM2ZtN2pUa0RLOXM0L294alNabm5JTHc" +
			"rc0lwVGFoLzdlL2hLNkxEN0VFbzNuTHZZK0VjTzdHa21IYXVCUW5CZmhPaz08L01vZHVsdXM+PEV4cG9uZW50PkFRQUI8L0V4cG9uZW50Pj" +
			"xQPjZlSFdVYUZNdWRTV0svODJPeWxxNHZ2Y0FDbmNHUHYvN1VKWVVETnY1elBZVGE5UFNXUTRzNUk3RHBDTWJYcExLK0VldE5mOUFCZ1ZwV" +
			"jZERTJlMTR3PT08L1A+PFE+eS9uMkc3d2FYZVlGUnZXWjNROW96NVkyVEpHdUdaSXIzeis3QlVGOWZIckp1Nk9SU2V0YUVkdW5tcjgzSFVN" +
			"N3E4TGIvWGxtdmVpS0p0OWh2NWx6d3c9PTwvUT48RFA+Sy9IdExTVmJuMGNjZUdQWnNzQVRmMWJIZlpoZjdLbmM2cDJlcm1NYjBadGlOeWF" +
			"MaFVTNWlyUWRPSjFjWlcybkZqV1VhWEp6N1VLWlBwdEZrYTNZOVE9PTwvRFA+PERRPkJSbm9QTU5VaVhxaU1TY2RSUGtJcndCYnRVaURhU0" +
			"pOdEpTY2NjSTBpRE50N2lKbUZNb3RBM3RSMHIzcmUvRGRnaXNxWTBsdzkxamtjNXBza0dVZkR3PT08L0RRPjxJbnZlcnNlUT5rVGpLTzBpc" +
			"XU4M3pTZGpqbWNoT2lYQ0k0bm5veTg5c0JiOFFqMk92TXpnRnhOazhVV1hoT29ZdGVnUDNiVUFhZEJBT3VGSnRCcE1RMmdCemo2ekRWZz09" +
			"PC9JbnZlcnNlUT48RD5COUhQeDdBa24vQU1EbFpibUxVY3ZyUm9iWGhrZWtHT1BSQzVRWXFjVjBYU1d3clhvNzFiVlpXVU5KbG5hYkhjOUc" +
			"4clBpRkRIcHVDcGI5Z2JxYitVdmdKRXFrd0t5cU5HSmdnSm9yS1Irb2doWFh3czRuZVVTV1lENnpqbGQvN2U0QlNRM05ScTJGbEFPSEZnRn" +
			"p3aElhazZwY1pOT2pwazlTUWdSY2ZaSGs9PC9EPjwvUlNBS2V5VmFsdWU+";
		final String keyXmlStr = Base64.decodeStr(keyXmlBase64Str);

		final PrivateKey privateKey = KeyUtil.generateRSAPrivateKey(keyXmlStr);
		final RSA rsa = new RSA(privateKey, null);

		final String encryptStr = "tqmp7hGri5WYcZT8bJXJK3SKVlkAx1i1JSpOlOIGB+EAA5OoWS0PtCcWdwLou/qVM28e" +
			"xXKGpmehYbx0Ez0Co8bLHMMnXU3bxp3PXstF2MvrODJoEz+nEzxQ92ngg2n/96Du1rCbwkletYFRO47HpkcEYSTKBsi6NtC98JhUsYSXG15" +
			"hCJu/I8vOWDF9sB4FCFF9qScpEOUndhctDvAH/UvxBqvSix8mJdL9pyz6Er3zhhQ//4LnI3dQQM0saTq4rZITliTxalT32DRfz0Vj5hNj/S" +
			"o54SspX6fbHjRu0jEaMAotebYZ1Tgpw4AHCYy1DIYoVeGSACd4kc+6ka67gI8jXD7H0tIhI2zyTU3MWQWm2tSOCj+WllELlmCn7ssDp37M6" +
			"hNO9Imzzj32hWQrsvYsCFufAh+KqRQ1zoF1CQVK8wHRf2ppSFjfR9cCcunpqHqeRrJIpzhJ11dvGZ3JokcjOfDrTNKyXXr7+NVkmc9jPvByEGJXcgkJuX1EHyMv";
		final String decrypt = rsa.decryptStr(encryptStr, KeyType.PrivateKey);

		final String expectedDecodeStr = """
			cpu=178BFBFF00A50F00\r
			baseBoard=MP242ML1\r
			bios=MP242ML1\r
			mac=00:FF:CB:EF:28:18|00:FF:03:A2:FC:D7|C8:94:02:F8:8A:83\r
			cusname=123\r
			serviceno=12121\r
			kcliccount=1\r
			cjliccount=1\r
			venprintliccount=1\r
			beginTime=2025-10-11 14:05:10\r
			endTime=2026-10-11 14:05:10\r
			lictype=租赁\r
			serviceendtime=1\r
			validate=1\r
			validateunit=年\r
			""";

		Assertions.assertEquals(expectedDecodeStr, decrypt);
	}
}
