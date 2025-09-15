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

package cn.hutool.ai.core;

/**
 * 公共Message类
 *
 * @author elichow
 * @since 5.8.38
 */
public class Message {
	//角色 注意：如果设置系统消息，请放在messages列表的第一位
	private String role;
	//内容
	private Object content;

	/**
	 * 构造
	 */
	public Message() {
	}

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
	 * 设置角色
	 *
	 * @param role 角色
	 */
	public void setRole(final String role) {
		this.role = role;
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

	/**
	 * 设置内容
	 *
	 * @param content 内容
	 */
	public void setContent(final Object content) {
		this.content = content;
	}
}
