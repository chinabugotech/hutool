package cn.hutool.v7.core.io.file;

import cn.hutool.v7.core.io.IORuntimeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class Issue4121Test {
	@Test
	void testListFileNames_NonExistentDirectory() {
		assertThrows(IORuntimeException.class, () -> {
			FileUtil.listFileNames("/non/existent/path");
		});
	}

}
