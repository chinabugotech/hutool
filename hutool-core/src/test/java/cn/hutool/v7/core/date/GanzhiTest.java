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

package cn.hutool.v7.core.date;

import cn.hutool.v7.core.date.chinese.ChineseDate;
import cn.hutool.v7.core.date.chinese.ChineseDateFormat;
import cn.hutool.v7.core.date.chinese.GanZhi;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import java.util.Objects;

public class GanzhiTest {

	@Test
	public void getGanzhiOfYearTest(){
		Assertions.assertEquals("庚子", GanZhi.getGanzhiOfYear(2020));
	}

	@Test
	public void getCyclicalYMDTest(){
		//通过公历构建
		final ChineseDate chineseDate = new ChineseDate(Objects.requireNonNull(DateUtil.parse("1993-01-06")));
		final String cyclicalYMD = chineseDate.toString(ChineseDateFormat.GGG);
		Assertions.assertEquals("农历壬申年癸丑月丁亥日",cyclicalYMD);
	}

	@Test
	public void getCyclicalYMDTest2(){
		//通过农历构建
		final ChineseDate chineseDate = new ChineseDate(1992,12,14);
		final String cyclicalYMD = chineseDate.toString(ChineseDateFormat.GGG);
		Assertions.assertEquals("农历壬申年癸丑月丁亥日",cyclicalYMD);
	}

	@Test
	public void getCyclicalYMDTest3(){
		//通过公历构建
		final ChineseDate chineseDate = new ChineseDate(Objects.requireNonNull(DateUtil.parse("2020-08-28")));
		final String cyclicalYMD = chineseDate.toString(ChineseDateFormat.GGG);
		Assertions.assertEquals("农历庚子年甲申月癸卯日",cyclicalYMD);
	}

	@Test
	public void getCyclicalYMDTest4(){
		//通过公历构建
		final ChineseDate chineseDate = new ChineseDate(Objects.requireNonNull(DateUtil.parse("1905-08-28")));
		final String cyclicalYMD = chineseDate.toString(ChineseDateFormat.GGG);
		Assertions.assertEquals("农历乙巳年甲申月己亥日",cyclicalYMD);
	}
	@Test
	public void lunarYearStartsAtSpringFestivalTest() {
		Assertions.assertEquals("癸卯", new ChineseDate(LocalDate.of(2024, 2, 4)).getCyclical());
		Assertions.assertEquals("癸卯", new ChineseDate(LocalDate.of(2024, 2, 9)).getCyclical());
		Assertions.assertEquals("甲辰", new ChineseDate(LocalDate.of(2024, 2, 10)).getCyclical());
		Assertions.assertEquals("甲辰", new ChineseDate(LocalDate.of(2025, 1, 28)).getCyclical());
		Assertions.assertEquals("乙巳", new ChineseDate(LocalDate.of(2025, 1, 29)).getCyclical());
		Assertions.assertEquals("乙巳", new ChineseDate(LocalDate.of(2025, 2, 3)).getCyclical());
	}

	@Test
	public void monthChangesOnJieDateTest() {
		Assertions.assertEquals("甲子", GanZhi.getGanzhiOfMonth(2024, 1, 5));
		Assertions.assertEquals("乙丑", GanZhi.getGanzhiOfMonth(2024, 1, 6));
		Assertions.assertEquals("乙丑", GanZhi.getGanzhiOfMonth(2024, 2, 3));
		Assertions.assertEquals("丙寅", GanZhi.getGanzhiOfMonth(2024, 2, 4));
		Assertions.assertEquals("丁丑", GanZhi.getGanzhiOfMonth(2025, 2, 2));
		Assertions.assertEquals("戊寅", GanZhi.getGanzhiOfMonth(2025, 2, 3));
	}

	@Test
	public void dayFollowsCivilDateTest() {
		Assertions.assertEquals("甲子", GanZhi.getGanzhiOfDay(1949, 10, 1));
		Assertions.assertEquals("己亥", GanZhi.getGanzhiOfDay(2024, 2, 5));
		Assertions.assertEquals("庚子", GanZhi.getGanzhiOfDay(2024, 2, 6));
	}

	@Test
	public void combinedFormatKeepsLunarYearTest() {
		Assertions.assertEquals("农历癸卯年丙寅月己亥日",
			new ChineseDate(LocalDate.of(2024, 2, 5)).toString(ChineseDateFormat.GGG));
		Assertions.assertEquals("农历乙巳年丁丑月辛丑日",
			new ChineseDate(LocalDate.of(2025, 2, 1)).toString(ChineseDateFormat.GGG));
	}

}
