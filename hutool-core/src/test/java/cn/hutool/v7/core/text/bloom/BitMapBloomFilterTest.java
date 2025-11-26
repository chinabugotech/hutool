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

package cn.hutool.v7.core.text.bloom;

import cn.hutool.v7.core.codec.hash.HashUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BitMapBloomFilterTest {

	private static final int SIZE = 2 * 1024 * 1024 * 8;

	@Test
	public void filterTest() {

		final CombinedBloomFilter filter = new CombinedBloomFilter(FuncFilter.of(SIZE, HashUtil::rsHash));
		filter.add("123");
		filter.add("abc");
		filter.add("ddd");

		Assertions.assertTrue(filter.contains("abc"));
		Assertions.assertTrue(filter.contains("ddd"));
		Assertions.assertTrue(filter.contains("123"));
	}
	@Test
	public void multiHashFuncTest() {
		final FuncFilter filter = FuncFilter.of(SIZE,
			HashUtil::rsHash,
			HashUtil::jsHash,
			HashUtil::pjwHash,
			HashUtil::elfHash,
			HashUtil::bkdrHash,
			HashUtil::sdbmHash,
			HashUtil::djbHash,
			HashUtil::dekHash,
			HashUtil::apHash,
			HashUtil::javaDefaultHash
		);

		filter.add("Hutool");
		filter.add("BloomFilter");
		filter.add("Java");

		Assertions.assertTrue(filter.contains("Hutool"));
		Assertions.assertTrue(filter.contains("BloomFilter"));
		Assertions.assertTrue(filter.contains("Java"));
		Assertions.assertFalse(filter.contains("Python"));
		Assertions.assertFalse(filter.contains("Go"));
		Assertions.assertFalse(filter.contains("hutool"));
	}

	@Test
	public void combinedMultiHashTest() {
		FuncFilter multiHashFuncFilter = FuncFilter.of(SIZE,
			HashUtil::bkdrHash,
			HashUtil::apHash,
			HashUtil::djbHash
		);
		final CombinedBloomFilter filter = new CombinedBloomFilter(multiHashFuncFilter);
		filter.add("123123WASD-WASD");
		Assertions.assertTrue(filter.contains("123123WASD-WASD"));
		Assertions.assertFalse(filter.contains("123123WASD-WASD-false"));
	}

	@Test
	public void chineseStringWithThreeHashesTest() {
		final FuncFilter filter = FuncFilter.of(SIZE,
			HashUtil::bkdrHash,
			HashUtil::apHash,
			HashUtil::djbHash
		);

		String s1 = "你好世界";
		String s2 = "双亲委派";
		String s3 = "测试工程师";

		filter.add(s1);
		filter.add(s2);
		filter.add(s3);
		Assertions.assertTrue(filter.contains(s1), "应包含: " + s1);
		Assertions.assertTrue(filter.contains(s2), "应包含: " + s2);
		Assertions.assertTrue(filter.contains(s3), "应包含: " + s3);
		Assertions.assertFalse(filter.contains("我好世界"), "多字");
		Assertions.assertFalse(filter.contains("父亲委派"), "改字");
		Assertions.assertFalse(filter.contains("测试"), "子串");
		Assertions.assertFalse(filter.contains(""), "空串");
		Assertions.assertFalse(filter.contains("👍"), "未添加的");
	}
}
