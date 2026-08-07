package cn.hutool.core.collection;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.lang.Assert;

import java.util.List;
import java.util.function.Consumer;

/**
 * 集合批量处理工具，支持批次、降级和重试。
 *
 * <p>典型用法：批量写入数据库时，某一批失败则对该批逐条降级处理，
 * 单条仍失败则再尝试一次降级重试。
 *
 * <p>示例：
 * <pre>{@code
 *   BatchUtil.handle(list, 500,
 *       batch -> jdbc.batchInsert(batch),
 *       batch -> jdbc.batchInsert(batch), // 重试批处理
 *       item -> jdbc.insert(item),
 *       item -> jdbc.insert(item));      // 重试单条处理
 * }</pre>
 *
 * <p>注意：本工具按批大小切片后逐批处理，每批为原列表的独立副本，
 * 处理过程中外部修改原列表不会影响当前批次。
 *
 * @author wangzaixu
 */
public final class BatchUtil {

	/** 默认批大小 */
	public static final int DEFAULT_BATCH_SIZE = 500;

	private BatchUtil() {
	}

	/**
	 * 使用默认批大小（{@link #DEFAULT_BATCH_SIZE}）执行批量处理。
	 *
	 * @param list          待处理集合
	 * @param batchConsumer 批处理方法，不能为 {@code null}
	 * @param onceConsumer  单条降级处理方法，不能为 {@code null}
	 * @param <T>           元素类型
	 */
	public static <T> void handle(List<T> list,
	                              Consumer<List<T>> batchConsumer,
	                              Consumer<T> onceConsumer) {
		handle(list, DEFAULT_BATCH_SIZE, batchConsumer, null, onceConsumer, null);
	}

	/**
	 * 按指定批大小分页执行批量处理，每页失败则降级为单条处理（不进行重试）。
	 *
	 * @param list          待处理集合，为 {@code null} 或空集合则直接返回
	 * @param batchSize     每批大小，必须大于 0
	 * @param batchConsumer 批处理方法，不能为 {@code null}
	 * @param onceConsumer  单条降级处理方法，不能为 {@code null}
	 * @param <T>           元素类型
	 * @throws IllegalArgumentException 当 {@code batchSize <= 0}、{@code batchConsumer} 为 {@code null} 或 {@code onceConsumer} 为 {@code null}
	 */
	public static <T> void handle(List<T> list, int batchSize,
	                              Consumer<List<T>> batchConsumer,
	                              Consumer<T> onceConsumer) {
		handle(list, batchSize, batchConsumer, null, onceConsumer, null);
	}

	/**
	 * 按指定批大小分页执行批量处理，每页失败则降级为单条处理，单条失败则再尝试一次降级重试。
	 *
	 * <p>执行流程：
	 * <ol>
	 *   <li>按 {@code batchSize} 切片后执行 {@code batchConsumer}</li>
	 *   <li>若失败，且 {@code retryBatchConsumer} 非空，再尝试一次批处理</li>
	 *   <li>若仍失败（或没有批重试），将该批逐条执行 {@code onceConsumer}</li>
	 *   <li>单条仍失败时，若 {@code retryOnceConsumer} 非空，再尝试一次</li>
	 *   <li>仅当 {@code retryOnceConsumer} 非空且重试仍失败时，最终抛出 {@link RuntimeException}</li>
	 * </ol>
	 *
	 * <p>注意：当未提供 {@code retryOnceConsumer} 时，单条处理失败将被静默忽略。
	 * 调用方需自行保证 {@code onceConsumer} 的可靠性，或显式提供 {@code retryOnceConsumer} 以接收失败。
	 *
	 * @param list               待处理集合，为 {@code null} 或空集合则直接返回
	 * @param batchSize          每批大小，必须大于 0
	 * @param batchConsumer      批处理方法，不能为 {@code null}
	 * @param retryBatchConsumer 批处理重试方法，允许为 {@code null}
	 * @param onceConsumer       单条降级处理方法，不能为 {@code null}
	 * @param retryOnceConsumer  单条降级重试方法，允许为 {@code null}
	 * @param <T>                元素类型
	 * @throws IllegalArgumentException 当 {@code batchSize <= 0}、{@code batchConsumer} 为 {@code null} 或 {@code onceConsumer} 为 {@code null}
	 */
	public static <T> void handle(List<T> list, int batchSize,
	                              Consumer<List<T>> batchConsumer,
	                              Consumer<List<T>> retryBatchConsumer,
	                              Consumer<T> onceConsumer,
	                              Consumer<T> retryOnceConsumer) {
		if (CollUtil.isEmpty(list)) {
			return;
		}
		Assert.isTrue(batchSize > 0, "batchSize must be > 0, but was: {}", batchSize);
		Assert.notNull(batchConsumer, "batchConsumer must not be null");
		Assert.notNull(onceConsumer, "onceConsumer must not be null");
		ListUtil.page(list, batchSize, page -> handlePage(page, batchConsumer, retryBatchConsumer, onceConsumer, retryOnceConsumer));
	}

	/**
	 * 处理一批数据：先调用批处理消费者，失败后降级为单条处理或批重试处理
	 *
	 * @param page               一批数据视图
	 * @param batchConsumer      批处理方法
	 * @param retryBatchConsumer 批处理重试方法，可为 {@code null}
	 * @param onceConsumer       单条处理方法
	 * @param retryOnceConsumer  单条重试方法，可为 {@code null}
	 * @param <T>                元素类型
	 */
	private static <T> void handlePage(List<T> page,
	                                   Consumer<List<T>> batchConsumer,
	                                   Consumer<List<T>> retryBatchConsumer,
	                                   Consumer<T> onceConsumer,
	                                   Consumer<T> retryOnceConsumer) {
		try {
			batchConsumer.accept(page);
		} catch (Exception e) {
			if (retryBatchConsumer != null) {
				retryBatchHandle(page, retryBatchConsumer, onceConsumer, retryOnceConsumer);
			} else {
				for (T item : page) {
					handleOnce(item, onceConsumer, retryOnceConsumer);
				}
			}
		}
	}

	/**
	 * 批重试处理：失败后将该批拆为单条处理
	 *
	 * @param page               一批数据视图
	 * @param retryBatchConsumer 批重试方法
	 * @param onceConsumer       单条处理方法
	 * @param retryOnceConsumer  单条重试方法，可为 {@code null}
	 * @param <T>                元素类型
	 */
	private static <T> void retryBatchHandle(List<T> page,
	                                         Consumer<List<T>> retryBatchConsumer,
	                                         Consumer<T> onceConsumer,
	                                         Consumer<T> retryOnceConsumer) {
		try {
			retryBatchConsumer.accept(page);
		} catch (Exception e) {
			for (T item : page) {
				handleOnce(item, onceConsumer, retryOnceConsumer);
			}
		}
	}

	/**
	 * 单条处理：失败后调用单条重试方法（若存在）
	 *
	 * @param item              单条数据
	 * @param onceConsumer      单条处理方法
	 * @param retryOnceConsumer 单条重试方法，可为 {@code null}
	 * @param <T>               元素类型
	 */
	private static <T> void handleOnce(T item,
	                                   Consumer<T> onceConsumer,
	                                   Consumer<T> retryOnceConsumer) {
		if (item == null) {
			return;
		}
		try {
			onceConsumer.accept(item);
		} catch (Exception e) {
			if (retryOnceConsumer != null) {
				retryOnceHandle(item, retryOnceConsumer);
			}
		}
	}

	/**
	 * 单条重试处理：最终失败抛出原始异常
	 *
	 * @param item              单条数据
	 * @param retryOnceConsumer 单条重试方法
	 * @param <T>               元素类型
	 */
	private static <T> void retryOnceHandle(T item, Consumer<T> retryOnceConsumer) {
		if (item == null) {
			return;
		}
		try {
			retryOnceConsumer.accept(item);
		} catch (Exception e) {
			ExceptionUtil.wrapAndThrow(e);
		}
	}
}
