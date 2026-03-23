package cn.hutool.bloomfilter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BitSetBloomFilterTest {

	@Test
	public void testConstructorWithInvalidParameters() {
		// 测试参数 c 的无效情况（c <= 0）
		assertThrows(IllegalArgumentException.class, () -> {
			new BitSetBloomFilter(0, 100, 3);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			new BitSetBloomFilter(-5, 100, 3);
		});
		// 测试参数 n 的无效情况 (n <= 0)
		assertThrows(IllegalArgumentException.class, () -> {
			new BitSetBloomFilter(200, 0, 3);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			new BitSetBloomFilter(200, -10, 3);
		});
		// 测试参数 k 的无效情况（k < 1 或 k > 8）
		assertThrows(IllegalArgumentException.class, () -> {
			new BitSetBloomFilter(200, 100, 0);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			new BitSetBloomFilter(200, 100, 9);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			new BitSetBloomFilter(200, 100, -2);
		});
	}
}
