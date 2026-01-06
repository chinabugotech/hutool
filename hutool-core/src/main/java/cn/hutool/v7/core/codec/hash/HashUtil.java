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

package cn.hutool.v7.core.codec.hash;

import cn.hutool.v7.core.codec.Number128;
import cn.hutool.v7.core.codec.hash.metro.MetroHash128;
import cn.hutool.v7.core.codec.hash.metro.MetroHash64;

/**
 * Hash算法大全<br>
 * 推荐使用FNV1算法
 *
 * @author Goodzzp, Looly
 */
public class HashUtil {

	/**
	 * 加法hash
	 *
	 * @param key   字符串
	 * @param prime 一个质数
	 * @return hash结果
	 */
	public static int additiveHash(final String key, final int prime) {
		int hash, i;
		for (hash = key.length(), i = 0; i < key.length(); i++) {
			hash += key.charAt(i);
		}
		return hash % prime;
	}

	/**
	 * 旋转hash
	 *
	 * @param key   输入字符串
	 * @param prime 质数
	 * @return hash值
	 */
	public static int rotatingHash(final String key, final int prime) {
		int hash, i;
		for (hash = key.length(), i = 0; i < key.length(); ++i) {
			hash = (hash << 4) ^ (hash >> 28) ^ key.charAt(i);
		}

		// 使用：hash = (hash ^ (hash>>10) ^ (hash>>20)) & mask;
		// 替代：hash %= prime;
		// return (hash ^ (hash>>10) ^ (hash>>20));
		return hash % prime;
	}

	/**
	 * 一次一个hash
	 *
	 * @param key 输入字符串
	 * @return 输出hash值
	 */
	public static int oneByOneHash(final String key) {
		int hash, i;
		for (hash = 0, i = 0; i < key.length(); ++i) {
			hash += key.charAt(i);
			hash += (hash << 10);
			hash ^= (hash >> 6);
		}
		hash += (hash << 3);
		hash ^= (hash >> 11);
		hash += (hash << 15);
		// return (hash & M_MASK);
		return hash;
	}

	/**
	 * Bernstein's hash
	 *
	 * @param key 输入字节数组
	 * @return 结果hash
	 */
	public static int bernstein(final String key) {
		int hash = 0;
		int i;
		for (i = 0; i < key.length(); ++i) {
			hash = 33 * hash + key.charAt(i);
		}
		return hash;
	}

	/**
	 * Universal Hashing
	 *
	 * @param key  字节数组
	 * @param mask 掩码
	 * @param tab  tab
	 * @return hash值
	 */
	public static int universal(final char[] key, final int mask, final int[] tab) {
		int hash = key.length;
		int i;
		final int len = key.length;
		for (i = 0; i < (len << 3); i += 8) {
			final char k = key[i >> 3];
			if ((k & 0x01) == 0) {
				hash ^= tab[i];
			}
			if ((k & 0x02) == 0) {
				hash ^= tab[i + 1];
			}
			if ((k & 0x04) == 0) {
				hash ^= tab[i + 2];
			}
			if ((k & 0x08) == 0) {
				hash ^= tab[i + 3];
			}
			if ((k & 0x10) == 0) {
				hash ^= tab[i + 4];
			}
			if ((k & 0x20) == 0) {
				hash ^= tab[i + 5];
			}
			if ((k & 0x40) == 0) {
				hash ^= tab[i + 6];
			}
			if ((k & 0x80) == 0) {
				hash ^= tab[i + 7];
			}
		}
		return (hash & mask);
	}

	/**
	 * Zobrist Hashing
	 *
	 * @param key  字节数组
	 * @param mask 掩码
	 * @param tab  tab
	 * @return hash值
	 */
	public static int zobrist(final char[] key, final int mask, final int[][] tab) {
		int hash, i;
		for (hash = key.length, i = 0; i < key.length; ++i) {
			hash ^= tab[i][key[i]];
		}
		return (hash & mask);
	}

	/**
	 * 改进的32位FNV算法1
	 *
	 * @param data 数组
	 * @return hash结果
	 */
	public static int fnvHash(final byte[] data) {
		final int p = 16777619;
		int hash = (int) 2166136261L;
		for (final byte b : data) {
			hash = (hash ^ b) * p;
		}
		hash += hash << 13;
		hash ^= hash >> 7;
		hash += hash << 3;
		hash ^= hash >> 17;
		hash += hash << 5;
		return Math.abs(hash);
	}

	/**
	 * 改进的32位FNV算法1
	 *
	 * @param data 字符串
	 * @return hash结果
	 */
	public static int fnvHash(final String data) {
		final int p = 16777619;
		int hash = (int) 2166136261L;
		for (int i = 0; i < data.length(); i++) {
			hash = (hash ^ data.charAt(i)) * p;
		}
		hash += hash << 13;
		hash ^= hash >> 7;
		hash += hash << 3;
		hash ^= hash >> 17;
		hash += hash << 5;
		return Math.abs(hash);
	}

	/**
	 * Thomas Wang的算法，整数hash
	 *
	 * @param key 整数
	 * @return hash值
	 */
	public static int intHash(int key) {
		key += ~(key << 15);
		key ^= (key >>> 10);
		key += (key << 3);
		key ^= (key >>> 6);
		key += ~(key << 11);
		key ^= (key >>> 16);
		return key;
	}

	/**
	 * RS算法hash
	 *
	 * @param str 字符串
	 * @return hash值
	 */
	public static int rsHash(final String str) {
		final int b = 378551;
		int a = 63689;
		int hash = 0;

		for (int i = 0; i < str.length(); i++) {
			hash = hash * a + str.charAt(i);
			a = a * b;
		}

		return hash & 0x7FFFFFFF;
	}

	/**
	 * JS算法
	 *
	 * @param str 字符串
	 * @return hash值
	 */
	public static int jsHash(final String str) {
		int hash = 1315423911;

		for (int i = 0; i < str.length(); i++) {
			hash ^= ((hash << 5) + str.charAt(i) + (hash >> 2));
		}

		return Math.abs(hash) & 0x7FFFFFFF;
	}

	/**
	 * PJW算法
	 *
	 * @param str 字符串
	 * @return hash值
	 */
	public static int pjwHash(final String str) {
		final int bitsInUnsignedInt = 32;
		final int threeQuarters = (bitsInUnsignedInt * 3) / 4;
		final int oneEighth = bitsInUnsignedInt / 8;
		final int highBits = 0xFFFFFFFF << (bitsInUnsignedInt - oneEighth);
		int hash = 0;
		int test;

		for (int i = 0; i < str.length(); i++) {
			hash = (hash << oneEighth) + str.charAt(i);

			if ((test = hash & highBits) != 0) {
				hash = ((hash ^ (test >> threeQuarters)) & (~highBits));
			}
		}

		return hash & 0x7FFFFFFF;
	}

	/**
	 * ELF算法
	 *
	 * @param str 字符串
	 * @return hash值
	 */
	public static int elfHash(final String str) {
		int hash = 0;
		int x;

		for (int i = 0; i < str.length(); i++) {
			hash = (hash << 4) + str.charAt(i);
			if ((x = (int) (hash & 0xF0000000L)) != 0) {
				hash ^= (x >> 24);
				hash &= ~x;
			}
		}

		return hash & 0x7FFFFFFF;
	}

	/**
	 * BKDR算法
	 *
	 * @param str 字符串
	 * @return hash值
	 */
	public static int bkdrHash(final String str) {
		final int seed = 131; // 31 131 1313 13131 131313 etc..
		int hash = 0;

		for (int i = 0; i < str.length(); i++) {
			hash = (hash * seed) + str.charAt(i);
		}

		return hash & 0x7FFFFFFF;
	}

	/**
	 * SDBM算法
	 *
	 * @param str 字符串
	 * @return hash值
	 */
	public static int sdbmHash(final String str) {
		int hash = 0;

		for (int i = 0; i < str.length(); i++) {
			hash = str.charAt(i) + (hash << 6) + (hash << 16) - hash;
		}

		return hash & 0x7FFFFFFF;
	}

	/**
	 * DJB算法
	 *
	 * @param str 字符串
	 * @return hash值
	 */
	public static int djbHash(final String str) {
		int hash = 5381;

		for (int i = 0; i < str.length(); i++) {
			hash = ((hash << 5) + hash) + str.charAt(i);
		}

		return hash & 0x7FFFFFFF;
	}

	/**
	 * DEK算法
	 *
	 * @param str 字符串
	 * @return hash值
	 */
	public static int dekHash(final String str) {
		int hash = str.length();

		for (int i = 0; i < str.length(); i++) {
			hash = ((hash << 5) ^ (hash >> 27)) ^ str.charAt(i);
		}

		return hash & 0x7FFFFFFF;
	}

	/**
	 * AP算法
	 *
	 * @param str 字符串
	 * @return hash值
	 */
	public static int apHash(final String str) {
		int hash = 0;

		for (int i = 0; i < str.length(); i++) {
			hash ^= ((i & 1) == 0) ? ((hash << 7) ^ str.charAt(i) ^ (hash >> 3)) : (~((hash << 11) ^ str.charAt(i) ^ (hash >> 5)));
		}

		// return (hash & 0x7FFFFFFF);
		return hash;
	}

	/**
	 * TianL Hash算法
	 *
	 * @param str 字符串
	 * @return Hash值
	 */
	public static long tianlHash(final String str) {
		long hash;

		final int iLength = str.length();
		if (iLength == 0) {
			return 0;
		}

		if (iLength <= 256) {
			hash = 16777216L * (iLength - 1);
		} else {
			hash = 4278190080L;
		}

		int i;

		char ucChar;

		if (iLength <= 96) {
			for (i = 1; i <= iLength; i++) {
				ucChar = str.charAt(i - 1);
				if (ucChar <= 'Z' && ucChar >= 'A') {
					ucChar = (char) (ucChar + 32);
				}
				hash += (3L * i * ucChar * ucChar + 5L * i * ucChar + 7L * i + 11 * ucChar) % 16777216;
			}
		} else {
			for (i = 1; i <= 96; i++) {
				ucChar = str.charAt(i + iLength - 96 - 1);
				if (ucChar <= 'Z' && ucChar >= 'A') {
					ucChar = (char) (ucChar + 32);
				}
				hash += (3L * i * ucChar * ucChar + 5L * i * ucChar + 7L * i + 11 * ucChar) % 16777216;
			}
		}
		if (hash < 0) {
			hash *= -1;
		}
		return hash;
	}

	/**
	 * JAVA自己带的算法
	 *
	 * @param str 字符串
	 * @return hash值
	 */
	public static int javaDefaultHash(final String str) {
		int h = 0;
		int off = 0;
		final int len = str.length();
		for (int i = 0; i < len; i++) {
			h = 31 * h + str.charAt(off++);
		}
		return h;
	}

	/**
	 * 混合hash算法，输出64位的值
	 *
	 * @param str 字符串
	 * @return hash值
	 */
	public static long mixHash(final String str) {
		long hash = str.hashCode();
		hash <<= 32;
		hash |= fnvHash(str);
		return hash;
	}

	/**
	 * 根据对象的内存地址生成相应的Hash值
	 *
	 * @param obj 对象
	 * @return hash值
	 * @since 4.2.2
	 */
	public static int identityHashCode(final Object obj) {
		return System.identityHashCode(obj);
	}

	/**
	 * MurmurHash算法32-bit实现
	 *
	 * @param data 数据
	 * @return hash值
	 * @since 4.3.3
	 */
	public static int murmur32(final byte[] data) {
		return MurmurHash.INSTANCE.hash32(data);
	}

	/**
	 * MurmurHash算法64-bit实现
	 *
	 * @param data 数据
	 * @return hash值
	 * @since 4.3.3
	 */
	public static long murmur64(final byte[] data) {
		return MurmurHash.INSTANCE.hash64(data);
	}

	/**
	 * MurmurHash算法128-bit实现
	 *
	 * @param data 数据
	 * @return hash值
	 * @since 4.3.3
	 */
	public static Number128 murmur128(final byte[] data) {
		return MurmurHash.INSTANCE.hash128(data);
	}

	/**
	 * CityHash算法32-bit实现
	 *
	 * @param data 数据
	 * @return hash值
	 * @since 5.2.5
	 */
	public static int cityHash32(final byte[] data) {
		return CityHash.INSTANCE.hash32(data);
	}

	/**
	 * CityHash算法64-bit实现，种子1使用默认的CityHash#k2
	 *
	 * @param data 数据
	 * @param seed 种子2
	 * @return hash值
	 * @since 5.2.5
	 */
	public static long cityHash64(final byte[] data, final long seed) {
		return CityHash.INSTANCE.hash64(data, seed);
	}

	/**
	 * CityHash算法64-bit实现，种子1使用默认的CityHash#k2
	 *
	 * @param data  数据
	 * @param seed0 种子1
	 * @param seed1 种子2
	 * @return hash值
	 * @since 5.2.5
	 */
	public static long cityHash64(final byte[] data, final long seed0, final long seed1) {
		return CityHash.INSTANCE.hash64(data, seed0, seed1);
	}

	/**
	 * CityHash算法64-bit实现
	 *
	 * @param data 数据
	 * @return hash值
	 * @since 5.2.5
	 */
	public static long cityHash64(final byte[] data) {
		return CityHash.INSTANCE.hash64(data);
	}

	/**
	 * CityHash算法128-bit实现
	 *
	 * @param data 数据
	 * @return hash值
	 */
	public static Number128 cityHash128(final byte[] data) {
		return CityHash.INSTANCE.hash128(data);
	}

	/**
	 * CityHash算法128-bit实现
	 *
	 * @param data 数据
	 * @param seed 种子
	 * @return hash值
	 */
	public static Number128 cityHash128(final byte[] data, final Number128 seed) {
		return CityHash.INSTANCE.hash128(data, seed);
	}

	/**
	 * MetroHash 算法64-bit实现
	 *
	 * @param data 数据
	 * @param seed 种子
	 * @return hash值
	 */
	public static long metroHash64(final byte[] data, final long seed) {
		return MetroHash64.of(seed).hash64(data);
	}

	/**
	 * MetroHash 算法128-bit实现
	 *
	 * @param data 数据
	 * @param seed 种子
	 * @return hash值
	 */
	public static Number128 metroHash128(final byte[] data, final long seed) {
		return MetroHash128.of(seed).hash128(data);
	}

	/**
	 * HF Hash算法
	 *
	 * @param data 字符串
	 * @return hash结果
	 * @since 5.8.0
	 */
	public static long hfHash(final String data) {
		final int length = data.length();
		long hash = 0;

		for (int i = 0; i < length; i++) {
			hash += (long) data.charAt(i) * 3 * i;
		}

		if (hash < 0) {
			hash = -hash;
		}

		return hash;
	}

	/**
	 * HFIP Hash算法
	 *
	 * @param data 字符串
	 * @return hash结果
	 * @since 5.8.0
	 */
	public static long hfIpHash(final String data) {
		final int length = data.length();
		long hash = 0;
		for (int i = 0; i < length; i++) {
			hash += data.charAt(i % 4) ^ data.charAt(i);
		}
		return hash;
	}
}
