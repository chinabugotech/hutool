package cn.hutool.v7.poi.excel.writer;

import cn.hutool.v7.poi.excel.ExcelUtil;
import cn.hutool.v7.poi.excel.style.StyleUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class Issue4146Test {
	@Test
	@Disabled
	public void writeSheetWithStyleTest() {
		final ExcelWriter writer = ExcelUtil.getWriter("d:\\test\\issue4146.xlsx", "表格1");

		final List<TestUser> list = new ArrayList<>();
		TestUser test = new TestUser("张三", 18, 90.0, 0.9878);
		list.add(test);
		test = new TestUser("李四", 18, 79.5, 0.8311);
		list.add(test);
		test = new TestUser("王五", 18, 89.9, 0.6932);
		list.add(test);
		test = new TestUser("赵六", 18, 69.9, 0.7912);
		list.add(test);
		test = new TestUser("孙七", 18, 79.9, 0.6432);
		list.add(test);

		final ExcelWriteConfig config = writer.getConfig();
		config.addHeaderAlias("name", "姓名");
		config.addHeaderAlias("age", "年龄");
		config.addHeaderAlias("score", "分数");
		config.addHeaderAlias("zb", "占比");

		config.setOnlyAlias(true);
		writer.write(list, true);

		// 百分比的单元格样式必须单独创建，使用StyleSet中的样式修改则会修改全局样式
		final CellStyle percentCellStyle = writer.createCellStyle();
		percentCellStyle.setDataFormat(writer.getWorkbook().createDataFormat().getFormat("0.00%"));
		// 填充背景颜色，必须指定FillPatternType才有效
		StyleUtil.setColor(percentCellStyle, IndexedColors.YELLOW, FillPatternType.SOLID_FOREGROUND);
		// 设置边框颜色和粗细
		StyleUtil.setBorder(percentCellStyle, BorderStyle.THIN, IndexedColors.BLACK);
		final int rowCount = writer.getRowCount();
		// 设置列样式无效，除非将默认样式清除，因此必须在写出数据后为单元格指定自定义的样式
		for (int i = 1; i < rowCount; i++) {
			writer.setStyle(percentCellStyle, 3, i);
		}

		writer.close();
	}

	@Data
	@AllArgsConstructor
	static class TestUser {
		private String name;
		private Integer age;
		private Double score;
		private Double zb;
	}
}
