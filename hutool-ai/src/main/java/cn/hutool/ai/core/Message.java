package cn.hutool.ai.core;

/**
 * 公共Message类
 *
 * @author elichow
 * @since 5.8.37
 */
public class Message {
	//角色 注意：如果设置系统消息，请放在messages列表的第一位
	private final String role;
	//内容
	private final Object content;

	public Message(String role, Object content) {
		this.role = role;
		this.content = content;
	}

	public String getRole() {
		return role;
	}

	public Object getContent() {
		return content;
	}
}
