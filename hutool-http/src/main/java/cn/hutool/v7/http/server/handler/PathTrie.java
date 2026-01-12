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

package cn.hutool.v7.http.server.handler;

import cn.hutool.v7.core.text.StrUtil;
import cn.hutool.v7.core.text.split.SplitUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 路由处理器<br>
 * 根据请求的路径精确匹配路由，并调用对应的处理器
 *
 * @author Looly
 * @since 7.0.0
 */
public class PathTrie {

	private final Node root;

	/**
	 * 构造
	 */
	public PathTrie() {
		root = new Node();
	}

	/**
	 * 添加路由
	 *
	 * @param path 路径
	 * @param handler    处理器
	 */
	public void add(final String path, final HttpHandler handler) {
		Node node = root;
		final List<String> parts = SplitUtil.splitTrim(path, StrUtil.SLASH);
		for (final String part : parts) {
			node = node.getOrCreateChildren(part);
		}
		node.isEndOfPath = true;
		node.handler = handler;
	}

	/**
	 * 查找匹配的处理器，采用最长匹配模式，即：<br>
	 * 传入"a/b/c"，存在"a/b/c"，则直接匹配，否则匹配"a/b"，否则匹配"a"
	 *
	 * @param path 路径
	 * @return 处理器
	 */
	public HttpHandler match(final String path) {
		Node matchedNode = null;
		Node node = root;
		final List<String> parts = SplitUtil.splitTrim(path, StrUtil.SLASH);
		for (final String part : parts) {
			node = node.getChildren(part);
			if (node == null) {
				break;
			}
			if(node.isEndOfPath){
				matchedNode = node;
			}
		}
		return null == matchedNode ? null : matchedNode.handler;
	}

	static class Node {
		Map<String, Node> children;
		boolean isEndOfPath;
		HttpHandler handler;

		public Node() {
			isEndOfPath = false;
			handler = null;
		}

		/**
		 * 获取子节点
		 *
		 * @param part 节点标识
		 * @return 子节点
		 */
		public Node getChildren(final String part) {
			return null == children ? null : children.get(part);
		}

		/**
		 * 获取或创建子节点
		 *
		 * @param part 节点标识
		 * @return 子节点
		 */
		public Node getOrCreateChildren(final String part) {
			if(null == children){
				children = new HashMap<>();
			}
			return children.computeIfAbsent(part, c -> new Node());
		}
	}
}

