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

package cn.hutool.v7.core.bean.path.node;

import cn.hutool.v7.core.text.CharUtil;
import cn.hutool.v7.core.text.StrUtil;

/**
 * 节点简单工厂
 *
 * @author Looly
 * @since 7.0.0
 */
public class NodeFactory {

	/**
	 * 根据表达式创建对应的节点
	 *
	 * @param expression 表达式
	 * @return 节点
	 */
	public static Node createNode(final String expression) {
		if (StrUtil.isEmpty(expression)) {
			return EmptyNode.INSTANCE;
		}

		if (StrUtil.contains(expression, CharUtil.COLON)) {
			return new RangeNode(expression);
		}

		if (StrUtil.contains(expression, CharUtil.COMMA)) {
			return new ListNode(expression);
		}

		return new NameNode(expression);
	}
}
