package org.dromara.hutool.core.data.masking;

import org.dromara.hutool.core.text.StrUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 富文本脱敏处理器，用于对富文本内容进行脱敏处理
 *
 * @author xjf
 */
public class RichTextMaskingProcessor {

    /**
     * 脱敏规则列表
     */
    private final List<RichTextMaskingRule> rules = new ArrayList<>();

    /**
     * 是否保留HTML标签
     */
    private boolean preserveHtmlTags = true;

    /**
     * 默认的脱敏字符
     */
    private char defaultMaskChar = '*';

    /**
     * 构造函数
     */
    public RichTextMaskingProcessor() {
    }

    /**
     * 构造函数
     *
     * @param preserveHtmlTags 是否保留HTML标签
     */
    public RichTextMaskingProcessor(boolean preserveHtmlTags) {
        this.preserveHtmlTags = preserveHtmlTags;
    }

    /**
     * 添加脱敏规则
     *
     * @param rule 脱敏规则
     * @return this
     */
    public RichTextMaskingProcessor addRule(RichTextMaskingRule rule) {
        this.rules.add(rule);
        return this;
    }

    /**
     * 设置默认的脱敏字符
     *
     * @param defaultMaskChar 默认的脱敏字符
     * @return this
     */
    public RichTextMaskingProcessor setDefaultMaskChar(char defaultMaskChar) {
        this.defaultMaskChar = defaultMaskChar;
        return this;
    }

    /**
     * 对文本内容进行脱敏处理
     *
     * @param text 文本内容
     * @return 脱敏后的文本
     */
    public String mask(String text) {
        if (StrUtil.isBlank(text)) {
            return text;
        }

        // 如果是HTML内容，则需要特殊处理
        if (preserveHtmlTags && isHtmlContent(text)) {
            return maskHtmlContent(text);
        } else {
            // 普通文本直接处理
            return maskPlainText(text);
        }
    }

    /**
     * 判断是否为HTML内容
     *
     * @param text 文本内容
     * @return 是否为HTML内容
     */
    private boolean isHtmlContent(String text) {
        // 简单判断是否包含HTML标签
        return text.contains("<") && text.contains(">") &&
               (text.contains("</") || text.contains("/>"));
    }

    /**
     * 对HTML内容进行脱敏处理
     *
     * @param html HTML内容
     * @return 脱敏后的HTML
     */
    private String maskHtmlContent(String html) {
        StringBuilder result = new StringBuilder();
        int lastIndex = 0;
        boolean inTag = false;
        String currentTag = null;

        for (int i = 0; i < html.length(); i++) {
            char c = html.charAt(i);

            if (c == '<') {
                // 处理标签前的文本内容
                if (!inTag && i > lastIndex) {
                    String textContent = html.substring(lastIndex, i);
                    result.append(processTextContentWithContext(textContent, currentTag));
                }

                inTag = true;
                lastIndex = i;

                // 尝试获取当前标签名
                int tagNameStart = i + 1;
                if (tagNameStart < html.length()) {
                    // 跳过结束标签的斜杠
                    if (html.charAt(tagNameStart) == '/') {
                        tagNameStart++;
                    }

                    // 查找标签名结束位置
                    int tagNameEnd = html.indexOf(' ', tagNameStart);
                    if (tagNameEnd == -1) {
                        tagNameEnd = html.indexOf('>', tagNameStart);
                    }

                    if (tagNameEnd > tagNameStart) {
                        currentTag = html.substring(tagNameStart, tagNameEnd).toLowerCase();
                    }
                }
            } else if (c == '>' && inTag) {
                inTag = false;
                result.append(html, lastIndex, i + 1); // 保留标签
                lastIndex = i + 1;
            }
        }

        // 处理最后一部分
        if (lastIndex < html.length()) {
            if (inTag) {
                // 如果还在标签内，直接添加剩余部分
                result.append(html.substring(lastIndex));
            } else {
                // 处理最后的文本内容
                String textContent = html.substring(lastIndex);
                result.append(processTextContentWithContext(textContent, currentTag));
            }
        }

        return result.toString();
    }

    /**
     * 根据上下文处理文本内容
     *
     * @param text 文本内容
     * @param tagName 当前所在的标签名
     * @return 处理后的文本
     */
    private String processTextContentWithContext(String text, String tagName) {
        if (StrUtil.isBlank(text)) {
            return text;
        }

        String result = text;

        for (RichTextMaskingRule rule : rules) {
            // 检查是否需要根据标签进行过滤
            if (tagName != null) {
                // 如果设置了只包含特定标签且当前标签不在列表中，则跳过
                if (!rule.getIncludeTags().isEmpty() && !rule.getIncludeTags().contains(tagName)) {
                    continue;
                }

                // 如果当前标签在排除列表中，则跳过
                if (rule.getExcludeTags().contains(tagName)) {
                    continue;
                }
            }

            // 应用脱敏规则
            result = applyMaskingRule(result, rule);
        }

        return result;
    }

    /**
     * 对普通文本进行脱敏处理
     *
     * @param text 文本内容
     * @return 脱敏后的文本
     */
    private String maskPlainText(String text) {
        String result = text;

        for (RichTextMaskingRule rule : rules) {
            result = applyMaskingRule(result, rule);
        }

        return result;
    }

    /**
     * 应用脱敏规则
     *
     * @param text 文本内容
     * @param rule 脱敏规则
     * @return 脱敏后的文本
     */
    private String applyMaskingRule(String text, RichTextMaskingRule rule) {
        if (StrUtil.isBlank(text) || StrUtil.isBlank(rule.getPattern())) {
            return text;
        }

        Pattern pattern = Pattern.compile(rule.getPattern());
        Matcher matcher = pattern.matcher(text);

        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            String matched = matcher.group();
            String replacement;

            switch (rule.getMaskType()) {
                case FULL:
                    // 完全脱敏，用脱敏字符替换整个匹配内容
                    replacement = StrUtil.repeat(rule.getMaskChar(), matched.length());
                    break;

                case PARTIAL:
                    // 部分脱敏，保留部分原始内容
                    replacement = partialMask(matched, rule.getPreserveLeft(), rule.getPreserveRight(), rule.getMaskChar());
                    break;

                case REPLACE:
                    // 替换脱敏，用指定文本替换
                    replacement = rule.getReplacement();
                    break;

                default:
                    replacement = matched;
                    break;
            }

            // 处理正则表达式中的特殊字符
            matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement));
        }

        matcher.appendTail(sb);

        return sb.toString();
    }

    /**
     * 部分脱敏，保留部分原始内容
     *
     * @param text 原文本
     * @param preserveLeft 保留左侧字符数
     * @param preserveRight 保留右侧字符数
     * @param maskChar 脱敏字符
     * @return 脱敏后的文本
     */
    private String partialMask(String text, int preserveLeft, int preserveRight, char maskChar) {
        if (StrUtil.isBlank(text)) {
            return text;
        }

        int length = text.length();

        // 调整保留字符数，确保不超过文本长度
        preserveLeft = Math.min(preserveLeft, length);
        preserveRight = Math.min(preserveRight, length - preserveLeft);

        // 计算需要脱敏的字符数
        int maskLength = length - preserveLeft - preserveRight;

        if (maskLength <= 0) {
            return text;
        }

        StringBuilder sb = new StringBuilder(length);

        // 添加左侧保留的字符
        if (preserveLeft > 0) {
            sb.append(text, 0, preserveLeft);
        }

        // 添加脱敏字符
        sb.append(StrUtil.repeat(maskChar, maskLength));

        // 添加右侧保留的字符
        if (preserveRight > 0) {
            sb.append(text, length - preserveRight, length);
        }

        return sb.toString();
    }
}
