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

package cn.hutool.v7.core.reflect.factory;

import cn.hutool.v7.core.pool.ObjectFactory;

/**
 * 简易对象工厂，只生产对象，不验证和销毁
 *
 * @param <T> 对象类型
 */
public abstract class SimpleObjectFactory<T> implements ObjectFactory<T> {

	@Override
	public boolean validate(final T t) {
		return true;
	}

	@Override
	public void destroy(final T t) {
		// do nothing
	}
}
