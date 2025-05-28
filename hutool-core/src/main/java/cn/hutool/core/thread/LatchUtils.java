package cn.hutool.core.thread;


import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * CountDownLatch 工具类，实现对CountDownLatch类的创建以及 countDown 和 await方法的封装
 * 实现对业务代码和CountDownLatch相关操作的隔离，避免业务代码与CountDownLatch的耦合，增加线程并行等待的安全性
 * @author wwwzzzggg
 */
public class LatchUtils {

	private static final ThreadLocal<List<TaskInfo>> THREADLOCAL = ThreadLocal.withInitial(LinkedList::new);

	private static final class TaskInfo {
		private Executor executor;
		private Runnable runnable;

		public TaskInfo(Executor executor, Runnable runnable) {
			this.executor = executor;
			this.runnable = runnable;
		}
	}

	public static void submitTask(Executor executor, Runnable runnable) {
		THREADLOCAL.get().add(new TaskInfo(executor, runnable));
	}

	private static List<TaskInfo> popTask() {
		List<TaskInfo> taskInfos = THREADLOCAL.get();
		THREADLOCAL.remove();
		return taskInfos;
	}

	public static boolean waitFor(Long timeout, TimeUnit timeUnit) {
		List<TaskInfo> taskInfos = popTask();
		if (taskInfos.isEmpty()) {
			return true;
		}

		CountDownLatch latch = new CountDownLatch(taskInfos.size());
		for (TaskInfo taskInfo : taskInfos) {
			Executor executor = taskInfo.executor;
			Runnable runnable = taskInfo.runnable;
			executor.execute(() -> {
				try {
					runnable.run();
				} finally {
					latch.countDown();
				}
			});
		}

		boolean await = false;
		try {
			if (timeout != null) {
				await = latch.await(timeout, timeUnit != null ? timeUnit : TimeUnit.SECONDS);
			} else {
				latch.await();
				await = true;
			}
		} catch (Exception e) {

		}

		return await;
	}

}
