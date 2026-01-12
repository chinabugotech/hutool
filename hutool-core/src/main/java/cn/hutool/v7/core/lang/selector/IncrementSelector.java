/*
 * Copyright (c) 2013-2026 Hutool Team.
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

package cn.hutool.v7.core.lang.selector;

import java.io.Serial;
import java.util.ArrayList;

/**
 * 简单的轮询选择器
 *
 * @param <T> 元素类型
 * @author Looly
 * @since 7.0.0
 */
public class IncrementSelector<T> extends ArrayList<T> implements Selector<T> {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 值索引
	 */
	private int position;

	// region ----- Constructors

	/**
	 * 构造
	 */
	public IncrementSelector() {
		super();
	}

	/**
	 * 构造
	 *
	 * @param objList 对象列表
	 */
	public IncrementSelector(final Iterable<T> objList) {
		this();
		for (final T obj : objList) {
			add(obj);
		}
	}
	// endregion

	@Override
	public T select() {
		final T result = get(position);
		if(++position >= size()){
			position = 0;
		}
		return result;
	}
}
