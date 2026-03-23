package cn.hutool.core.date;

import cn.hutool.core.lang.Console;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class IssueIC00HGTest {
	@Test
	@Disabled
	void dateToStringTest(){
		Console.log(DateUtil.date().toString());
	}
}
