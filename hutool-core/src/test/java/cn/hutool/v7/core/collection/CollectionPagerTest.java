package cn.hutool.v7.core.collection;

import org.junit.jupiter.api.Test;
import java.util.*;

public class CollectionPagerTest {

	@Test
	public void testPage() {
		List<String> list = Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j");
		CollectionPager.Page<String> page = CollectionPager.paginate(list, 2, 3);

		// 获取第2页的数据，每页3条，那么应该返回：["d","e","f"]
		System.out.println(page.getData()); // [d, e, f]
		System.out.println(page.getPageNum()); // 2
		System.out.println(page.getPageSize()); // 3
		System.out.println(page.getTotalCount()); // 10
		System.out.println(page.getTotalPage()); // 4
	}
}
