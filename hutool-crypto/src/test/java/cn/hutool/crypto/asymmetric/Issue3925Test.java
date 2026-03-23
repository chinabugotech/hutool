package cn.hutool.crypto.asymmetric;

import org.bouncycastle.crypto.DataLengthException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Issue3925Test {
	@Test
	void sm2Test() {
		final SM2 sm2 = new SM2();
		Assertions.assertThrows(DataLengthException.class, ()->sm2.encrypt("", KeyType.PublicKey));
	}
}
