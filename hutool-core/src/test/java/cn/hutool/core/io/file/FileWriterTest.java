package cn.hutool.core.io.file;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.FileUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class FileWriterTest {

	@Test
	@Disabled
	void writeLinesAppendLineSeparatorTest() {
		final FileWriter writer = FileWriter.create(FileUtil.file("d:/test/lines_append_line_separator.txt"));
		writer.writeLines(ListUtil.of("aaa", "bbb", "ccc"), null, false, true);
	}

	@Test
	@Disabled
	void writeLinesTest() {
		final FileWriter writer = FileWriter.create(FileUtil.file("d:/test/lines.txt"));
		writer.writeLines(ListUtil.of("aaa", "bbb", "ccc"), null, false);
	}

}
