package cn.hutool.v7.cron;

/**
 * 任务表工厂类
 *
 * @author Looly
 */
public class TaskTableFactory {

	/**
	 * 创建任务表
	 *
	 * @param config 定时任务配置
	 * @return 任务表
	 */
	public static TaskTable create(CronConfig config) {
		return config.isUseTriggerQueue() ? new TriggerQueueTaskTable() : new MatchTaskTable();
	}
}
