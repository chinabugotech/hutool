/*
 * Copyright (c) 2026 Hutool Team.
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

package cn.hutool.v7.poi.excel.sax;

import cn.hutool.v7.core.lang.Console;
import cn.hutool.v7.poi.excel.ExcelUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class IssueIJSHQTTest {
	@Test
	@Disabled
	public void readBySaxXlsTest() {
		// 替换为你的xls文件路径
		ExcelUtil.readBySax("d:/test/b106e21a-78e1-49a7-86e4-da70035ac6ed.xls", 0,
			(sheetIndex, rowIndex, rowList) -> {
				Console.log("[{}] [{}] {}", sheetIndex, rowIndex, rowList);
			});
	}
}
