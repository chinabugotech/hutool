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

package cn.hutool.v7.poi.excel.writer;

import cn.hutool.v7.poi.excel.ExcelUtil;
import cn.hutool.v7.poi.excel.SimpleClientAnchor;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class WriteCommentTest {
	@Test
	@Disabled
	void writeCommentTest() {
		final ExcelWriter writer = ExcelUtil.getWriter("d:/test/comments.xlsx");
		final SimpleClientAnchor clientAnchor = new SimpleClientAnchor(0, 0, 1, 1);
		ExcelDrawingUtil.drawingCellComment(writer.getOrCreateCell(5, 6), clientAnchor, "测试内容");
		writer.close();
	}
}
