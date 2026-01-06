/*
 * Copyright (c) 2013-2026 Hutool Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.hutool.v7.extra.compress;

import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import cn.hutool.v7.core.io.file.FileUtil;
import cn.hutool.v7.core.util.CharsetUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class IssueI71K5VTest {

	@SuppressWarnings("resource")
	@Test
	@Disabled
	void createArchiverTest() {
		CompressUtil.
			createArchiver(CharsetUtil.UTF_8, ArchiveStreamFactory.ZIP,
				FileUtil.file("d:\\test\\test.zip"))
			.add(FileUtil.file("d:\\java\\"))
			.close();
	}
}
