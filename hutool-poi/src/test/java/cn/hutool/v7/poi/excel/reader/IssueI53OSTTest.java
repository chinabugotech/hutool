/*
 * Copyright (c) 2013-2025 Hutool Team and hutool.cn
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

package cn.hutool.v7.poi.excel.reader;

import cn.hutool.v7.core.lang.Console;
import cn.hutool.v7.poi.excel.ExcelUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Sax方式读取合并单元格，只有第一个单元格有值，其余为null
 */
public class IssueI53OSTTest {

	@Test
	@Disabled
	public void readTest(){
		final Map<String, Object> result = new HashMap<>();
		final List<Object> header = new ArrayList<>();

		ExcelUtil.readBySax("d:/test/sax_merge.xlsx", -1, (sheetIndex, rowIndex, rowCells) -> {
			if(rowIndex == 0){
				header.addAll(rowCells);
				return;
			}
			for (int i = 0; i < rowCells.size(); i++) {
				result.put((String) header.get(i), rowCells.get(i));
			}
			Console.log(result);
			result.clear();
		});
	}
}
