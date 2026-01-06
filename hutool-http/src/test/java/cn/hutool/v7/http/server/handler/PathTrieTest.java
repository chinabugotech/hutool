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

package cn.hutool.v7.http.server.handler;

import cn.hutool.v7.core.lang.Console;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PathTrieTest {
	@Test
	public void testPathTrie() {
		final PathTrie trie = new PathTrie();

		// 添加一些路径和对应的处理器
		trie.add("/user", (req, res) -> Console.log("User handler"));
		trie.add("/user/profile", (req, res) -> Console.log("Profile handler"));

		// 测试精确匹配
		assertNotNull(trie.match("/user"));
		// 匹配父路径
		assertNotNull(trie.match("/user/test1"));
		// 匹配最近的上级路径
		assertNotNull(trie.match("/user/test1/test2"));

		// 自动忽略空路径，尾部的/也忽略
		assertNotNull(trie.match("/user/profile"));
		assertNotNull(trie.match("/user/profile/"));
		assertNotNull(trie.match("/user////profile/"));

		// 测试不存在的路径
		assertNull(trie.match("/nonexistent"));
		assertNull(trie.match("/"));
		assertNull(trie.match(""));
		assertNull(trie.match(null));
	}
}
