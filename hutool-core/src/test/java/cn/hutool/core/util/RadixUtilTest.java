package cn.hutool.core.util;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class RadixUtilTest {
	@Test
	public void issueIDFPGRTest() {
		String radixs = "0123456789ABC"; // base 13
		String bad = "1X3"; // 'X' 不在 radix 中
		assertThrows(IllegalArgumentException.class, () -> {
			RadixUtil.decode(radixs, bad);
		});
	}
}
