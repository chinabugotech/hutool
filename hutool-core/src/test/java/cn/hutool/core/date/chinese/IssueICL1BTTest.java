package cn.hutool.core.date.chinese;

import cn.hutool.core.date.ChineseDate;
import cn.hutool.core.date.DateUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class IssueICL1BTTest {
	@Test
	void getFestivalsTest(){
		String date = "2025-07-31";
		Date productionDate = DateUtil.parseDate( date);
		ChineseDate chineseDate = new ChineseDate(productionDate);
		System.out.println(chineseDate.isLeapMonth());
		Assertions.assertTrue(chineseDate.isLeapMonth());
		String festivals = chineseDate.getFestivals();
		Assertions.assertEquals("", festivals);
	}
}
