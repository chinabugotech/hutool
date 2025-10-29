package cn.hutool.v7.core.io.file;

import cn.hutool.v7.core.io.IORuntimeException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class Issue4121Test {
	@Test
	void testListFileNames_NonExistentDirectory() {
		assertThrows(IORuntimeException.class, () -> {
			FileUtil.listFileNames("/non/existent/path");
		});
	}

	@Test
	void testListFileNames_RelativePath() {
		// 测试相对路径（相对于classpath）
		List<String> result = FileUtil.listFileNames("META-INF");

		assertEquals(3, result.size());
		assertTrue(result.contains("MANIFEST.MF"));
		assertTrue(result.contains("LICENSE-notice.md"));
		assertTrue(result.contains("LICENSE.md"));
	}

}
