/*
 * Copyright (c) 2013-2025 Hutool Team and hutool.cn
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

import cn.hutool.v7.core.io.file.FileUtil;
import cn.hutool.v7.core.util.CharsetUtil;
import cn.hutool.v7.extra.compress.extractor.Extractor;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@SuppressWarnings("resource")
public class ExtractorTest {

	@Test
	@Disabled
	public void zipTest(){
		final Extractor extractor = CompressUtil.createExtractor(
				CharsetUtil.defaultCharset(),
				FileUtil.file("d:/test/c_1344112734760931330_20201230104703032.zip"));

		extractor.extract(FileUtil.file("d:/test/compress/test2/"));
	}

	@Test
	@Disabled
	public void sevenZTest(){
		final Extractor extractor = 	CompressUtil.createExtractor(
				CharsetUtil.defaultCharset(),
				FileUtil.file("d:/test/compress/test.7z"));

		extractor.extract(FileUtil.file("d:/test/compress/test2/"));
	}

	@Test
	@Disabled
	public void tgzTest(){
		final Extractor extractor = 	CompressUtil.createExtractor(
				CharsetUtil.defaultCharset(),
				"tgz",
				FileUtil.file("d:/test/test.tgz"));

		extractor.extract(FileUtil.file("d:/test/tgz/"));
	}
}
