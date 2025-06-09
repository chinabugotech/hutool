package org.dromara.hutool.core.data;

import org.dromara.hutool.core.data.masking.RichTextMaskingProcessor;
import org.dromara.hutool.core.data.masking.RichTextMaskingRule;

/**
 * 富文本脱敏工具类，提供对富文本内容的脱敏处理功能
 *
 * @author xjf
 */
public class RichTextMaskingUtil {

    /**
     * 默认的富文本脱敏处理器
     */
    private static final RichTextMaskingProcessor DEFAULT_PROCESSOR = createDefaultProcessor();

    /**
     * 创建默认的富文本脱敏处理器
     *
     * @return 默认的富文本脱敏处理器
     */
    private static RichTextMaskingProcessor createDefaultProcessor() {
        RichTextMaskingProcessor processor = new RichTextMaskingProcessor(true);

        // 添加一些常用的脱敏规则

        // 邮箱脱敏规则
        processor.addRule(new RichTextMaskingRule(
                "邮箱",
                "[\\w.-]+@[\\w.-]+\\.\\w+",
                RichTextMaskingRule.MaskType.PARTIAL,
                "[邮箱已隐藏]")
                .setPreserveLeft(1)
                .setPreserveRight(0)
                .setMaskChar('*'));

        // 网址脱敏规则
        processor.addRule(new RichTextMaskingRule(
                "网址",
                "https?://[\\w.-]+(?:/[\\w.-]*)*",
                RichTextMaskingRule.MaskType.REPLACE,
                "[网址已隐藏]"));

        // 敏感词脱敏规则（示例）
        processor.addRule(new RichTextMaskingRule(
                "敏感词",
                "(机密|绝密|内部资料|秘密|保密)",
                RichTextMaskingRule.MaskType.FULL,
                "***")
                .setMaskChar('*'));

        return processor;
    }

    /**
     * 对富文本内容进行脱敏处理
     *
     * @param text 富文本内容
     * @return 脱敏后的文本
     */
    public static String mask(String text) {
        return DEFAULT_PROCESSOR.mask(text);
    }

    /**
     * 使用自定义处理器对富文本内容进行脱敏处理
     *
     * @param text 富文本内容
     * @param processor 自定义处理器
     * @return 脱敏后的文本
     */
    public static String mask(String text, RichTextMaskingProcessor processor) {
        return processor.mask(text);
    }

    /**
     * 创建一个新的富文本脱敏处理器
     *
     * @param preserveHtmlTags 是否保留HTML标签
     * @return 富文本脱敏处理器
     */
    public static RichTextMaskingProcessor createProcessor(boolean preserveHtmlTags) {
        return new RichTextMaskingProcessor(preserveHtmlTags);
    }

    /**
     * 创建一个邮箱脱敏规则
     *
     * @return 邮箱脱敏规则
     */
    public static RichTextMaskingRule createEmailRule() {
        return new RichTextMaskingRule(
                "邮箱",
                "[\\w.-]+@[\\w.-]+\\.\\w+",
                RichTextMaskingRule.MaskType.PARTIAL,
                null)
                .setPreserveLeft(1)
                .setPreserveRight(0)
                .setMaskChar('*');
    }

    /**
     * 创建一个网址脱敏规则
     *
     * @param replacement 替换文本
     * @return 网址脱敏规则
     */
    public static RichTextMaskingRule createUrlRule(String replacement) {
        return new RichTextMaskingRule(
                "网址",
                "https?://[\\w.-]+(?:/[\\w.-]*)*",
                RichTextMaskingRule.MaskType.REPLACE,
                replacement);
    }

    /**
     * 创建一个敏感词脱敏规则
     *
     * @param pattern 敏感词正则表达式
     * @return 敏感词脱敏规则
     */
    public static RichTextMaskingRule createSensitiveWordRule(String pattern) {
        return new RichTextMaskingRule(
                "敏感词",
                pattern,
                RichTextMaskingRule.MaskType.FULL,
                null)
                .setMaskChar('*');
    }

    /**
     * 创建一个自定义脱敏规则
     *
     * @param name 规则名称
     * @param pattern 匹配模式（正则表达式）
     * @param maskType 脱敏类型
     * @param replacement 替换内容
     * @return 自定义脱敏规则
     */
    public static RichTextMaskingRule createCustomRule(String name, String pattern,
                                                      RichTextMaskingRule.MaskType maskType,
                                                      String replacement) {
        return new RichTextMaskingRule(name, pattern, maskType, replacement);
    }

    /**
     * 创建一个部分脱敏规则
     *
     * @param name 规则名称
     * @param pattern 匹配模式（正则表达式）
     * @param preserveLeft 保留左侧字符数
     * @param preserveRight 保留右侧字符数
     * @param maskChar 脱敏字符
     * @return 部分脱敏规则
     */
    public static RichTextMaskingRule createPartialMaskRule(String name, String pattern,
                                                           int preserveLeft, int preserveRight,
                                                           char maskChar) {
        return new RichTextMaskingRule(name, pattern, preserveLeft, preserveRight, maskChar);
    }
}
