/*
 * Copyright (c) 2026 Hutool Team.
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

package cn.hutool.v7.core.func;

import java.util.function.*;

/**
 * 函数工具类
 *
 * @author Looly
 * @since 7.0.0
 */
public class FuncUtil {

	/**
	 * 获取函数返回值<br>
	 * 如果函数为{@code null} 返回{@code null}
	 *
	 * @param supplier 函数
	 * @param <T>      值类型
	 * @return 值
	 */
	public static <T> T get(final Supplier<T> supplier) {
		return supplier == null ? null : supplier.get();
	}

	/**
	 * 获取函数返回值<br>
	 * 如果函数为{@code null} 忽略函数执行
	 *
	 * @param operator 函数
	 * @param t        参数
	 * @param <T>      值类型
	 * @return 值
	 */
	public static <T> T apply(final UnaryOperator<T> operator, final T t) {
		return operator == null ? null : operator.apply(t);
	}

	/**
	 * 获取函数返回值<br>
	 * 如果函数为{@code null} 忽略函数执行
	 *
	 * @param function 函数
	 * @param t        参数
	 * @param <T>      参数类型
	 * @param <R>      返回值类型
	 * @return 值
	 */
	public static <T, R> R apply(final Function<T, R> function, final T t) {
		return function == null ? null : function.apply(t);
	}

	/**
	 * 测试函数<br>
	 * 如果函数为{@code null} 忽略函数执行
	 *
	 * @param predicate 函数
	 * @param t         参数
	 * @param <T>       值类型
	 * @return 值
	 */
	public static <T> boolean test(final Predicate<T> predicate, final T t) {
		return predicate != null && predicate.test(t);
	}

	/**
	 * 测试函数<br>
	 * 如果函数为{@code null} 忽略函数执行
	 *
	 * @param predicate 函数
	 * @param t         参数
	 * @param u         参数
	 * @param <T>       值类型
	 * @param <U>       值类型
	 * @return 值
	 */
	public static <T, U> boolean test(final BiPredicate<T, U> predicate, final T t, final U u) {
		return predicate != null && predicate.test(t, u);
	}

	/**
	 * 执行函数<br>
	 * 如果函数为{@code null} 忽略函数执行
	 *
	 * @param consumer 函数
	 * @param t        参数
	 * @param <T>      值类型
	 */
	public static <T> void accept(final Consumer<T> consumer, final T t) {
		if (consumer != null) {
			consumer.accept(t);
		}
	}

	/**
	 * 执行函数<br>
	 * 如果函数为{@code null} 忽略函数执行
	 *
	 * @param consumer 函数
	 * @param p1       参数1
	 * @param p2       参数2
	 * @param p3       参数3
	 * @param <P1>     参数类型1
	 * @param <P2>     参数类型2
	 * @param <P3>     参数类型3
	 */
	public static <P1, P2, P3> void accept(final SerConsumer3<P1, P2, P3> consumer, final P1 p1, final P2 p2, final P3 p3) {
		if (consumer != null) {
			consumer.accept(p1, p2, p3);
		}
	}

	/**
	 * 执行函数<br>
	 * 如果函数为{@code null} 忽略函数执行
	 *
	 * @param consumer 函数
	 * @param t        参数
	 * @param u        参数
	 * @param <T>      值类型
	 * @param <U>      值类型
	 */
	public static <T, U> void accept(final BiConsumer<T, U> consumer, final T t, final U u) {
		if (consumer != null) {
			consumer.accept(t, u);
		}
	}
}
