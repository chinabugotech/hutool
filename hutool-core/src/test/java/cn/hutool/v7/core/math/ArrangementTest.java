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

package cn.hutool.v7.core.math;

import cn.hutool.v7.core.lang.Console;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 排列单元测试
 *
 * @author Looly
 *
 */
public class ArrangementTest {

	@Test
	public void arrangementTest() {
		long result = Arrangement.count(4, 2);
		assertEquals(12, result);

		result = Arrangement.count(4, 1);
		assertEquals(4, result);

		result = Arrangement.count(4, 0);
		assertEquals(1, result);

		final long resultAll = Arrangement.countAll(4);
		assertEquals(64, resultAll);
	}

	@Test
	public void selectTest() {
		final Arrangement arrangement = new Arrangement(new String[]{"1", "2", "3", "4"});
		final List<String[]> list = arrangement.select(2);
		// 校验数量一致
		assertEquals(Arrangement.count(4, 2), list.size());
		// 逐项严格校验顺序是否一致（按 DFS 顺序）
		assertArrayEquals(new String[]{"1", "2"}, list.get(0));
		assertArrayEquals(new String[]{"1", "3"}, list.get(1));
		assertArrayEquals(new String[]{"1", "4"}, list.get(2));
		assertArrayEquals(new String[]{"2", "1"}, list.get(3));
		assertArrayEquals(new String[]{"2", "3"}, list.get(4));
		assertArrayEquals(new String[]{"2", "4"}, list.get(5));
		assertArrayEquals(new String[]{"3", "1"}, list.get(6));
		assertArrayEquals(new String[]{"3", "2"}, list.get(7));
		assertArrayEquals(new String[]{"3", "4"}, list.get(8));
		assertArrayEquals(new String[]{"4", "1"}, list.get(9));
		assertArrayEquals(new String[]{"4", "2"}, list.get(10));
		assertArrayEquals(new String[]{"4", "3"}, list.get(11));

		// 测试 selectAll
		final List<String[]> selectAll = arrangement.selectAll();
		assertEquals(Arrangement.countAll(4), selectAll.size());

		// m=0，应该返回一个空排列
		final List<String[]> list2 = arrangement.select(0);
		assertEquals(1, list2.size());
	}

	// ----------------------------------------------------
	// 扩展测试：边界、错误处理
	// ----------------------------------------------------
	@Test
	public void boundaryTest() {
		final Arrangement arr = new Arrangement(new String[]{"A", "B", "C"});

		// m = n
		final List<String[]> full = arr.select(3);
		assertEquals(6, full.size());

		// m = 1
		final List<String[]> one = arr.select(1);
		assertEquals(3, one.size());
		assertArrayEquals(new String[]{"A"}, one.get(0));

		// m > n → empty list
		assertTrue(arr.select(10).isEmpty());

		// m < 0 → empty list
		assertTrue(arr.select(-1).isEmpty());
	}

	// ----------------------------------------------------
	// 扩展测试：空数组
	// ----------------------------------------------------
	@Test
	public void emptyTest() {
		final Arrangement arrangement = new Arrangement(new String[]{});

		assertEquals(1, arrangement.select(0).size());
		assertTrue(arrangement.select(1).isEmpty());
		assertTrue(arrangement.selectAll().isEmpty()); // A(0,m) = 0 for m>0，A(0,0)=1 → 全排列 = 1 个空排列
	}

	// ----------------------------------------------------
	// 扩展测试：重复元素（用于验证去重算法）
	// 默认 Arrangement 不去重，因此应该包含重复排列
	// ----------------------------------------------------
	@Test
	@Disabled("默认 Arrangement 不支持去重；启用后手动检查")
	public void duplicateElementTest() {
		final Arrangement arrangement = new Arrangement(new String[]{"1", "1", "3"});
		final List<String[]> list = arrangement.select(2);

		// 应该有 A(3,2) = 6 个
		assertEquals(6, list.size());

		for (final String[] s : list) {
			Console.log(s);
		}
	}

	// ----------------------------------------------------
	// 扩展测试：selectAll 覆盖全部不重复排列（A(n,1..n)）
	// ----------------------------------------------------
	@Test
	public void selectAllTest() {
		final Arrangement arrangement = new Arrangement(new String[]{"1", "2", "3"});

		final List<String[]> all = arrangement.selectAll();

		// 打印用于观测
//		for (final String[] s : all) {
//			Console.log(s);
//		}

		// A(3,1) + A(3,2) + A(3,3) = 3 + 6 + 6 = 15
		assertEquals(Arrangement.countAll(3), all.size());
		assertEquals(15, all.size());

		// spot check 不重复排列
		assertArrayEquals(new String[]{"1"}, all.get(0));
		assertArrayEquals(new String[]{"1", "2"}, all.get(3));
		assertArrayEquals(new String[]{"1", "2", "3"}, all.get(9));
	}

	// ----------------------------------------------------
	// 迭代器测试
	// ----------------------------------------------------
	@Test
	public void iteratorTest() {
		final Arrangement arrangement = new Arrangement(new String[]{"1", "2", "3"});

		// 测试 m=2 的情况
		final List<String[]> iterResult = new ArrayList<>();
		for (final String[] perm : arrangement.iterate(2)) {
			iterResult.add(perm);
		}

		assertEquals(6, iterResult.size());
		assertArrayEquals(new String[]{"1", "2"}, iterResult.get(0));
		assertArrayEquals(new String[]{"1", "3"}, iterResult.get(1));
		assertArrayEquals(new String[]{"2", "1"}, iterResult.get(2));
		assertArrayEquals(new String[]{"2", "3"}, iterResult.get(3));
		assertArrayEquals(new String[]{"3", "1"}, iterResult.get(4));
		assertArrayEquals(new String[]{"3", "2"}, iterResult.get(5));
	}

	@Test
	public void iteratorFullTest() {
		final Arrangement arrangement = new Arrangement(new String[]{"1", "2", "3"});

		// 测试全排列的情况
		final List<String[]> iterResult = new ArrayList<>();
		for (final String[] perm : arrangement.iterate(3)) {
			iterResult.add(perm);
		}

		assertEquals(6, iterResult.size());
	}

	@Test
	public void iteratorBoundaryTest() {
		final Arrangement arrangement = new Arrangement(new String[]{"1", "2", "3"});

		// 测试 m > n 的情况
		final List<String[]> iterResult = new ArrayList<>();
		for (final String[] perm : arrangement.iterate(5)) {
			iterResult.add(perm);
		}
		assertTrue(iterResult.isEmpty());
	}
}
