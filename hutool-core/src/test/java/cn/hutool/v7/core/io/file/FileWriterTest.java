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
