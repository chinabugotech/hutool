package cn.hutool.poi.excel;

import cn.hutool.core.lang.Console;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * 测试使用ExcelUtil.readBySax读取.xls文件时，公式结果为字符串的单元格内容不缺失
 *
 * @see <a href="https://gitee.com/chinabugotech/hutool/issues/IJSHQT">Issue IJSHQT</a>
 */
public class IssueIJSHQTTest {

	@Test
    @Disabled
	public void readBySaxXlsTest() {
		// 替换为你的xls文件路径
		ExcelUtil.readBySax("C:\\Users\\admin\\Downloads\\b106e21a-78e1-49a7-86e4-da70035ac6ed.xls", 0,
				(sheetIndex, rowIndex, rowList) -> {
					Console.log("[{}] [{}] {}", sheetIndex, rowIndex, rowList);
				});
	}
}
