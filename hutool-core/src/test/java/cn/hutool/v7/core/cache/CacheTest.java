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

package cn.hutool.v7.core.cache;

import cn.hutool.v7.core.cache.impl.TimedCache;
import cn.hutool.v7.core.date.DateUnit;
import cn.hutool.v7.core.thread.ThreadUtil;
import cn.hutool.v7.core.util.RandomUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * 缓存测试用例
 * @author Looly
 *
 */
public class CacheTest {

	@Test
	public void fifoCacheTest(){
		final Cache<String,String> fifoCache = CacheUtil.newFIFOCache(3);
		fifoCache.setListener((key, value)->{
			// 监听测试，此测试中只有key1被移除，测试是否监听成功
			assertEquals("key1", key);
			assertEquals("value1", value);
		});

		fifoCache.put("key1", "value1", DateUnit.SECOND.getMillis() * 3);
		fifoCache.put("key2", "value2", DateUnit.SECOND.getMillis() * 3);
		fifoCache.put("key3", "value3", DateUnit.SECOND.getMillis() * 3);
		fifoCache.put("key4", "value4", DateUnit.SECOND.getMillis() * 3);

		//由于缓存容量只有3，当加入第四个元素的时候，根据FIFO规则，最先放入的对象将被移除
		final String value1 = fifoCache.get("key1");
		Assertions.assertNull(value1);
	}

	@Test
	public void fifoCacheCapacityTest(){
		final Cache<String,String> fifoCache = CacheUtil.newFIFOCache(100);
		for (int i = 0; i < RandomUtil.randomInt(100, 1000); i++) {
			fifoCache.put("key" + i, "value" + i);
		}
		assertEquals(100, fifoCache.size());
	}

	@Test
	public void lfuCacheTest(){
		final Cache<String, String> lfuCache = CacheUtil.newLFUCache(3);
		lfuCache.put("key1", "value1", DateUnit.SECOND.getMillis() * 3);
		//使用次数+1
		lfuCache.get("key1");
		lfuCache.put("key2", "value2", DateUnit.SECOND.getMillis() * 3);
		lfuCache.put("key3", "value3", DateUnit.SECOND.getMillis() * 3);
		lfuCache.put("key4", "value4", DateUnit.SECOND.getMillis() * 3);

		//由于缓存容量只有3，当加入第四个元素的时候，根据LFU规则，最少使用的将被移除（2,3被移除）
		final String value1 = lfuCache.get("key1");
		final String value2 = lfuCache.get("key2");
		final String value3 = lfuCache.get("key3");
		Assertions.assertNotNull(value1);
		Assertions.assertNull(value2);
		Assertions.assertNull(value3);
	}

	@Test
	public void lfuCacheTest2(){
		final Cache<String, String> lfuCache = CacheUtil.newLFUCache(3);
		final String s = lfuCache.get(null);
		Assertions.assertNull(s);
	}

	@Test
	public void lruCacheTest(){
		final Cache<String, String> lruCache = CacheUtil.newLRUCache(3);
		//通过实例化对象创建
//		LRUCache<String, String> lruCache = new LRUCache<String, String>(3);
		lruCache.put("key1", "value1", DateUnit.SECOND.getMillis() * 3);
		lruCache.put("key2", "value2", DateUnit.SECOND.getMillis() * 3);
		lruCache.put("key3", "value3", DateUnit.SECOND.getMillis() * 3);
		//使用时间推近
		lruCache.get("key1");
		lruCache.put("key4", "value4", DateUnit.SECOND.getMillis() * 3);

		final String value1 = lruCache.get("key1");
		Assertions.assertNotNull(value1);
		//由于缓存容量只有3，当加入第四个元素的时候，根据LRU规则，最少使用的将被移除（2被移除）
		final String value2 = lruCache.get("key2");
		Assertions.assertNull(value2);
	}

	@Test
	public void timedCacheTest(){
		final TimedCache<String, String> timedCache = CacheUtil.newTimedCache(4);
//		TimedCache<String, String> timedCache = new TimedCache<String, String>(DateUnit.SECOND.getMillis() * 3);
		timedCache.put("key1", "value1", 1);//1毫秒过期
		timedCache.put("key2", "value2", DateUnit.SECOND.getMillis() * 5);//5秒过期
		timedCache.put("key3", "value3");//默认过期(4毫秒)
		timedCache.put("key4", "value4", Long.MAX_VALUE);//永不过期

		//启动定时任务，每5毫秒秒检查一次过期
		timedCache.schedulePrune(5);
		//等待5毫秒
		ThreadUtil.sleep(5);

		//5毫秒后由于value2设置了5毫秒过期，因此只有value2被保留下来
		final String value1 = timedCache.get("key1");
		Assertions.assertNull(value1);
		final String value2 = timedCache.get("key2");
		assertEquals("value2", value2);

		//5毫秒后，由于设置了默认过期，key3只被保留4毫秒，因此为null
		final String value3 = timedCache.get("key3");
		Assertions.assertNull(value3);

		final String value3Supplier = timedCache.get("key3", () -> "Default supplier");
		assertEquals("Default supplier", value3Supplier);

		// 永不过期
		final String value4 = timedCache.get("key4");
		assertEquals("value4", value4);

		//取消定时清理
		timedCache.cancelPruneSchedule();
	}

	/**
	 * TimedCache的数据过期后不是每次都触发监听器onRemove，而是偶尔触发onRemove
	 * https://gitee.com/chinabugotech/hutool/issues/IBP752
	 */
	@Test
	public void whenContainsKeyTimeout_shouldCallOnRemove() {
		final int timeout = 50;
		final TimedCache<Integer, String> ALARM_CACHE = new TimedCache<>(timeout);

		final AtomicInteger counter = new AtomicInteger(0);
		ALARM_CACHE.setListener((key, value) -> {
			counter.incrementAndGet();
		});

		ALARM_CACHE.put(1, "value1");

		ThreadUtil.sleep(100);

		assertFalse(ALARM_CACHE.containsKey(1));
		assertEquals(1, counter.get());
	}

	/**
	 * ReentrantCache类clear()方法、AbstractCache.putWithoutLock方法可能导致资源泄露
	 * https://github.com/chinabugotech/hutool/issues/3957
	 */
	@Test
	public void reentrantCache_clear_Method_Test() {
		AtomicInteger removeCount = new AtomicInteger();
		Cache<String, String> lruCache = CacheUtil.newLRUCache(4);
		lruCache.setListener((key, cachedObject) -> removeCount.getAndIncrement());
		lruCache.put("key1","String1");
		lruCache.put("key2","String2");
		lruCache.put("key3","String3");
		lruCache.put("key1","String4");//key已经存在，原始putWithoutLock方法存在资源泄露
		lruCache.put("key4","String5");
		lruCache.clear();//ReentrantCache类clear()方法存在资源泄露
		Assertions.assertEquals(5, removeCount.get());
	}
}
