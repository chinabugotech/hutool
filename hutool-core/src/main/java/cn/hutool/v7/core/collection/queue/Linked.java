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

package cn.hutool.v7.core.collection.queue;

/**
 * Linked接口定义了一个链式数据结构的通用接口<br>
 * 链式结构中每个元素都包含两个指针，分别指向前一个元素和后一个元素。
 *
 * @param <T> 泛型参数，表示链式结构中元素的类型，必须继承自{@code Linked<T>}接口
 * @author Looly
 */
public interface Linked<T extends Linked<T>> {

	/**
	 * 获取前一个节点，若返回{@code null} 表示这个节点没有链接或者为头节点
	 *
	 * @return 前一个值
	 */
	T getPrevious();

	/**
	 * 设置前一个节点，若为{@code null} 表示这个节点没有链接或者为头节点
	 *
	 * @param prev 前一个值
	 */
	void setPrevious(T prev);

	/**
	 * 获取下一个节点，若返回{@code null} 表示这个节点没有链接或者为尾节点
	 *
	 * @return 下一个节点
	 */
	T getNext();

	/**
	 * 设置下一个节点，若为{@code null} 表示这个节点没有链接或者为尾节点。
	 *
	 * @param next 下一个节点
	 */
	void setNext(T next);
}
