/*
 * Copyright (c) 2025 Hutool Team and hutool.cn
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

package org.dromara.hutool.crypto.digest;

import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;

/**
 * Argon2加密实现
 *
 * @author changhr2013
 * @author Looly
 * @since 5.8.38
 */
public class Argon2 {

	/**
	 * 默认hash长度
	 */
	public static final int DEFAULT_HASH_LENGTH = 32;

	private int hashLength = DEFAULT_HASH_LENGTH;
	private final Argon2Parameters.Builder paramsBuilder;

	/**
	 * 构造，默认使用{@link Argon2Parameters#ARGON2_id}类型
	 */
	public Argon2(){
		this(Argon2Parameters.ARGON2_id);
	}

	/**
	 * 构造
	 *
	 * @param type {@link Argon2Parameters#ARGON2_d}、{@link Argon2Parameters#ARGON2_i}、{@link Argon2Parameters#ARGON2_id}
	 */
	public Argon2(final int type){
		this(new Argon2Parameters.Builder(type));
	}

	/**
	 * 构造
	 *
	 * @param paramsBuilder 参数构造器
	 */
	public Argon2(final Argon2Parameters.Builder paramsBuilder){
		this.paramsBuilder = paramsBuilder;
	}

	/**
	 * 设置hash长度
	 *
	 * @param hashLength hash长度
	 * @return this
	 */
	public Argon2 setHashLength(final int hashLength){
		this.hashLength = hashLength;
		return this;
	}

	/**
	 * 设置版本
	 *
	 * @param version 版本
	 * @return this
	 * @see Argon2Parameters#ARGON2_VERSION_10
	 * @see Argon2Parameters#ARGON2_VERSION_13
	 */
	public Argon2 setVersion(final int version){
		this.paramsBuilder.withVersion(version);
		return this;
	}

	/**
	 * 设置盐
	 *
	 * @param salt 盐
	 * @return this
	 */
	public Argon2 setSalt(final byte[] salt){
		this.paramsBuilder.withSalt(salt);
		return this;
	}

	/**
	 * 设置可选的密钥数据，用于增加哈希的复杂性
	 *
	 * @param secret 密钥
	 * @return this
	 */
	public Argon2 setSecret(final byte[] secret){
		this.paramsBuilder.withSecret(secret);
		return this;
	}

	/**
	 * @param additional 附加数据
	 * @return this
	 */
	public Argon2 setAdditional(final byte[] additional){
		this.paramsBuilder.withAdditional(additional);
		return this;
	}

	/**
	 * 设置迭代次数<br>
	 * 迭代次数越多，生成哈希的时间就越长，破解哈希就越困难
	 *
	 * @param iterations 迭代次数
	 * @return this
	 */
	public Argon2 setIterations(final int iterations){
		this.paramsBuilder.withIterations(iterations);
		return this;
	}

	/**
	 * 设置内存，单位KB<br>
	 * 内存越大，生成哈希的时间就越长，破解哈希就越困难
	 *
	 * @param memoryAsKB 内存，单位KB
	 * @return this
	 */
	public Argon2 setMemoryAsKB(final int memoryAsKB){
		this.paramsBuilder.withMemoryAsKB(memoryAsKB);
		return this;
	}

	/**
	 * 设置并行度，即同时使用的核心数<br>
	 * 值越高，生成哈希的时间就越长，破解哈希就越困难
	 *
	 * @param parallelism 并行度
	 * @return this
	 */
	public Argon2 setParallelism(final int parallelism){
		this.paramsBuilder.withParallelism(parallelism);
		return this;
	}

	/**
	 * 生成hash值
	 *
	 * @param password 密码
	 * @return hash值
	 */
	public byte[] digest(final char[] password){
		final Argon2BytesGenerator generator = new Argon2BytesGenerator();
		generator.init(paramsBuilder.build());
		final byte[] result = new byte[hashLength];
		generator.generateBytes(password, result);
		return result;
	}
}
