package cn.hutool.poi.excel.sax;

import cn.hutool.core.lang.Console;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.cell.FormulaCellValue;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class Issue4195Test {
	@Test
	@Disabled
	void saxReadFormulaTest() {
		// 测试公式读取
		ExcelUtil.readBySax("formula_test.xlsx", -1, (sheetIndex, rowIndex, rowCells) -> {
			final Object value = rowCells.get(2);
			if(value instanceof FormulaCellValue) {
				final FormulaCellValue result = ((FormulaCellValue) value);
				Console.log("公式 {} 结果: {}", result.getValue(), result.getResult());
			}else{
				Console.log("非公式: {}", value.getClass());
			}
		});
	}
}
