/*
 * Copyright (c) 2013-2025 Hutool Team and hutool.cn
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

package cn.hutool.v7.core.bean;

import cn.hutool.v7.core.bean.copier.ValueProvider;
import cn.hutool.v7.core.reflect.ConstructorUtil;

import java.lang.reflect.RecordComponent;
import java.lang.reflect.Type;
import java.util.AbstractMap;
import java.util.Map;

/**
 * java.lang.Record 相关工具类封装
 *
 * @author Looly
 * @since 7.0.0
 */
public class RecordUtil {

	/**
	 * 判断给定类是否为Record类
	 *
	 * @param clazz 类
	 * @return 是否为Record类
	 */
	public static boolean isRecord(final Class<?> clazz) {
		return null != clazz && clazz.isRecord();
	}

	/**
	 * 获取Record类中所有字段名称，getter方法名与字段同名
	 *
	 * @param recordClass Record类
	 * @return 字段数组
	 */
	@SuppressWarnings("unchecked")
	public static Map.Entry<String, Type>[] getRecordComponents(final Class<?> recordClass) {
		final RecordComponent[] components = recordClass.getRecordComponents();
		final Map.Entry<String, Type>[] entries = new Map.Entry[components.length];
		for (int i = 0; i < components.length; i++) {
			entries[i] = new AbstractMap.SimpleEntry<>(components[i].getName(), components[i].getGenericType());
		}
		return entries;
	}

	/**
	 * 实例化Record类
	 *
	 * @param recordClass   类
	 * @param valueProvider 参数值提供器
	 * @return Record类
	 */
	public static Object newInstance(final Class<?> recordClass, final ValueProvider<String> valueProvider) {
		final Map.Entry<String, Type>[] recordComponents = getRecordComponents(recordClass);
		final Object[] args = new Object[recordComponents.length];
		for (int i = 0; i < args.length; i++) {
			args[i] = valueProvider.value(recordComponents[i].getKey(), recordComponents[i].getValue());
		}

		return ConstructorUtil.newInstance(recordClass, args);
	}
}
