package cn.hutool.core.util;

import cn.hutool.core.collection.ListUtil;


import java.util.*;



/**
 * SPI机制中的服务加载工具类，流程如下
 *
 * <pre>
 *     1、创建接口，并创建实现类
 *     2、ClassPath/META-INF/services下创建与接口全限定类名相同的文件
 *     3、文件内容填写实现类的全限定类名
 * </pre>
 * 相关介绍见：https://www.jianshu.com/p/3a3edbcd8f24
 *
 * @author looly
 * @since 5.1.6
 */
public class ServiceLoaderUtil {

	/**
	 * 加载第一个可用的 Service 实现。
	 * <p>
	 * 为兼容 JDK 24+ 中 {@link ServiceLoader} 在加载服务实现时可能抛出的 {@link NoClassDefFoundError}，
	 * 此方法在调用 {@code hasNext()} 和 {@code next()} 时安全忽略异常。
	 * 当遇到依赖缺失或配置错误的实现时会自动跳过，并返回第一个可用的非空实例。
	 * </p>
	 *
	 * @param clazz 服务接口类型
	 * @param <T>   服务实现类型
	 * @return 第一个可用的非空实现，若无可用实现则返回 {@code null}
	 * @since 5.8.41 （JDK 24+ 兼容优化）
	 * @see <a href="https://bugs.openjdk.org/browse/JDK-8350481">JDK-8350481</a>
	 */
	public static <T> T loadFirstAvailable(Class<T> clazz) {
		final Iterator<T> iterator = load(clazz).iterator();
		while (true) {
			T instance;
			try {
				// 注意：JDK 24+ 下 hasNext() 和 next() 均可能触发 NoClassDefFoundError
				if (!iterator.hasNext()) {
					break;
				}
				instance = iterator.next();
			} catch (ServiceConfigurationError | NoClassDefFoundError e) {
				// 安全忽略当前实现，尝试下一个
				continue;
			}
			if (instance != null) {
				return instance;
			}
		}
		return null;
	}

	/**
	 * 加载第一个服务，如果用户定义了多个接口实现类，只获取第一个。
	 *
	 * @param <T>   接口类型
	 * @param clazz 服务接口
	 * @return 第一个服务接口实现对象，无实现返回{@code null}
	 */
	public static <T> T loadFirst(Class<T> clazz) {
		final Iterator<T> iterator = load(clazz).iterator();
		if (iterator.hasNext()) {
			return iterator.next();
		}
		return null;
	}

	/**
	 * 加载服务
	 *
	 * @param <T>   接口类型
	 * @param clazz 服务接口
	 * @return 服务接口实现列表
	 */
	public static <T> ServiceLoader<T> load(Class<T> clazz) {
		return load(clazz, null);
	}

	/**
	 * 加载服务
	 *
	 * @param <T>    接口类型
	 * @param clazz  服务接口
	 * @param loader {@link ClassLoader}
	 * @return 服务接口实现列表
	 */
	public static <T> ServiceLoader<T> load(Class<T> clazz, ClassLoader loader) {
		return ServiceLoader.load(clazz, ObjectUtil.defaultIfNull(loader, ClassLoaderUtil::getClassLoader));
	}

	/**
	 * 加载服务 并已list列表返回
	 *
	 * @param <T>   接口类型
	 * @param clazz 服务接口
	 * @return 服务接口实现列表
	 * @since 5.4.2
	 */
	public static <T> List<T> loadList(Class<T> clazz) {
		return loadList(clazz, null);
	}

	/**
	 * 加载服务 并已list列表返回
	 *
	 * @param <T>    接口类型
	 * @param clazz  服务接口
	 * @param loader {@link ClassLoader}
	 * @return 服务接口实现列表
	 * @since 5.4.2
	 */
	public static <T> List<T> loadList(Class<T> clazz, ClassLoader loader) {
		return ListUtil.list(false, load(clazz, loader));
	}
}
