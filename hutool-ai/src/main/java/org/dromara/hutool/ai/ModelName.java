package org.dromara.hutool.ai;

/**
 * 模型厂商的名称（不指具体的模型）
 *
 * @author elichow
 * @since 6.0.0
 */
public enum ModelName {
	/**
	 * deepSeek
	 */
	DEEPSEEK("deepSeek"),
	/**
	 * openai
	 */
	OPENAI("openai"),
	/**
	 * doubao
	 */
	DOUBAO("doubao"),
	/**
	 * grok
	 */
	GROK("grok");

	private final String value;

	ModelName(final String value) {
		this.value = value;
	}

	/**
	 * 获取值
	 *
	 * @return 值
	 */
	public String getValue() {
		return value;
	}
}
