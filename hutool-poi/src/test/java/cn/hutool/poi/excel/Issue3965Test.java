package cn.hutool.poi.excel;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.FileUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class Issue3965Test {
	@Test
	@Disabled
	void writeTest() {
		ArrayList<List<String>> arrayList = new ArrayList<>();

		arrayList.add(ListUtil.of("a"));
		arrayList.add(ListUtil.of("b"));
		arrayList.add(ListUtil.of("c"));
		arrayList.add(ListUtil.of("d"));

		ExcelWriter writer = ExcelUtil.getWriter(FileUtil.file("d:/test/123.xlsx"));
		writer.setColumnWidth(0, 50);
		writer.write(arrayList);
		writer.flush();
		writer.close();
	}
}
