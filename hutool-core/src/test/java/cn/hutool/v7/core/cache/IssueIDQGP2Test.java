/*
 * Copyright (c) 2026 Hutool Team.
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

package cn.hutool.v7.core.cache;

import cn.hutool.v7.core.collection.CollUtil;
import cn.hutool.v7.core.collection.ListUtil;
import cn.hutool.v7.core.func.SerSupplier;
import cn.hutool.v7.core.lang.Console;
import cn.hutool.v7.core.thread.ThreadUtil;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

public class IssueIDQGP2Test {
	private static final Cache<String, List<String>> CACHE = CacheUtil.newLFUCache(1, 10);
	@SneakyThrows
	public static void main(final String[] args) {
		final List<Future<?>> futures = new ArrayList<>();
		for (int i = 0; i < 10000; i++) {
			final Future<?> future = ThreadUtil.execAsync(() -> {
				final List<String> dataList = CACHE.get("x1", (SerSupplier<List<String>>) () -> ListUtil.of("x1", "x2", "x3"));

				if (CollUtil.isEmpty(dataList)) {
					Console.log("dataList is empty");
				}
			});
			futures.add(future);
		}
		for (final Future<?> future : futures) {
			future.get();
		}
		Console.log("done");
	}
}
