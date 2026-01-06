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

package cn.hutool.v7.core.reflect;

import java.lang.reflect.Type;

/**
 * 空类型表示
 *
 * @author Looly
 * @since 6.0.0
 */
public class NullType implements Type {
	/**
	 * 单例对象
	 */
	public static NullType INSTANCE = new NullType();

	private NullType(){}

	@Override
	public String toString() {
		return "Type of null";
	}
}
