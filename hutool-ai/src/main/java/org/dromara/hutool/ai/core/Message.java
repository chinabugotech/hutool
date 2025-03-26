package org.dromara.hutool.ai.core;

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
