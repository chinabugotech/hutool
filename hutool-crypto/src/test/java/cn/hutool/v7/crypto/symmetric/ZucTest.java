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

import cn.hutool.v7.core.util.CharsetUtil;
import cn.hutool.v7.core.util.RandomUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ZucTest {

	@Test
	public void zuc128Test(){
		final byte[] secretKey = ZUC.generateKey(ZUC.ZUCAlgorithm.ZUC_128);
		final byte[] iv = RandomUtil.randomBytes(16);
		final ZUC zuc = new ZUC(ZUC.ZUCAlgorithm.ZUC_128, secretKey, iv);

		final String msg = RandomUtil.randomLettersAndNumbersLower(500);
		final byte[] crypt2 = zuc.encrypt(msg);
		final String msg2 = zuc.decryptStr(crypt2, CharsetUtil.UTF_8);
		Assertions.assertEquals(msg, msg2);
	}

	@Test
	public void zuc256Test(){
		final byte[] secretKey = ZUC.generateKey(ZUC.ZUCAlgorithm.ZUC_256);
		final byte[] iv = RandomUtil.randomBytes(25);
		final ZUC zuc = new ZUC(ZUC.ZUCAlgorithm.ZUC_256, secretKey, iv);

		final String msg = RandomUtil.randomLettersAndNumbersLower(500);
		final byte[] crypt2 = zuc.encrypt(msg);
		final String msg2 = zuc.decryptStr(crypt2, CharsetUtil.UTF_8);
		Assertions.assertEquals(msg, msg2);
	}
}
