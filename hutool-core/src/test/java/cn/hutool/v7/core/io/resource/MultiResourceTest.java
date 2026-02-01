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

package cn.hutool.v7.core.io.resource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * MultiResource Iterator接口功能单元测试
 */
class MultiResourceTest {

	private MultiResource multiResource;

	@BeforeEach
	void setUp() {
		// 创建具体的Resource实现用于测试
		final TestResource testResource1 = new TestResource("resource1", "content1");
		final TestResource testResource2 = new TestResource("resource2", "content2");
		final TestResource testResource3 = new TestResource("resource3", "content3");

		final List<Resource> resourceList = new ArrayList<>();
		resourceList.add(testResource1);
		resourceList.add(testResource2);
		resourceList.add(testResource3);

		multiResource = new MultiResource(resourceList);
	}

	/**
	 * 测试hasNext方法 - 初始状态
	 */
	@Test
	void testHasNextInitial() {
		// 初始游标为-1，有效游标为0，所以应该有下一个元素
		assertTrue(multiResource.hasNext(), "初始状态应该有下一个元素");
	}

	/**
	 * 测试hasNext方法 - 中间状态
	 */
	@Test
	void testHasNextMiddle() {
		// 调用一次next()后，游标变为0，还有剩余元素
		multiResource.next();
		assertTrue(multiResource.hasNext(), "中间状态应该还有下一个元素");

		// 再调用两次next()，游标变为2，还应有下一个元素
		multiResource.next();
		multiResource.next();
		assertTrue(multiResource.hasNext(), "接近末尾时应该还有下一个元素");
	}

	/**
	 * 测试hasNext方法 - 末尾状态
	 */
	@Test
	void testHasNextEnd() {
		// 连续调用next()直到超出范围
		multiResource.next(); // 游标: 0
		multiResource.next(); // 游标: 1
		multiResource.next(); // 游标: 2
		multiResource.next(); // 游标: 3

		assertFalse(multiResource.hasNext(), "超出范围后不应该有下一个元素");
	}

	/**
	 * 测试next方法 - 正常遍历
	 */
	@Test
	void testNextNormalTraversal() {
		// 第一次调用next()
		final Resource result1 = multiResource.next();
		assertEquals(multiResource, result1, "next()返回的应该是自身实例");
		assertEquals(0, getCursorValue(multiResource), "游标应该更新为0");

		// 验证getName方法使用的是第一个资源
		assertEquals("resource1", multiResource.name(), "getName应该返回第一个资源名称");

		// 第二次调用next()
		final Resource result2 = multiResource.next();
		assertEquals(multiResource, result2, "next()返回的应该是自身实例");
		assertEquals(1, getCursorValue(multiResource), "游标应该更新为1");

		// 验证getName方法使用的是第二个资源
		assertEquals("resource2", multiResource.name(), "getName应该返回第二个资源名称");
	}

	/**
	 * 测试next方法 - 超出边界异常
	 */
	@Test
	void testNextBoundaryException() {
		// 遍历所有资源
		multiResource.next(); // 游标: 0
		multiResource.next(); // 游标: 1
		multiResource.next(); // 游标: 2
		multiResource.next(); // 游标: 3

		// 再次调用next()应该抛出异常
		assertThrows(ConcurrentModificationException.class, () -> {
			multiResource.next();
		}, "超出边界时调用next()应该抛出ConcurrentModificationException");
	}

	/**
	 * 测试remove方法
	 */
	@Test
	void testRemove() {
		// 先移动游标到第一个位置
		multiResource.next(); // 游标: 0
		assertEquals(3, getInternalSize(multiResource), "初始资源数量应该是3");

		// 执行删除操作
		multiResource.remove();

		assertEquals(2, getInternalSize(multiResource), "删除后资源数量应该是2");
		assertEquals("resource2", multiResource.name(), "删除后getName应该返回第二个资源名称");
	}

	/**
	 * 测试iterator方法
	 */
	@Test
	void testIterator() {
		final var iterator = multiResource.iterator();
		assertNotNull(iterator, "iterator方法不应返回null");

		// 验证迭代器可以遍历所有资源
		int count = 0;
		while (iterator.hasNext()) {
			final Resource resource = iterator.next();
			assertNotNull(resource, "迭代器返回的资源不应为null");
			count++;
		}

		assertEquals(3, count, "迭代器应该能遍历所有3个资源");
	}

	/**
	 * 测试reset方法
	 */
	@Test
	void testReset() {
		// 移动游标到中间位置
		multiResource.next(); // 游标: 0
		multiResource.next(); // 游标: 1
		assertEquals(1, getCursorValue(multiResource), "游标应该为1");

		// 重置游标
		multiResource.reset();
		assertEquals(-1, getCursorValue(multiResource), "重置后游标应该为-1");

		// 重置后应该能够重新开始遍历
		assertTrue(multiResource.hasNext(), "重置后应该有下一个元素");
		final Resource result = multiResource.next();
		assertEquals(multiResource, result, "重置后next()应该返回自身");
		assertEquals(0, getCursorValue(multiResource), "重置后第一次next()后游标应该为0");
	}

	/**
	 * 测试并发安全的同步方法
	 */
	@Test
	void testSynchronizedMethods() {
		// 由于无法直接测试同步块，我们验证方法可以正常调用
		assertDoesNotThrow(() -> {
			multiResource.reset();
		}, "reset方法应该可以正常调用");

		assertDoesNotThrow(() -> {
			multiResource.next();
		}, "next方法应该可以正常调用");
	}

	/**
	 * 测试添加资源后的迭代行为
	 */
	@Test
	void testAddResourceThenIterate() {
		final TestResource newResource = new TestResource("newResource", "newContent");

		// 添加新资源
		multiResource.add(newResource);

		// 验证可以遍历新增的资源
		// 先遍历原有的3个资源
		multiResource.next(); // 游标: 0
		multiResource.next(); // 游标: 1
		multiResource.next(); // 游标: 2
		multiResource.next(); // 游标: 3

		// 现在应该还有元素（第4个）
		assertTrue(multiResource.hasNext(), "添加资源后应该还有元素");

		multiResource.next(); // 游标: 4
		assertEquals(4, getInternalSize(multiResource), "资源总数应该是4");
	}

	/**
	 * 测试addAll方法
	 */
	@Test
	void testAddAllResources() {
		final TestResource extraResource1 = new TestResource("extra1", "extraContent1");
		final TestResource extraResource2 = new TestResource("extra2", "extraContent2");

		final List<Resource> extraResources = List.of(extraResource1, extraResource2);

		// 添加多个资源
		multiResource.addAll(extraResources);

		assertEquals(5, getInternalSize(multiResource), "添加多个资源后总数应该是5");
	}

	// 辅助方法：通过反射获取私有字段值
	private int getCursorValue(final MultiResource multiResource) {
		try {
			final var field = MultiResource.class.getDeclaredField("cursor");
			field.setAccessible(true);
			return field.getInt(multiResource);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	private int getInternalSize(final MultiResource multiResource) {
		try {
			final var field = MultiResource.class.getDeclaredField("resources");
			field.setAccessible(true);
			final List<?> resources = (List<?>) field.get(multiResource);
			return resources.size();
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 测试Resource接口的实现类，用于单元测试
	 */
		private record TestResource(String name, String content) implements Resource {
		@Override
			public URL getUrl() {
				try {
					return new URL("http://example.com/" + name);
				} catch (final MalformedURLException e) {
					throw new RuntimeException(e);
				}
			}

			@Override
			public long size() {
				return content.getBytes(StandardCharsets.UTF_8).length;
			}

			@Override
			public InputStream getStream() {
				return new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
			}

			@Override
			public boolean isModified() {
				return false;
			}
		}
}
