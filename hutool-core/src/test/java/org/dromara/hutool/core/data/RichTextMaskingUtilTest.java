package org.dromara.hutool.core.data;

import org.dromara.hutool.core.data.masking.RichTextMaskingProcessor;
import org.dromara.hutool.core.data.masking.RichTextMaskingRule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * 富文本脱敏工具类测试
 *
 * @author xjf
 */
public class RichTextMaskingUtilTest {

    @Test
    public void testDefaultMask() {
        // 测试默认脱敏功能
        String html = "这是一封邮件，联系人：test@example.com，网址：https://www.example.com，包含机密信息。";
        String masked = RichTextMaskingUtil.mask(html);

        // 验证邮箱被脱敏
        Assertions.assertFalse(masked.contains("test@example.com"));
        Assertions.assertTrue(masked.contains("t***"));

        // 验证网址被脱敏
        Assertions.assertFalse(masked.contains("https://www.example.com"));
        Assertions.assertTrue(masked.contains("[网址已隐藏]"));

        // 验证敏感词被脱敏
        Assertions.assertFalse(masked.contains("机密"));
        Assertions.assertTrue(masked.contains("**"));
    }

    @Test
    public void testHtmlContentMask() {
        // 测试HTML内容脱敏
        String html = "<p>这是一封邮件，联系人：<a href='mailto:test@example.com'>test@example.com</a>，" +
                "网址：<a href='https://www.example.com'>https://www.example.com</a>，" +
                "包含<span style='color:red'>机密</span>信息。</p>";
        String masked = RichTextMaskingUtil.mask(html);

        // 验证HTML标签被保留
        Assertions.assertTrue(masked.contains("<p>"));
        Assertions.assertTrue(masked.contains("</p>"));
        Assertions.assertTrue(masked.contains("<a href='mailto:"));
        Assertions.assertTrue(masked.contains("<span style='color:red'>"));

        // 验证邮箱被脱敏
        Assertions.assertFalse(masked.contains("test@example.com"));
        Assertions.assertTrue(masked.contains("t***"));

        // 验证网址被脱敏
        Assertions.assertFalse(masked.contains("https://www.example.com"));
        Assertions.assertTrue(masked.contains("[网址已隐藏]"));

        // 验证敏感词被脱敏
        Assertions.assertFalse(masked.contains("机密"));
        Assertions.assertTrue(masked.contains("**"));
    }

    @Test
    public void testCustomProcessor() {
        // 创建自定义处理器
        RichTextMaskingProcessor processor = RichTextMaskingUtil.createProcessor(true);

        // 添加自定义规则 - 手机号码
        processor.addRule(RichTextMaskingUtil.createPartialMaskRule(
                "手机号",
                "1[3-9]\\d{9}",
                3,
                4,
                '*'));

        // 添加自定义规则 - 公司名称
        processor.addRule(RichTextMaskingUtil.createCustomRule(
                "公司名称",
                "XX科技有限公司",
                RichTextMaskingRule.MaskType.REPLACE,
                "[公司名称已隐藏]"));

        // 测试文本
        String text = "联系电话：13812345678，公司名称：XX科技有限公司";
        String masked = RichTextMaskingUtil.mask(text, processor);

        // 验证手机号被脱敏
        Assertions.assertFalse(masked.contains("13812345678"));
        Assertions.assertTrue(masked.contains("138*****5678"));

        // 验证公司名称被脱敏
        Assertions.assertFalse(masked.contains("XX科技有限公司"));
        Assertions.assertTrue(masked.contains("[公司名称已隐藏]"));
    }

    @Test
    public void testTagFiltering() {
        // 创建自定义处理器
        RichTextMaskingProcessor processor = RichTextMaskingUtil.createProcessor(true);

        // 创建只在特定标签中生效的规则
        RichTextMaskingRule rule = RichTextMaskingUtil.createCustomRule(
                "标签内敏感信息",
                "敏感信息",
                RichTextMaskingRule.MaskType.REPLACE,
                "[已隐藏]");

        // 设置只在div标签中生效
        Set<String> includeTags = new HashSet<>();
        includeTags.add("div");
        rule.setIncludeTags(includeTags);

        processor.addRule(rule);

        // 测试HTML
        String html = "<p>这是一段敏感信息</p><div>这也是一段敏感信息</div>";
        String masked = RichTextMaskingUtil.mask(html, processor);

        // 验证只有div标签中的敏感信息被脱敏
        Assertions.assertTrue(masked.contains("<p>这是一段敏感信息</p>"));
        Assertions.assertTrue(masked.contains("<div>这也是一段[已隐藏]</div>"));
    }

    @Test
    public void testExcludeTags() {
        // 创建自定义处理器
        RichTextMaskingProcessor processor = RichTextMaskingUtil.createProcessor(true);

        // 创建排除特定标签的规则
        RichTextMaskingRule rule = RichTextMaskingUtil.createCustomRule(
                "排除标签内敏感信息",
                "敏感信息",
                RichTextMaskingRule.MaskType.REPLACE,
                "[已隐藏]");

        // 设置排除code标签
        rule.addExcludeTag("code");

        processor.addRule(rule);

        // 测试HTML
        String html = "<p>这是一段敏感信息</p><code>这是代码中的敏感信息</code>";
        String masked = RichTextMaskingUtil.mask(html, processor);

        // 验证code标签中的敏感信息不被脱敏
        Assertions.assertTrue(masked.contains("<p>这是一段[已隐藏]</p>"));
        Assertions.assertTrue(masked.contains("<code>这是代码中的敏感信息</code>"));
    }

    @Test
    public void testComplexHtml() {
        // 测试复杂HTML内容
        String html = "<div class='content'>" +
                "<h1>公司内部文档</h1>" +
                "<p>联系人：张三 <a href='mailto:zhangsan@example.com'>zhangsan@example.com</a></p>" +
                "<p>电话：13812345678</p>" +
                "<div class='secret'>这是一段机密信息，请勿外传</div>" +
                "<pre><code>// 这是一段代码\nString password = \"123456\";</code></pre>" +
                "<p>公司网址：<a href='https://www.example.com'>https://www.example.com</a></p>" +
                "</div>";

        // 创建自定义处理器
        RichTextMaskingProcessor processor = RichTextMaskingUtil.createProcessor(true);

        // 添加邮箱脱敏规则
        processor.addRule(RichTextMaskingUtil.createEmailRule());

        // 添加手机号脱敏规则
        processor.addRule(RichTextMaskingUtil.createPartialMaskRule(
                "手机号",
                "1[3-9]\\d{9}",
                3,
                4,
                '*'));

        // 添加敏感词脱敏规则
        processor.addRule(RichTextMaskingUtil.createSensitiveWordRule("机密|内部"));

        // 添加网址脱敏规则
        processor.addRule(RichTextMaskingUtil.createUrlRule("[网址已隐藏]"));

        // 添加密码脱敏规则，但排除code标签
        RichTextMaskingRule passwordRule = RichTextMaskingUtil.createCustomRule(
                "密码",
                "password = \"[^\"]+\"",
                RichTextMaskingRule.MaskType.REPLACE,
                "password = \"******\"");
        passwordRule.addExcludeTag("code");
        processor.addRule(passwordRule);

        String masked = RichTextMaskingUtil.mask(html, processor);

        // 验证结果
        Assertions.assertTrue(masked.contains("<h1>公司**文档</h1>"));
        Assertions.assertTrue(masked.contains("z***"));
        Assertions.assertTrue(masked.contains("138*****5678"));
        Assertions.assertTrue(masked.contains("这是一段**信息"));
        Assertions.assertTrue(masked.contains("String password = \"123456\""));
        Assertions.assertTrue(masked.contains("[网址已隐藏]"));
    }
}
