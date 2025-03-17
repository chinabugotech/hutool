package cn.hutool.ai;

/**
 * 模型厂商的名称（不指具体的模型）
 *
 * @author elichow
 * @since 5.8.37
 */
public enum ModelName {
	DEEPSEEK("deepSeek"),
	OPENAI("openai"),
	DOUBAO("doubao"),
	GROK("grok");

	private final String value;

	ModelName(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
