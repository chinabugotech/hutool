package cn.hutool.core.date;

import cn.hutool.core.date.chinese.GanZhi;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class GanzhiTest {

	@Test
	public void getGanzhiOfYearTest(){
		assertEquals("庚子", GanZhi.getGanzhiOfYear(2020));
	}

	@Test
	public void getCyclicalYMDTest(){
		//通过公历构建
		ChineseDate chineseDate = new ChineseDate(DateUtil.parseDate("1993-01-06"));
		String cyclicalYMD = chineseDate.getCyclicalYMD();
		assertEquals("壬申年癸丑月丁亥日",cyclicalYMD);
	}

	@Test
	public void getCyclicalYMDTest2(){
		//通过农历构建
		ChineseDate chineseDate = new ChineseDate(1992,12,14);
		String cyclicalYMD = chineseDate.getCyclicalYMD();
		assertEquals("壬申年癸丑月丁亥日",cyclicalYMD);
	}

	@Test
	public void getCyclicalYMDTest3(){
		//通过公历构建
		ChineseDate chineseDate = new ChineseDate(DateUtil.parseDate("2020-08-28"));
		String cyclicalYMD = chineseDate.getCyclicalYMD();
		assertEquals("庚子年甲申月癸卯日",cyclicalYMD);
	}

	@Test
	public void getCyclicalYMDTest4(){
		//通过公历构建
		ChineseDate chineseDate = new ChineseDate(DateUtil.parseDate("1905-08-28"));
		String cyclicalYMD = chineseDate.getCyclicalYMD();
		assertEquals("乙巳年甲申月己亥日",cyclicalYMD);
	}
	@Test
	public void lunarYearStartsAtSpringFestivalTest() {
		assertEquals("癸卯", new ChineseDate(LocalDate.of(2024, 2, 4)).getCyclical());
		assertEquals("癸卯", new ChineseDate(LocalDate.of(2024, 2, 9)).getCyclical());
		assertEquals("甲辰", new ChineseDate(LocalDate.of(2024, 2, 10)).getCyclical());
		assertEquals("甲辰", new ChineseDate(LocalDate.of(2025, 1, 28)).getCyclical());
		assertEquals("乙巳", new ChineseDate(LocalDate.of(2025, 1, 29)).getCyclical());
		assertEquals("乙巳", new ChineseDate(LocalDate.of(2025, 2, 3)).getCyclical());
	}

	@Test
	public void monthChangesOnJieDateTest() {
		assertEquals("甲子", GanZhi.getGanzhiOfMonth(2024, 1, 5));
		assertEquals("乙丑", GanZhi.getGanzhiOfMonth(2024, 1, 6));
		assertEquals("乙丑", GanZhi.getGanzhiOfMonth(2024, 2, 3));
		assertEquals("丙寅", GanZhi.getGanzhiOfMonth(2024, 2, 4));
		assertEquals("丁丑", GanZhi.getGanzhiOfMonth(2025, 2, 2));
		assertEquals("戊寅", GanZhi.getGanzhiOfMonth(2025, 2, 3));
	}

	@Test
	public void dayFollowsCivilDateTest() {
		assertEquals("甲子", GanZhi.getGanzhiOfDay(1949, 10, 1));
		assertEquals("己亥", GanZhi.getGanzhiOfDay(2024, 2, 5));
		assertEquals("庚子", GanZhi.getGanzhiOfDay(2024, 2, 6));
	}

	@Test
	public void combinedFormatKeepsLunarYearTest() {
		assertEquals("癸卯年丙寅月己亥日",
			new ChineseDate(LocalDate.of(2024, 2, 5)).getCyclicalYMD());
		assertEquals("乙巳年丁丑月辛丑日",
			new ChineseDate(LocalDate.of(2025, 2, 1)).getCyclicalYMD());
	}

}
