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

package cn.hutool.v7.poi.excel.shape;

import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Workbook;
import cn.hutool.v7.core.io.IoUtil;
import cn.hutool.v7.core.lang.Console;
import cn.hutool.v7.poi.excel.WorkbookUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ExcelPicUtilTest {
	@Test
	@Disabled
	void readPicTest() {
		final Workbook book = WorkbookUtil.createBook("d:/test/poi/a.xlsx");
		final List<Picture> picMap = ExcelPicUtil.getShapePics(
			WorkbookUtil.createBook("d:/test/poi/a.xlsx"), 0);
		Console.log(picMap);

//		final List<? extends PictureData> allPictures = book.getAllPictures();
//		for (PictureData shape : allPictures) {
//			Console.log(shape);
//		}

		IoUtil.closeQuietly(book);
	}
}
