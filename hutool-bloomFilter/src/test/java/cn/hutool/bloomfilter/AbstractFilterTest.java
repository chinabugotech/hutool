package cn.hutool.bloomfilter;

import cn.hutool.bloomfilter.bitMap.BitMap;
import cn.hutool.bloomfilter.filter.DefaultFilter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class AbstractFilterTest {

	@Test
	void testInitWhenMaxValueLessThanMachineNum() {
		assertDoesNotThrow(() -> {
			DefaultFilter filter = new DefaultFilter(1, BitMap.MACHINE32);
			filter.add("init");
		}, "maxValue=1且machineNum=32时add应无异常");
		assertDoesNotThrow(() -> {
			DefaultFilter filter = new DefaultFilter(31, BitMap.MACHINE32);
			filter.add("init");
		}, "maxValue=31且machineNum=32时add应无异常");
		assertDoesNotThrow(() -> {
			DefaultFilter filter = new DefaultFilter(1, BitMap.MACHINE64);
			filter.add("init");
		}, "maxValue=1且machineNum=64时add应无异常");
		assertDoesNotThrow(() -> {
			DefaultFilter filter = new DefaultFilter(63, BitMap.MACHINE64);
			filter.add("init");
		}, "maxValue=63且machineNum=64时add应无异常");
	}
}
