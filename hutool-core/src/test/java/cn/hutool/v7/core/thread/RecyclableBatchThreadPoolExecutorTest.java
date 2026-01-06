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

package cn.hutool.v7.core.thread;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import cn.hutool.v7.core.thread.RecyclableBatchThreadPoolExecutor.Warp;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;

public class RecyclableBatchThreadPoolExecutorTest {

	/**
	 * 批量处理数据
	 */
	@Test
	@Disabled
	public void test() throws InterruptedException {
		final int corePoolSize = 10;// 线程池大小
		final int batchSize = 100;// 每批次数据量
		final int clientCount = 30;// 调用者数量
		test(corePoolSize,batchSize,clientCount);
	}

	/**
	 * 普通查询接口加速
	 */
	@Test
	@Disabled
	public void test2() {
		final RecyclableBatchThreadPoolExecutor executor = new RecyclableBatchThreadPoolExecutor(10);
		final long s = System.nanoTime();
		final Warp<String> warp1 = Warp.of(this::select1);
		final Warp<List<String>> warp2 = Warp.of(this::select2);
		executor.processByWarp(warp1, warp2);
		final Map<String, Object> map = new HashMap<>();
		map.put("key1",warp1.get());
		map.put("key2",warp2.get());
		final long d = System.nanoTime() - s;
		System.out.printf("总耗时：%.2f秒%n",d/1e9);
		System.out.println(map);
	}

	public void test(final int corePoolSize, final int batchSize, final int clientCount ) throws InterruptedException{
		final RecyclableBatchThreadPoolExecutor processor = new RecyclableBatchThreadPoolExecutor(corePoolSize);
		// 模拟多个调用者线程提交任务
		final ExecutorService testExecutor = Executors.newFixedThreadPool(clientCount);
		final Map<Integer, List<Integer>> map = new HashMap<>();
		for(int i = 0; i < clientCount; i++){
			map.put(i,testDate(1000));
		}
		final long s = System.nanoTime();
		final List<Future<?>> futures = new ArrayList<>();
		for (int j = 0; j < clientCount; j++) {
			final int clientId = j;
			final Future<?> submit = testExecutor.submit(() -> {
				final Function<Integer, String> function = p -> {
					try {
						Thread.sleep(10);
					} catch (final InterruptedException e) {
						throw new RuntimeException(e);
					}
					return Thread.currentThread().getName() + "#" + p;
				};
				final long start = System.nanoTime();
				final List<String> process = processor.process(map.get(clientId), batchSize, function);
				final long duration = System.nanoTime() - start;
				System.out.printf("【clientId：%s】处理结果：%s\n处理耗时：%.2f秒%n", clientId, process, duration / 1e9);
			});
			futures.add(submit);
		}
		futures.forEach(p-> {
			try {
				p.get();
			} catch (final InterruptedException | ExecutionException e) {
				throw new RuntimeException(e);
			}
		});
		final long d = System.nanoTime() - s;
		System.out.printf("总耗时：%.2f秒%n",d/1e9);
		testExecutor.shutdown();
		processor.shutdown();
	}
	public static List<Integer> testDate(final int count){
		final List<Integer> list = new ArrayList<>();
		for(int i = 1;i<=count;i++){
			list.add(i);
		}
		return list;
	}

	private String select1()  {
		try {
			Thread.sleep(3000);
		} catch (final InterruptedException e) {
			throw new RuntimeException(e);
		}
		return "1";
	}

	private List<String> select2() {
		try {
			Thread.sleep(5000);
		} catch (final InterruptedException e) {
			throw new RuntimeException(e);
		}
		return Arrays.asList("1","2","3");
	}
}
