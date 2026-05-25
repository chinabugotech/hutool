package cn.hutool.core.text.csv;

import cn.hutool.core.io.resource.ResourceUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IssueICRMKATest {
	@Test
	public void issueICRMAKTest() {
		CsvReader reader = CsvUtil.getReader();
		CsvData data = reader.read(ResourceUtil.getUtf8Reader("issueICRMKA.csv"));
		final CsvRow row = data.getRow(1);
		Assertions.assertEquals("6.3\" Google Pixel 9 Pro 128 GB Beige", row.get(0));
	}
}
