package org.dromara.hutool.core.data.masking;

import java.util.HashSet;
import java.util.Set;

/**
 * 富文本脱敏规则，用于配置如何对富文本内容进行脱敏处理
 *
 * @author xjf
 */
public class RichTextMaskingRule {

	/**
	 * 脱敏类型枚举
	 */
	public enum MaskType {
		/**
		 * 完全脱敏，将匹配的内容完全替换为指定字符
		 */
		FULL,

		/**
		 * 部分脱敏，保留部分原始内容
		 */
		PARTIAL,

		/**
		 * 替换脱敏，将匹配的内容替换为指定的替换文本
		 */
		REPLACE
	}

	/**
	 * 规则名称
	 */
	private String name;

	/**
	 * 匹配模式（正则表达式）
	 */
	private String pattern;

	/**
	 * 脱敏类型
	 */
	private MaskType maskType;

	/**
	 * 替换内容
	 */
	private String replacement;

	/**
	 * 保留左侧字符数（用于PARTIAL类型）
	 */
	private int preserveLeft;

	/**
	 * 保留右侧字符数（用于PARTIAL类型）
	 */
	private int preserveRight;

	/**
	 * 脱敏字符
	 */
	private char maskChar = '*';

	/**
	 * 是否处理HTML标签内容
	 */
	private boolean processHtmlTags = false;

	/**
	 * 需要排除的HTML标签
	 */
	private Set<String> excludeTags = new HashSet<>();

	/**
	 * 仅处理指定的HTML标签
	 */
	private Set<String> includeTags = new HashSet<>();

	/**
	 * 构造函数
	 */
	public RichTextMaskingRule() {
	}

	/**
	 * 构造函数
	 *
	 * @param name        规则名称
	 * @param pattern     匹配模式（正则表达式）
	 * @param maskType    脱敏类型
	 * @param replacement 替换内容
	 */
	public RichTextMaskingRule(final String name, final String pattern, final MaskType maskType, final String replacement) {
		this.name = name;
		this.pattern = pattern;
		this.maskType = maskType;
		this.replacement = replacement;
	}

	/**
	 * 构造函数，用于部分脱敏
	 *
	 * @param name          规则名称
	 * @param pattern       匹配模式（正则表达式）
	 * @param preserveLeft  保留左侧字符数
	 * @param preserveRight 保留右侧字符数
	 * @param maskChar      脱敏字符
	 */
	public RichTextMaskingRule(final String name, final String pattern, final int preserveLeft, final int preserveRight, final char maskChar) {
		this.name = name;
		this.pattern = pattern;
		this.maskType = MaskType.PARTIAL;
		this.preserveLeft = preserveLeft;
		this.preserveRight = preserveRight;
		this.maskChar = maskChar;
	}

	// Getter and Setter methods

	/**
	 * 获取规则名称
	 *
	 * @return 规则名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置规则名称
	 *
	 * @param name 名称
	 * @return this
	 */
	public RichTextMaskingRule setName(final String name) {
		this.name = name;
		return this;
	}

	/**
	 * 获取匹配模式（正则表达式）
	 * @return 匹配模式（正则表达式）
	 */
	public String getPattern() {
		return pattern;
	}

	/**
	 * 设置匹配模式（正则表达式）
	 * @param pattern 匹配模式（正则表达式）
	 * @return this
	 */
	public RichTextMaskingRule setPattern(final String pattern) {
		this.pattern = pattern;
		return this;
	}


	/**
	 * 获取脱敏类型
	 *
	 * @return 脱敏类型
	 */
	public MaskType getMaskType() {
		return maskType;
	}

	/**
	 * 设置脱敏类型
	 *
	 * @param maskType 脱敏类型
	 * @return this
	 */
	public RichTextMaskingRule setMaskType(final MaskType maskType) {
		this.maskType = maskType;
		return this;
	}

	/**
	 * 获取替换内容
	 *
	 * @return 替换内容
	 */
	public String getReplacement() {
		return replacement;
	}

	/**
	 * 设置替换内容
	 *
	 * @param replacement 替换内容
	 * @return this
	 */
	public RichTextMaskingRule setReplacement(final String replacement) {
		this.replacement = replacement;
		return this;
	}

	/**
	 * 获取保留左侧字符数
	 *
	 * @return 保留左侧字符数
	 */
	public int getPreserveLeft() {
		return preserveLeft;
	}

	/**
	 * 设置保留左侧字符数
	 *
	 * @param preserveLeft 保留左侧字符数
	 * @return this
	 */
	public RichTextMaskingRule setPreserveLeft(final int preserveLeft) {
		this.preserveLeft = preserveLeft;
		return this;
	}

	/**
	 * 获取保留右侧字符数
	 *
	 * @return 保留右侧字符数
	 */
	public int getPreserveRight() {
		return preserveRight;
	}

	/**
	 * 设置保留右侧字符数
	 *
	 * @param preserveRight 保留右侧字符数
	 * @return this
	 */
	public RichTextMaskingRule setPreserveRight(final int preserveRight) {
		this.preserveRight = preserveRight;
		return this;
	}

	/**
	 * 获取脱敏字符
	 *
	 * @return 脱敏字符
	 */
	public char getMaskChar() {
		return maskChar;
	}

	/**
	 * 设置脱敏字符
	 *
	 * @param maskChar 脱敏字符
	 * @return this
	 */
	public RichTextMaskingRule setMaskChar(final char maskChar) {
		this.maskChar = maskChar;
		return this;
	}

	/**
	 * 获取是否处理HTML标签内容
	 *
	 * @return 是否处理HTML标签内容
	 */
	public boolean isProcessHtmlTags() {
		return processHtmlTags;
	}

	/**
	 * 设置是否处理HTML标签内容
	 *
	 * @param processHtmlTags 是否处理HTML标签内容
	 * @return this
	 */
	public RichTextMaskingRule setProcessHtmlTags(final boolean processHtmlTags) {
		this.processHtmlTags = processHtmlTags;
		return this;
	}

	/**
	 * 获取需要排除的HTML标签
	 *
	 * @return 需要排除的HTML标签
	 */
	public Set<String> getExcludeTags() {
		return excludeTags;
	}

	/**
	 * 设置需要排除的HTML标签
	 *
	 * @param excludeTags 需要排除的HTML标签
	 * @return this
	 */
	public RichTextMaskingRule setExcludeTags(final Set<String> excludeTags) {
		this.excludeTags = excludeTags;
		return this;
	}

	/**
	 * 添加需要排除的HTML标签
	 *
	 * @param tag 需要排除的HTML标签
	 * @return this
	 */
	public RichTextMaskingRule addExcludeTag(final String tag) {
		this.excludeTags.add(tag.toLowerCase());
		return this;
	}

	/**
	 * 获取仅处理指定的HTML标签
	 *
	 * @return 仅处理指定的HTML标签
	 */
	public Set<String> getIncludeTags() {
		return includeTags;
	}

	/**
	 * 设置仅处理指定的HTML标签
	 *
	 * @param includeTags 仅处理指定的HTML标签
	 * @return this
	 */
	public RichTextMaskingRule setIncludeTags(final Set<String> includeTags) {
		this.includeTags = includeTags;
		return this;
	}

	/**
	 * 添加仅处理指定的HTML标签
	 *
	 * @param tag 仅处理指定的HTML标签
	 * @return this
	 */
	public RichTextMaskingRule addIncludeTag(final String tag) {
		this.includeTags.add(tag.toLowerCase());
		return this;
	}
}
