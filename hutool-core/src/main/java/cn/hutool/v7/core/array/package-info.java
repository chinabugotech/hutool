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

/**
 * 提供数组相关封装，作为{@link java.util.Arrays}的功能补充。<br>
 * 在Java中，数组是基本数据结构， 用来表示一组固定长度的相同类型的元素集合。<br>
 * 由于数组的长度不可变的特性，因此针对数组封装了{@link cn.hutool.v7.core.array.ArrayUtil}，提供一些类似于List有的特性。<br>
 * 在Java语言中，分为原始类型和对象类型的元素，因此对应数组操作也区分为原始类型数组和对象类型数组。
 * <ul>
 *     <li>原始类型数组包括类似{@code int[]}、{@code long[]}、{@code double[]}、{@code float[]}、{@code short[]}、
 *     {@code char[]}、{@code byte[]}、{@code boolean[]}等类型，封装方法在{@link cn.hutool.v7.core.array.PrimitiveArrayUtil}</li>
 *     <li>对象类型数组可以使用泛型表示，类似于`T[]`，封装于{@link cn.hutool.v7.core.array.ArrayUtil}</li>
 * </ul>
 *
 * @author Looly
 */
package cn.hutool.v7.core.array;
