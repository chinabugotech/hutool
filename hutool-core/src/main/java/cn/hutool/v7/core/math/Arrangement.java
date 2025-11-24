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

package cn.hutool.v7.core.math;

import cn.hutool.v7.core.array.ArrayUtil;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

/**
 * 排列A(n, m)<br>
 * 排列组合相关类 参考：<a href="http://cgs1999.iteye.com/blog/2327664">http://cgs1999.iteye.com/blog/2327664</a>
 *
 * @author Looly
 * @since 4.0.7
 */
public class Arrangement implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 用于排列的数据
	 */
	private final String[] datas;

	/**
	 * 构造
	 *
	 * @param datas 用于排列的数据
	 */
	public Arrangement(final String[] datas) {
		this.datas = datas;
	}

	/**
	 * 计算排列数，即A(n, n) = n!
	 *
	 * @param n 总数
	 * @return 排列数
	 */
	public static long count(final int n) {
		return count(n, n);
	}

	/**
	 * 计算排列数，即A(n, m) = n!/(n-m)!
	 *
	 * @param n 总数
	 * @param m 选择的个数
	 * @return 排列数
	 */
	public static long count(final int n, final int m) {
		if (m < 0 || m > n) {
			throw new IllegalArgumentException("n >= 0 && m >= 0 && m <= n required");
		}
		if (m == 0) {
			return 1;
		}
		long result = 1;
		// 从 n 到 n-m+1 逐个乘
		for (int i = 0; i < m; i++) {
			long next = result * (n - i);
			// 溢出检测
			if (next < result) {
				throw new ArithmeticException("Overflow computing A(" + n + "," + m + ")");
			}
			result = next;
		}
		return result;
	}

	/**
	 * 计算排列总数，即A(n, 1) + A(n, 2) + A(n, 3)...
	 *
	 * @param n 总数
	 * @return 排列数
	 */
	public static long countAll(final int n) {
		long total = 0;
		for (int i = 1; i <= n; i++) {
			total += count(n, i);
		}
		return total;
	}

	/**
	 * 全排列选择（列表全部参与排列）
	 *
	 * @return 所有排列列表
	 */
	public List<String[]> select() {
		return select(this.datas.length);
	}

	/**
	 * 从当前数据中选择 m 个元素，生成所有「不重复」的排列（Permutation）。
	 *
	 * <p>
	 * 说明：
	 * <ul>
	 *     <li>不允许重复选择同一个元素（即经典排列 A(n, m)）</li>
	 *     <li>结果中不会出现 ["1","1"] 这种重复元素的情况</li>
	 *     <li>顺序敏感，因此 ["1","2"] 与 ["2","1"] 都会包含</li>
	 * </ul>
	 * <p>
	 * 数量公式：
	 * <pre>
	 * A(n, m) = n! / (n - m)!
	 * </pre>
	 * <p>
	 * 举例：
	 * <pre>
	 * datas = ["1","2","3"]
	 * m = 2
	 * 输出：
	 * ["1","2"]
	 * ["1","3"]
	 * ["2","1"]
	 * ["2","3"]
	 * ["3","1"]
	 * ["3","2"]
	 * 共 6 个（A(3,2)=6）
	 * </pre>
	 *
	 * @param m 选择的元素个数
	 * @return 所有长度为 m 的不重复排列列表
	 */
	public List<String[]> select(final int m) {
		if (m < 0 || m > datas.length) {
			return Collections.emptyList();
		}
		if (m == 0) {
			// A(n,0) = 1，唯一一个空排列
			return Collections.singletonList(new String[0]);
		}

		long estimated = count(datas.length, m);
		int capacity = estimated > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) estimated;

		List<String[]> result = new ArrayList<>(capacity);
		boolean[] visited = new boolean[datas.length];
		dfs(new String[m], 0, visited, result);
		return result;
	}

	/**
	 * 生成当前数据的全部不重复排列（长度为 1 至 n 的所有排列）。
	 *
	 * <p>
	 * 说明：
	 * <ul>
	 *     <li>不允许重复选择元素（无 ["1","1"]，无 ["2","2","3"] 这种）</li>
	 *     <li>包含所有长度 m=1..n 的排列</li>
	 *     <li>总数量为 A(n,1) + A(n,2) + ... + A(n,n)</li>
	 * </ul>
	 * <p>
	 * 举例（datas = ["1","2","3"]）：
	 * <pre>
	 * m=1: ["1"], ["2"], ["3"]                             → 3 个
	 * m=2: ["1","2"], ["1","3"], ["2","1"], ...            → 6 个
	 * m=3: ["1","2","3"], ["1","3","2"], ["2","1","3"], ...→ 6 个
	 *
	 * 总共：3 + 6 + 6 = 15
	 * </pre>
	 *
	 * @return 所有不重复排列列表
	 */
	public List<String[]> selectAll() {
		final List<String[]> result = new ArrayList<>();
		for (int m = 1; m <= datas.length; m++) {
			result.addAll(select(m));
		}
		return result;
	}

	/**
	 * 返回一个排列的迭代器
	 *
	 * @param m 选择的元素个数
	 * @return 排列迭代器
	 */
	public Iterable<String[]> iterate(int m) {
		return () -> new ArrangementIterator(datas, m);
	}


	/**
	 * 排列迭代器
	 *
	 * @author CherryRum
	 */
	private static class ArrangementIterator implements Iterator<String[]> {

		private final String[] datas;
		private final int m;
		private final boolean[] visited;
		private final String[] buffer;
		private final Deque<Integer> stack = new ArrayDeque<>();
		boolean end = false;

		/**
		 * 构造函数
		 *
		 * @param datas 数据数组
		 * @param m     选择的元素个数
		 */
		ArrangementIterator(String[] datas, int m) {
			this.datas = datas;
			this.m = m;
			this.visited = new boolean[datas.length];
			this.buffer = new String[m];
			// 初始化 dfs 栈
			stack.push(0);
		}

		@Override
		public boolean hasNext() {
			return !end;
		}

		@Override
		public String[] next() {
			while (!stack.isEmpty()) {
				int depth = stack.size() - 1;

				int idx = stack.pop();
				if (idx >= datas.length) {
					// 这一层遍历结束
					if (!stack.isEmpty()) {
						int prev = stack.pop();
						stack.push(prev + 1);
					}
					continue;
				}

				// 如果该元素未使用
				if (!visited[idx]) {
					visited[idx] = true;
					buffer[depth] = datas[idx];

					if (depth == m - 1) {
						// 输出一个排列
						visited[idx] = false;

						// 下一次从 idx+1 继续
						stack.push(idx + 1);

						return Arrays.copyOf(buffer, m);
					} else {
						// 继续下一层
						stack.push(idx + 1); // 当前层下一个起点
						stack.push(0);       // 下一层起点
						continue;
					}
				}

				// 已访问则跳过
				stack.push(idx + 1);
			}

			end = true;
			return null;
		}
	}

	/**
	 * 核心递归方法（回溯算法）
	 * * @param current 当前构建的排列数组
	 *
	 * @param depth   当前递归深度（填到了第几个位置）
	 * @param visited 标记数组，记录哪些索引已经被使用了
	 * @param result  结果集
	 */
	private void dfs(String[] current, int depth, boolean[] visited, List<String[]> result) {
		if (depth == current.length) {
			result.add(Arrays.copyOf(current, current.length));
			return;
		}

		for (int i = 0; i < datas.length; i++) {
			if (!visited[i]) {
				visited[i] = true;
				current[depth] = datas[i];

				dfs(current, depth + 1, visited, result);
				visited[i] = false;
			}
		}
	}
}
