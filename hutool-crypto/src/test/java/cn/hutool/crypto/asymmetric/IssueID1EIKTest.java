package cn.hutool.crypto.asymmetric;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.KeyUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.PrivateKey;

public class IssueID1EIKTest {

	@Test
	void rsaTest(){
		// 1. Base64解码
		String str = "PFJTQUtleVZhbHVlPjxNb2R1bHVzPnVscHlkSXJydHJUMzJBSnFDV0FFMHQxNXdHYjBKUTJqSnpBUW1FakpRRzhkcnUrdDhyQUtzekVoNXRRL2x4eTdnMFVMR3dzWjNmekQrdm12d2lKWkx5d1dncmszMDdRbFpXSkU3dWIxM2ZtN2pUa0RLOXM0L294alNabm5JTHcrc0lwVGFoLzdlL2hLNkxEN0VFbzNuTHZZK0VjTzdHa21IYXVCUW5CZmhPaz08L01vZHVsdXM+PEV4cG9uZW50PkFRQUI8L0V4cG9uZW50PjxQPjZlSFdVYUZNdWRTV0svODJPeWxxNHZ2Y0FDbmNHUHYvN1VKWVVETnY1elBZVGE5UFNXUTRzNUk3RHBDTWJYcExLK0VldE5mOUFCZ1ZwVjZERTJlMTR3PT08L1A+PFE+eS9uMkc3d2FYZVlGUnZXWjNROW96NVkyVEpHdUdaSXIzeis3QlVGOWZIckp1Nk9SU2V0YUVkdW5tcjgzSFVNN3E4TGIvWGxtdmVpS0p0OWh2NWx6d3c9PTwvUT48RFA+Sy9IdExTVmJuMGNjZUdQWnNzQVRmMWJIZlpoZjdLbmM2cDJlcm1NYjBadGlOeWFMaFVTNWlyUWRPSjFjWlcybkZqV1VhWEp6N1VLWlBwdEZrYTNZOVE9PTwvRFA+PERRPkJSbm9QTU5VaVhxaU1TY2RSUGtJcndCYnRVaURhU0pOdEpTY2NjSTBpRE50N2lKbUZNb3RBM3RSMHIzcmUvRGRnaXNxWTBsdzkxamtjNXBza0dVZkR3PT08L0RRPjxJbnZlcnNlUT5rVGpLTzBpcXU4M3pTZGpqbWNoT2lYQ0k0bm5veTg5c0JiOFFqMk92TXpnRnhOazhVV1hoT29ZdGVnUDNiVUFhZEJBT3VGSnRCcE1RMmdCemo2ekRWZz09PC9JbnZlcnNlUT48RD5COUhQeDdBa24vQU1EbFpibUxVY3ZyUm9iWGhrZWtHT1BSQzVRWXFjVjBYU1d3clhvNzFiVlpXVU5KbG5hYkhjOUc4clBpRkRIcHVDcGI5Z2JxYitVdmdKRXFrd0t5cU5HSmdnSm9yS1Irb2doWFh3czRuZVVTV1lENnpqbGQvN2U0QlNRM05ScTJGbEFPSEZnRnp3aElhazZwY1pOT2pwazlTUWdSY2ZaSGs9PC9EPjwvUlNBS2V5VmFsdWU+";
		final String xml = Base64.decodeStr(str);

		final PrivateKey privateKey = KeyUtil.generateRSAPrivateKey(xml);
		RSA rsa = new RSA(privateKey, null);
		String decrypt = rsa.decryptStr("tqmp7hGri5WYcZT8bJXJK3SKVlkAx1i1JSpOlOIGB+EAA5OoWS0PtCcWdwLou/qVM28exXKGpmehYbx0Ez0Co8bLHMMnXU3bxp3PXstF2MvrODJoEz+nEzxQ92ngg2n/96Du1rCbwkletYFRO47HpkcEYSTKBsi6NtC98JhUsYSXG15hCJu/I8vOWDF9sB4FCFF9qScpEOUndhctDvAH/UvxBqvSix8mJdL9pyz6Er3zhhQ//4LnI3dQQM0saTq4rZITliTxalT32DRfz0Vj5hNj/So54SspX6fbHjRu0jEaMAotebYZ1Tgpw4AHCYy1DIYoVeGSACd4kc+6ka67gI8jXD7H0tIhI2zyTU3MWQWm2tSOCj+WllELlmCn7ssDp37M6hNO9Imzzj32hWQrsvYsCFufAh+KqRQ1zoF1CQVK8wHRf2ppSFjfR9cCcunpqHqeRrJIpzhJ11dvGZ3JokcjOfDrTNKyXXr7+NVkmc9jPvByEGJXcgkJuX1EHyMv", KeyType.
			PrivateKey);

		String decodeStr = "cpu=178BFBFF00A50F00\r\n" +
			"baseBoard=MP242ML1\r\n" +
			"bios=MP242ML1\r\n" +
			"mac=00:FF:CB:EF:28:18|00:FF:03:A2:FC:D7|C8:94:02:F8:8A:83\r\n" +
			"cusname=123\r\n" +
			"serviceno=12121\r\n" +
			"kcliccount=1\r\n" +
			"cjliccount=1\r\n" +
			"venprintliccount=1\r\n" +
			"beginTime=2025-10-11 14:05:10\r\n" +
			"endTime=2026-10-11 14:05:10\r\n" +
			"lictype=租赁\r\n" +
			"serviceendtime=1\r\n" +
			"validate=1\r\n" +
			"validateunit=年\r\n";
		Assertions.assertEquals(decodeStr, decrypt);
	}
}
