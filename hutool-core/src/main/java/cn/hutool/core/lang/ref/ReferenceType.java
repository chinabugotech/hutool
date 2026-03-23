package cn.hutool.core.lang.ref;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;

/**
 * 引用类型
 *
 * @author Looly
 */
public enum ReferenceType {
	/**
	 * 强引用，不回收
	 */
	STRONG,
	/**
	 * 软引用，在GC报告内存不足时会被GC回收
	 */
	SOFT,
	/**
	 * 弱引用，在GC时发现弱引用会回收其对象
	 */
	WEAK,
	/**
	 * 虚引用，在GC时发现虚引用对象，会将{@link PhantomReference}插入{@link ReferenceQueue}。 <br>
	 * 此时对象未被真正回收，要等到{@link ReferenceQueue}被真正处理后才会被回收。
	 */
	PHANTOM
}
