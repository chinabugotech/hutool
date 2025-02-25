package org.dromara.hutool.crypto.symmetric;

import org.dromara.hutool.crypto.Mode;
import org.dromara.hutool.crypto.SecureUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * https://tool.hiofd.com/sm4-encrypt-online/
 */
public class Issue3871Test {
	@Test
	void sm4Test(){
		final String data = "{\"mobile\":\"13333333333\"}";
		final byte[] key = SecureUtil.decode("Q5v/nsxmaCdFiltxIz+TCQ==");
		final byte[] iv = SecureUtil.decode("Dz15D//HqTvavbckOWgKiw==");

		final SM4 sm4 = new SM4(Mode.CBC.name(), "PKCS5Padding", key, iv);
		final String encryptBase64 = sm4.encryptBase64(data);
		Assertions.assertEquals("T6J7tGSQXam0BPauZIamqd5lJ/+I6q42vlziP3HBtT8=", encryptBase64);
		Assertions.assertEquals(data, sm4.decryptStr(encryptBase64));
	}
}
