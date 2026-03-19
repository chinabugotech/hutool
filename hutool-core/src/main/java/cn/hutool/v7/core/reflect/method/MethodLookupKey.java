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

package cn.hutool.v7.core.reflect.method;

import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * 方法查找键（用于细粒度缓存）
 * 用于唯一标识一次方法查找操作
 *
 * @author heco07
 * @since 5.8.44
 */
class MethodLookupKey implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	private final Class<?> clazz;
	private final boolean ignoreCase;
	private final String methodName;
	private final Class<?>[] paramTypes;

	/**
	 * 构造
	 *
	 * @param clazz       类
	 * @param ignoreCase  是否忽略大小写
	 * @param methodName  方法名
	 * @param paramTypes  参数类型
	 */
	public MethodLookupKey(final Class<?> clazz, final boolean ignoreCase, final String methodName, final Class<?>[] paramTypes) {
		this.clazz = clazz;
		this.ignoreCase = ignoreCase;
		this.methodName = methodName;
		this.paramTypes = paramTypes;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final MethodLookupKey that = (MethodLookupKey) o;
		return ignoreCase == that.ignoreCase &&
			Objects.equals(clazz, that.clazz) &&
			Objects.equals(methodName, that.methodName) &&
			Arrays.equals(paramTypes, that.paramTypes);
	}

	@Override
	public int hashCode() {
		int result = Objects.hash(clazz, ignoreCase, methodName);
		result = 31 * result + Arrays.hashCode(paramTypes);
		return result;
	}
}
