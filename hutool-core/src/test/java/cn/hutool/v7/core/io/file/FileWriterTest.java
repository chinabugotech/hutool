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

package cn.hutool.v7.core.io.file;

import cn.hutool.v7.core.collection.ListUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class FileWriterTest {
	@Test
	@Disabled
	void writeLinesAppendLineSeparatorTest() {
		final FileWriter writer = FileWriter.of(FileUtil.file("d:/test/lines_append_line_separator.txt"));
		writer.writeLines(ListUtil.of("aaa", "bbb", "ccc"), null, false);
	}

	@Test
	@Disabled
	void writeLinesTest() {
		final FileWriter writer = FileWriter.of(FileUtil.file("d:/test/lines.txt"));
		writer.writeLines(ListUtil.of("aaa", "bbb", "ccc"), null, false, false);
	}
}
