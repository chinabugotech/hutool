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

import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.math.ec.ECPoint;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.security.KeyPair;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class IssueI99FKETest {
	@Test
	void getSm2KeyTest() {
		final KeyPair pair = KeyUtil.generateKeyPair("SM2");
		final ECPrivateKey aPrivate = (ECPrivateKey) pair.getPrivate();
		final BigInteger d = aPrivate.getD();
		assertNotNull(d);

		final ECPublicKey aPublic = (ECPublicKey) pair.getPublic();
		final ECPoint point = aPublic.getQ();
		final BigInteger x = point.getXCoord().toBigInteger();
		final BigInteger y = point.getYCoord().toBigInteger();
		assertNotNull(x);
		assertNotNull(y);
	}
}
