package cn.hutool.core.thread;

import cn.hutool.core.thread.RecyclableBatchThreadPoolExecutor.Warp;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;

/**
 * {@link RecyclableBatchThreadPoolExecutor} 测试类
 */
public class RecyclableBatchThreadPoolExecutorTest {


	/**
	 * 批量处理数据
	 */
	@Test
	@Disabled
	public void test() throws InterruptedException {
		int corePoolSize = 10;// 线程池大小
		int batchSize = 100;// 每批次数据量
		int clientCount = 30;// 调用者数量
		test(corePoolSize,batchSize,clientCount);
	}

	/**
	 * 普通查询接口加速
	 */
	@Test
	@Disabled
	public void test2() {
		RecyclableBatchThreadPoolExecutor executor = new RecyclableBatchThreadPoolExecutor(10);
		long s = System.nanoTime();
		Warp<String> warp1 = Warp.of(this::select1);
		Warp<List<String>> warp2 = Warp.of(this::select2);
		executor.processByWarp(warp1, warp2);
		Map<String, Object> map = new HashMap<>();
		map.put("key1",warp1.get());
		map.put("key2",warp2.get());
		long d = System.nanoTime() - s;
		System.out.printf("总耗时：%.2f秒%n",d/1e9);
		System.out.println(map);
	}

	public void test(int corePoolSize,int batchSize,int clientCount ) throws InterruptedException{
		RecyclableBatchThreadPoolExecutor processor = new RecyclableBatchThreadPoolExecutor(corePoolSize);
		// 模拟多个调用者线程提交任务
		ExecutorService testExecutor = Executors.newFixedThreadPool(clientCount);
		Map<Integer, List<Integer>> map = new HashMap<>();
		for(int i = 0; i < clientCount; i++){
			map.put(i,testDate(1000));
		}
		long s = System.nanoTime();
		List<Future<?>> futures = new ArrayList<>();
		for (int j = 0; j < clientCount; j++) {
			final int clientId = j;
			Future<?> submit = testExecutor.submit(() -> {
				Function<Integer, String> function = p -> {
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
					return Thread.currentThread().getName() + "#" + p;
				};
				long start = System.nanoTime();
				List<String> process = processor.process(map.get(clientId), batchSize, function);
				long duration = System.nanoTime() - start;
				System.out.printf("【clientId：%s】处理结果：%s\n处理耗时：%.2f秒%n", clientId, process, duration / 1e9);
			});
			futures.add(submit);
		}
		futures.forEach(p-> {
			try {
				p.get();
			} catch (InterruptedException | ExecutionException e) {
				throw new RuntimeException(e);
			}
		});
		long d = System.nanoTime() - s;
		System.out.printf("总耗时：%.2f秒%n",d/1e9);
		testExecutor.shutdown();
		processor.shutdown();
	}
	public static List<Integer> testDate(int count){
		List<Integer> list = new ArrayList<>();
		for(int i = 1;i<=count;i++){
			list.add(i);
		}
		return list;
	}

	private String select1()  {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		return "1";
	}

	private List<String> select2() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		return Arrays.asList("1","2","3");
	}

}
