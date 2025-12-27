package cn.hutool.v7.poi.excel.sax;

import cn.hutool.v7.core.lang.Console;
import cn.hutool.v7.poi.excel.ExcelUtil;
import cn.hutool.v7.poi.excel.cell.values.FormulaCellValue;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class Issue4195Test {
	@Test
	@Disabled
	void saxReadFormulaTest() {
		// 测试公式读取
		ExcelUtil.readBySax("formula_test.xlsx", -1, (sheetIndex, rowIndex, rowCells) -> {
			final Object value = rowCells.get(2);
			if(value instanceof final FormulaCellValue result) {
				Console.log("公式 {} 结果: {}", result.getValue(), result.getResult());
			}else{
				Console.log("非公式: {}", value.getClass());
			}
		});
	}
}
