package cn.hutool.core.convert;

import cn.hutool.core.date.TimeZoneUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StringConvertTest {

	@Test
	public void timezoneToStrTest(){
		final String s = Convert.toStr(TimeZoneUtil.getTimeZone("Asia/Shanghai"));
		assertEquals("Asia/Shanghai", s);
	}
}
