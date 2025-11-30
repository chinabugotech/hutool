package cn.hutool.v7.core.collection;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 集合分页工具类
 *
 * <p>用于对内存中的集合进行分页，并返回分页信息和数据。</p>
 *
 * @author Nic
 * @since 2025-11-30
 */
public class CollectionPager {

	/**
	 * 分页结果类
	 *
	 * @param <T> 数据类型
	 */
	public static class Page<T> {
		private final int pageNum;     // 当前页码
		private final int pageSize;    // 每页大小
		private final int totalPage;   // 总页数
		private final int totalCount;  // 总记录数
		private final List<T> data;    // 当前页数据

		public Page(int pageNum, int pageSize, int totalCount, List<T> data) {
			this.pageNum = pageNum;
			this.pageSize = pageSize;
			this.totalCount = totalCount;
			this.totalPage = (int) Math.ceil((double) totalCount / pageSize);
			this.data = data;
		}

		// Getters
		public int getPageNum() {
			return pageNum;
		}

		public int getPageSize() {
			return pageSize;
		}

		public int getTotalPage() {
			return totalPage;
		}

		public int getTotalCount() {
			return totalCount;
		}

		public List<T> getData() {
			return data;
		}

		/**
		 * 是否有上一页
		 */
		public boolean hasPrev() {
			return pageNum > 1;
		}

		/**
		 * 是否有下一页
		 */
		public boolean hasNext() {
			return pageNum < totalPage;
		}

		/**
		 * 获取上一页页码
		 */
		public int getPrevPage() {
			return hasPrev() ? pageNum - 1 : pageNum;
		}

		/**
		 * 获取下一页页码
		 */
		public int getNextPage() {
			return hasNext() ? pageNum + 1 : pageNum;
		}
	}

	/**
	 * 对集合进行分页
	 *
	 * @param <T>        元素类型
	 * @param collection 集合
	 * @param pageNum    页码，从1开始
	 * @param pageSize   每页大小
	 * @return 分页结果对象
	 */
	public static <T> Page<T> paginate(Collection<T> collection, int pageNum, int pageSize) {
		if (collection == null) {
			collection = Collections.emptyList();
		}

		if (pageNum < 1) {
			pageNum = 1;
		}

		if (pageSize < 1) {
			pageSize = 10;
		}

		List<T> list = new ArrayList<>(collection);
		int totalCount = list.size();
		int fromIndex = (pageNum - 1) * pageSize;
		if (fromIndex >= totalCount) {
			return new Page<>(pageNum, pageSize, totalCount, Collections.emptyList());
		}

		int toIndex = Math.min(fromIndex + pageSize, totalCount);
		List<T> pageData = list.subList(fromIndex, toIndex);
		return new Page<>(pageNum, pageSize, totalCount, new ArrayList<>(pageData));
	}
}
