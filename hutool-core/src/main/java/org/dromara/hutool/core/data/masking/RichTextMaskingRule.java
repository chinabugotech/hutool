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
     * @param name       规则名称
     * @param pattern    匹配模式（正则表达式）
     * @param maskType   脱敏类型
     * @param replacement 替换内容
     */
    public RichTextMaskingRule(String name, String pattern, MaskType maskType, String replacement) {
        this.name = name;
        this.pattern = pattern;
        this.maskType = maskType;
        this.replacement = replacement;
    }

    /**
     * 构造函数，用于部分脱敏
     *
     * @param name         规则名称
     * @param pattern      匹配模式（正则表达式）
     * @param preserveLeft 保留左侧字符数
     * @param preserveRight 保留右侧字符数
     * @param maskChar     脱敏字符
     */
    public RichTextMaskingRule(String name, String pattern, int preserveLeft, int preserveRight, char maskChar) {
        this.name = name;
        this.pattern = pattern;
        this.maskType = MaskType.PARTIAL;
        this.preserveLeft = preserveLeft;
        this.preserveRight = preserveRight;
        this.maskChar = maskChar;
    }

    // Getter and Setter methods

    public String getName() {
        return name;
    }

    public RichTextMaskingRule setName(String name) {
        this.name = name;
        return this;
    }

    public String getPattern() {
        return pattern;
    }

    public RichTextMaskingRule setPattern(String pattern) {
        this.pattern = pattern;
        return this;
    }

    public MaskType getMaskType() {
        return maskType;
    }

    public RichTextMaskingRule setMaskType(MaskType maskType) {
        this.maskType = maskType;
        return this;
    }

    public String getReplacement() {
        return replacement;
    }

    public RichTextMaskingRule setReplacement(String replacement) {
        this.replacement = replacement;
        return this;
    }

    public int getPreserveLeft() {
        return preserveLeft;
    }

    public RichTextMaskingRule setPreserveLeft(int preserveLeft) {
        this.preserveLeft = preserveLeft;
        return this;
    }

    public int getPreserveRight() {
        return preserveRight;
    }

    public RichTextMaskingRule setPreserveRight(int preserveRight) {
        this.preserveRight = preserveRight;
        return this;
    }

    public char getMaskChar() {
        return maskChar;
    }

    public RichTextMaskingRule setMaskChar(char maskChar) {
        this.maskChar = maskChar;
        return this;
    }

    public boolean isProcessHtmlTags() {
        return processHtmlTags;
    }

    public RichTextMaskingRule setProcessHtmlTags(boolean processHtmlTags) {
        this.processHtmlTags = processHtmlTags;
        return this;
    }

    public Set<String> getExcludeTags() {
        return excludeTags;
    }

    public RichTextMaskingRule setExcludeTags(Set<String> excludeTags) {
        this.excludeTags = excludeTags;
        return this;
    }

    public RichTextMaskingRule addExcludeTag(String tag) {
        this.excludeTags.add(tag.toLowerCase());
        return this;
    }

    public Set<String> getIncludeTags() {
        return includeTags;
    }

    public RichTextMaskingRule setIncludeTags(Set<String> includeTags) {
        this.includeTags = includeTags;
        return this;
    }

    public RichTextMaskingRule addIncludeTag(String tag) {
        this.includeTags.add(tag.toLowerCase());
        return this;
    }
}
