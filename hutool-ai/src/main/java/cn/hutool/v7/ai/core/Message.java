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

package cn.hutool.v7.ai.core;

/**
 * 公共Message类
 *
 * @author elichow
 * @since 6.0.0
 */
public class Message {
	//角色 注意：如果设置系统消息，请放在messages列表的第一位
	private final String role;
	//内容
	private final Object content;

	/**
	 * 构造
	 *
	 * @param role    角色
	 * @param content 内容
	 */
	public Message(final String role, final Object content) {
		this.role = role;
		this.content = content;
	}

	/**
	 * 获取角色
	 *
	 * @return 角色
	 */
	public String getRole() {
		return role;
	}

	/**
	 * 获取内容
	 *
	 * @return 内容
	 */
	public Object getContent() {
		return content;
	}
}
