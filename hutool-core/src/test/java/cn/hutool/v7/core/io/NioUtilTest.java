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

package cn.hutool.v7.core.io;

import cn.hutool.v7.core.io.file.FileUtil;
import cn.hutool.v7.core.io.resource.ResourceUtil;
import cn.hutool.v7.core.io.stream.EmptyOutputStream;
import cn.hutool.v7.core.lang.Console;
import cn.hutool.v7.core.text.StrUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class NioUtilTest {
	@Test
	public void copyByNIOTest() {
		final File file = FileUtil.file("hutool.jpg");
		final long size = NioUtil.copyByNIO(ResourceUtil.getStream("hutool.jpg"), EmptyOutputStream.INSTANCE, NioUtil.DEFAULT_MIDDLE_BUFFER_SIZE, null);

		// 确认写出
		Assertions.assertEquals(file.length(), size);
		Assertions.assertEquals(22807, size);
	}

	@Test
	@Disabled
	public void copyByNIOTest2() {
		final File file = FileUtil.file("d:/test/logo.jpg");
		final BufferedInputStream in = FileUtil.getInputStream(file);
		final BufferedOutputStream out = FileUtil.getOutputStream("d:/test/2logo.jpg");

		final long copySize = IoUtil.copyByNIO(in, out, NioUtil.DEFAULT_BUFFER_SIZE, new StreamProgress() {
			@Override
			public void start() {
				Console.log("start");
			}

			@Override
			public void progress(final long total, final long progressSize) {
				Console.log("{} {}", total, progressSize);
			}

			@Override
			public void finish() {
				Console.log("finish");
			}
		});
		Assertions.assertEquals(file.length(), copySize);
	}

	@Test
	public void readUtf8Test() throws IOException {
		final String s = NioUtil.readUtf8(FileChannel.open(FileUtil.file("text.txt").toPath()));
		Assertions.assertTrue(StrUtil.isNotBlank(s));
	}
}
