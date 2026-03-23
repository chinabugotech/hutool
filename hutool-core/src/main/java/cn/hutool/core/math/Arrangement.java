package cn.hutool.core.math;

import cn.hutool.core.util.ArrayUtil;

import java.io.Serializable;
import java.util.*;

/**
 * 排列A(n, m)<br>
 * 排列组合相关类 参考：http://cgs1999.iteye.com/blog/2327664
 *
 * @author looly
 * @since 4.0.7
 */
public class Arrangement implements Serializable {
	private static final long serialVersionUID = 1L;

	private final String[] datas;

	/**
	 * 构造
	 *
	 * @param datas 用于排列的数据
	 */
	public Arrangement(String[] datas) {
		this.datas = datas;
	}

	/**
	 * 计算排列数，即A(n, n) = n!
	 *
	 * @param n 总数
	 * @return 排列数
	 */
	public static long count(int n) {
		return count(n, n);
	}

	/**
	 * 计算排列数，即A(n, m) = n!/(n-m)!
	 *
	 * @param n 总数
	 * @param m 选择的个数
	 * @return 排列数
	 */
	public static long count(int n, int m) {
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
	public static long countAll(int n) {
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
	 *
	 * 数量公式：
	 * <pre>
	 * A(n, m) = n! / (n - m)!
	 * </pre>
	 *
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

	public List<String[]> select(int m) {
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
	 *
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
		List<String[]> result = new ArrayList<>();
		for (int m = 1; m <= datas.length; m++) {
			result.addAll(select(m));
		}
		return result;
	}

	/**
	 * 排列选择<br>
	 * 排列方式为先从数据数组中取出一个元素，再把剩余的元素作为新的基数，依次列推，直到选择到足够的元素
	 *
	 * @param datas 选择的基数
	 * @param resultList 前面（resultIndex-1）个的排列结果
	 * @param resultIndex 选择索引，从0开始
	 * @param result 最终结果
	 */
	private void select(String[] datas, String[] resultList, int resultIndex, List<String[]> result) {
		if (resultIndex >= resultList.length) { // 全部选择完时，输出排列结果
			if (false == result.contains(resultList)) {
				result.add(Arrays.copyOf(resultList, resultList.length));
			}
			return;
		}

		// 递归选择下一个
		for (int i = 0; i < datas.length; i++) {
			resultList[resultIndex] = datas[i];
			select(ArrayUtil.remove(datas, i), resultList, resultIndex + 1, result);
		}
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
		private final int n;
		private final int m;
		private final boolean[] visited;
		private final String[] buffer;

		// 每一层记录当前尝试的下标，-1表示还未尝试
		private final int[] indices;
		private int depth;
		private boolean end;

		// 预取下一个元素
		private String[] nextItem;
		private boolean nextPrepared;

		ArrangementIterator(String[] datas, int m) {
			this.datas = datas;
			this.n = datas.length;
			this.m = m;
			this.visited = new boolean[n];
			this.nextItem = null;
			this.nextPrepared = false;

			if (m < 0 || m > n) {
				// 无效或无解，直接结束
				this.indices = new int[Math.max(1, m)];
				this.buffer = new String[Math.max(1, m)];
				this.depth = -1;
				this.end = true;
			} else if (m == 0) {
				// m == 0: 只返回一个空数组
				this.indices = new int[0];
				this.buffer = new String[0];
				this.depth = 0;
				this.end = false;
			} else {
				this.indices = new int[m];
				Arrays.fill(this.indices, -1);
				this.buffer = new String[m];
				this.depth = 0;
				this.end = false;
			}
		}

		@Override
		public boolean hasNext() {
			if (end) return false;
			if (nextPrepared) return nextItem != null;
			prepareNext();
			return nextItem != null;
		}

		@Override
		public String[] next() {
			if (end && !nextPrepared) {
				throw new NoSuchElementException();
			}
			if (!nextPrepared) {
				prepareNext();
			}
			if (nextItem == null) {
				throw new NoSuchElementException();
			}
			String[] ret = nextItem;
			// 清除预取缓存，下一次需要重新准备
			nextItem = null;
			nextPrepared = false;
			// 如果m == 0，该项是唯一项，迭代结束
			if (m == 0) {
				end = true;
			}
			return ret;
		}

		/**
		 * 将状态推进到下一个可返回的排列并把它放入 nextItem。
		 * 如果无更多排列，则将 end=true 并把 nextItem 置为 null。
		 */
		private void prepareNext() {
			// 已经准备过或已结束
			if (nextPrepared || end) {
				nextPrepared = true;
				return;
			}

			// special-case m == 0
			if (m == 0) {
				nextItem = new String[0];
				nextPrepared = true;
				// do not set end here; end will be set after returning this element in next()
				return;
			}

			// 非递归模拟DFS，直到找到一个可返回的排列或穷尽
			while (depth >= 0) {
				int start = indices[depth] + 1;
				boolean found = false;
				for (int i = start; i < n; i++) {
					if (!visited[i]) {
						// 如果当前层之前有选过一个元素，要先取消之前选中的 visited
						if (indices[depth] != -1) {
							visited[indices[depth]] = false;
						}
						indices[depth] = i;
						visited[i] = true;
						buffer[depth] = datas[i];
						found = true;
						break;
					}
				}

				if (!found) {
					// 本层没有可用元素，回溯
					if (indices[depth] != -1) {
						visited[indices[depth]] = false;
						indices[depth] = -1;
					}
					depth--;
					continue;
				}

				// 若已达到输出深度，准备输出（但不抛出）
				if (depth == m - 1) {
					nextItem = Arrays.copyOf(buffer, m);
					// 取消当前visited，为下一次在同一层寻找下一个候选做准备
					visited[indices[depth]] = false;
					// 保持 depth 不变（下一次 prepare 会从 indices[depth]+1 开始寻找）
					nextPrepared = true;
					return;
				} else {
					// 向下一层深入：初始化下一层为-1并继续循环
					depth++;
					if (depth < m) {
						indices[depth] = -1;
					}
				}
			}

			// 若循环结束，说明已经穷尽所有可能
			end = true;
			nextItem = null;
			nextPrepared = true;
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
