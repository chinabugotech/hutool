package cn.hutool.core.lang.ref;

/**
 * 针对{@link java.lang.ref.Reference}的接口定义，用于扩展功能<br>
 * 例如提供自定义的无需回收对象
 *
 * @param <T> 对象类型
 */
@FunctionalInterface
public interface Ref<T> {

	/**
	 * 获取引用的原始对象
	 *
	 * @return 原始对象
	 */
	T get();
}
