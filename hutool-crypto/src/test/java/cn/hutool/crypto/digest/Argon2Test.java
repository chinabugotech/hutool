package cn.hutool.crypto.digest;

import cn.hutool.core.codec.Base64;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Argon2Test {
	@Test
	public void argon2Test() {
		Argon2 argon2 = new Argon2();
		final byte[] digest = argon2.digest("123456".toCharArray());
		Assertions.assertEquals("wVGMOdzf5EdKGANPeHjaUnaFEJA0BnAq6HcF2psFmFo=", Base64.encode(digest));
	}

	@Test
	public void argon2WithSaltTest() {
		final Argon2 argon2 = new Argon2();
		argon2.setSalt("123456".getBytes());
		final byte[] digest = argon2.digest("123456".toCharArray());
		Assertions.assertEquals("sEpbXTdMWra36JXPVxrZMm3xyoR5GkMlLhtW0Kwp9Ag=", Base64.encode(digest));
	}
}
