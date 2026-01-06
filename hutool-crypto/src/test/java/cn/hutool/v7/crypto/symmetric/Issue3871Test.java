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

package cn.hutool.v7.crypto.symmetric;

import cn.hutool.v7.crypto.Mode;
import cn.hutool.v7.crypto.SecureUtil;
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
