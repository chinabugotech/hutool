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

package cn.hutool.v7.core.text.bloom;

import cn.hutool.v7.core.lang.Assert;

import java.io.Serial;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * 基于Hash函数方法的{@link BloomFilter}
 *
 * @author Looly
 * @since 5.8.0
 */
public class FuncFilter extends AbstractFilter {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 创建FuncFilter
	 *
	 * @param size     最大值
	 * @param hashFuncs Hash函数
	 * @return FuncFilter
	 */
	@SafeVarargs
	public static FuncFilter of(final int size, final Function<String, Number>... hashFuncs) {
		return new FuncFilter(size, hashFuncs);
	}

	// 允许接收多个哈希函数
	private final List<Function<String, Number>> hashFuncs;

	/**
	 * @param size     最大值
	 * @param hashFunc Hash函数
	 */
	@SafeVarargs
	public FuncFilter(final int size, final Function<String, Number>... hashFunc) {
		super(size);
		Assert.notEmpty(hashFunc, "Hash functions must not be empty");
		this.hashFuncs = Collections.unmodifiableList(Arrays.asList(hashFunc));
	}

	@Override
	public int hash(final String str) {
		return hash(str, hashFuncs.get(0));
	}

	public int hash(final String str, final Function<String, Number> hashFunc) {
		return hashFunc.apply(str).intValue() % size;
	}

	@Override
	public boolean contains(final String str) {
		for (final Function<String, Number> hashFunc : hashFuncs) {
			if (!bitSet.get(Math.abs(hash(str, hashFunc)))) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean add(final String str) {
		boolean add = false;
		for (final Function<String, Number> hashFunc : hashFuncs) {
			int hash = Math.abs(hash(str,  hashFunc));
			if (!bitSet.get(hash)) {
				bitSet.set(hash);
				add = true;
			}
		}
		return add;
	}
}
