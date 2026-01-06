/*
 * Copyright (c) 2013-2026 Hutool Team.
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

package cn.hutool.v7.extra.emoji;

import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiManager;
import com.vdurmont.emoji.EmojiParser;
import com.vdurmont.emoji.EmojiParser.FitzpatrickAction;

import java.util.List;
import java.util.Set;

/**
 * 基于https://github.com/vdurmont/emoji-java的Emoji表情工具类
 * <p>
 * emoji-java文档以及别名列表见：<a href="https://github.com/vdurmont/emoji-java">...</a>
 *
 * @author Looly
 * @since 4.2.1
 */
public class EmojiUtil {

	/**
	 * 是否为Emoji表情的Unicode符
	 *
	 * @param str 被测试的字符串
	 * @return 是否为Emoji表情的Unicode符
	 */
	public static boolean isEmoji(final String str) {
		return EmojiManager.isEmoji(str);
	}

	/**
	 * 是否包含Emoji表情的Unicode符
	 *
	 * @param str 被测试的字符串
	 * @return 是否包含Emoji表情的Unicode符
	 * @since 4.5.11
	 */
	public static boolean containsEmoji(final String str) {
		return EmojiManager.containsEmoji(str);
	}

	/**
	 * 通过tag方式获取对应的所有Emoji表情
	 *
	 * @param tag tag标签，例如“happy”
	 * @return Emoji表情集合，如果找不到返回null
	 */
	public static Set<Emoji> getByTag(final String tag) {
		return EmojiManager.getForTag(tag);
	}

	/**
	 * 通过别名获取Emoji
	 *
	 * @param alias 别名，例如“smile”
	 * @return Emoji对象，如果找不到返回null
	 */
	public static Emoji get(final String alias) {
		return EmojiManager.getForAlias(alias);
	}

	/**
	 * 将子串中的Emoji别名（两个":"包围的格式）和其HTML表示形式替换为为Unicode Emoji符号
	 * <p>
	 * 例如：
	 *
	 * <pre>
	 *  {@code :smile:}  替换为 {@code 😄}
	 * {@code &#128516;} 替换为 {@code 😄}
	 * {@code :boy|type_6:} 替换为 {@code 👦🏿}
	 * </pre>
	 *
	 * @param str 包含Emoji别名或者HTML表现形式的字符串
	 * @return 替换后的字符串
	 */
	public static String toUnicode(final String str) {
		return EmojiParser.parseToUnicode(str);
	}

	/**
	 * 将字符串中的Unicode Emoji字符转换为别名表现形式（两个":"包围的格式）
	 * <p>
	 * 例如： {@code 😄} 转换为 {@code :smile:}
	 *
	 * <p>
	 * {@link FitzpatrickAction}参数被设置为{@link FitzpatrickAction#PARSE}，则别名后会增加"|"并追加fitzpatrick类型
	 * <p>
	 * 例如：{@code 👦🏿} 转换为 {@code :boy|type_6:}
	 *
	 * <p>
	 * {@link FitzpatrickAction}参数被设置为{@link FitzpatrickAction#REMOVE}，则别名后的"|"和类型将被去除
	 * <p>
	 * 例如：{@code 👦🏿} 转换为 {@code :boy:}
	 *
	 * <p>
	 * {@link FitzpatrickAction}参数被设置为{@link FitzpatrickAction#IGNORE}，则别名后的类型将被忽略
	 * <p>
	 * 例如：{@code 👦🏿} 转换为 {@code :boy:🏿}
	 *
	 * @param str 包含Emoji Unicode字符的字符串
	 * @return 替换后的字符串
	 */
	public static String toAlias(final String str) {
		return toAlias(str, FitzpatrickAction.PARSE);
	}

	/**
	 * 将字符串中的Unicode Emoji字符转换为别名表现形式（两个":"包围的格式），别名后会增加"|"并追加fitzpatrick类型
	 * <p>
	 * 例如：{@code 👦🏿} 转换为 {@code :boy|type_6:}
	 *
	 * @param str               包含Emoji Unicode字符的字符串
	 * @param fitzpatrickAction {@link FitzpatrickAction}
	 * @return 替换后的字符串
	 */
	public static String toAlias(final String str, final FitzpatrickAction fitzpatrickAction) {
		return EmojiParser.parseToAliases(str, fitzpatrickAction);
	}

	/**
	 * 将字符串中的Unicode Emoji字符转换为HTML 16进制表现形式
	 * <p>
	 * 例如：{@code 👦🏿} 转换为 {@code &#x1f466;}
	 *
	 * @param str 包含Emoji Unicode字符的字符串
	 * @return 替换后的字符串
	 */
	public static String toHtmlHex(final String str) {
		return toHtml(str, true);
	}

	/**
	 * 将字符串中的Unicode Emoji字符转换为HTML表现形式（Hex方式）
	 * <p>
	 * 例如：{@code 👦🏿} 转换为 {@code &#128102;}
	 *
	 * @param str 包含Emoji Unicode字符的字符串
	 * @return 替换后的字符串
	 */
	public static String toHtml(final String str) {
		return toHtml(str, false);
	}

	/**
	 * 将字符串中的Unicode Emoji字符转换为HTML表现形式，例如：
	 * <pre>
	 * 如果为hex形式，{@code 👦🏿} 转换为 {@code &#x1f466;}
	 * 否则，{@code 👦🏿} 转换为 {@code &#128102;}
	 * </pre>
	 *
	 * @param str   包含Emoji Unicode字符的字符串
	 * @param isHex 是否hex形式
	 * @return 替换后的字符串
	 * @since 5.7.21
	 */
	public static String toHtml(final String str, final boolean isHex) {
		return isHex ? EmojiParser.parseToHtmlHexadecimal(str) :
				EmojiParser.parseToHtmlDecimal(str);
	}

	/**
	 * 去除字符串中所有的Emoji Unicode字符
	 *
	 * @param str 包含Emoji字符的字符串
	 * @return 替换后的字符串
	 */
	public static String removeAllEmojis(final String str) {
		return EmojiParser.removeAllEmojis(str);
	}

	/**
	 * 提取字符串中所有的Emoji Unicode
	 *
	 * @param str 包含Emoji字符的字符串
	 * @return Emoji字符列表
	 */
	public static List<String> extractEmojis(final String str) {
		return EmojiParser.extractEmojis(str);
	}
}
