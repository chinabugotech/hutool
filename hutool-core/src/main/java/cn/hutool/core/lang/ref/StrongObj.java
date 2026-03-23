package cn.hutool.core.lang.ref;

import java.util.Objects;

/**
 * 弱引用对象，在GC时发现弱引用会回收其对象
 *
 * @param <T> 键类型
 */
public class StrongObj<T> implements Ref<T> {

	private final T obj;

	/**
	 * 构造
	 *
	 * @param obj 原始对象
	 */
	public StrongObj(final T obj) {
		this.obj = obj;
	}

	@Override
	public T get() {
		return this.obj;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(obj);
	}

	@Override
	public boolean equals(final Object other) {
		if (other == this) {
			return true;
		} else if (other instanceof StrongObj) {
			return Objects.equals(((StrongObj<?>) other).get(), get());
		}
		return false;
	}
}
