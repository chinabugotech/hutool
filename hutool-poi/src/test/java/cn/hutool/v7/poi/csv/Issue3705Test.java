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

package cn.hutool.v7.poi.csv;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.StringWriter;

public class Issue3705Test {

	@Test
	void writeTest() {
		final StringWriter stringWriter = new StringWriter();

		final CsvWriteConfig csvWriteConfig = new CsvWriteConfig();
		try (final CsvWriter csvWriter = new CsvWriter(stringWriter, csvWriteConfig)) {
			// 由于一行的第一个字段中有逗号，因此需要使用双引号包围，否则会被当成分隔符
			csvWriter.writeLine("2024-08-20 14:24:35,");
			csvWriter.writeLine("最后一行");
			csvWriter.flush();
		}

		// CsvWriteConfig中默认为`\r\n`
		Assertions.assertEquals(
			"\"2024-08-20 14:24:35,\"\r\n最后一行",
			stringWriter.toString());
	}
}
