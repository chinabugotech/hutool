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

package cn.hutool.v7.core.codec;

import cn.hutool.v7.core.lang.Assert;

/**
 * 凯撒密码实现<br>
 * 仅对{@link #TABLE}中定义的52个大小写英文字母进行位移，其它字符（数字、符号、中文等）保持原样<br>
 * 算法来自：<a href="https://github.com/zhaorenjie110/SymmetricEncryptionAndDecryption">https://github.com/zhaorenjie110/SymmetricEncryptionAndDecryption</a>
 *
 * @author Looly
 */
public class Caesar {

	/**
	 * 26个字母表
	 */
	public static final String TABLE = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz";

	/**
	 * 传入明文，加密得到密文
	 *
	 * @param message 加密的消息
	 * @param offset  偏移量
	 * @return 加密后的内容
	 */
	public static String encode(final String message, final int offset) {
		Assert.notNull(message, "message must be not null!");
		final int len = message.length();
		final char[] plain = message.toCharArray();
		char c;
		for (int i = 0; i < len; i++) {
			c = message.charAt(i);
			if (isNotCaesarChar(c)) {
				continue;
			}
			plain[i] = encodeChar(c, offset);
		}
		return new String(plain);
	}

	/**
	 * 传入明文解密到密文
	 *
	 * @param cipherText 密文
	 * @param offset     偏移量
	 * @return 解密后的内容
	 */
	public static String decode(final String cipherText, final int offset) {
		Assert.notNull(cipherText, "cipherText must be not null!");
		final int len = cipherText.length();
		final char[] plain = cipherText.toCharArray();
		char c;
		for (int i = 0; i < len; i++) {
			c = cipherText.charAt(i);
			if (isNotCaesarChar(c)) {
				continue;
			}
			plain[i] = decodeChar(c, offset);
		}
		return new String(plain);
	}

	// ----------------------------------------------------------------------------------------- Private method start

	/**
	 * 是否为凯撒密码支持的字符，即是否在{@link #TABLE}字母表中<br>
	 * 不在字母表中的字符（如中文等非拉丁字母）无法参与位移计算，将保持原样
	 *
	 * @param c 字符
	 * @return 是否为支持的字符
	 * @since 5.8.48
	 */
	private static boolean isNotCaesarChar(final char c) {
		return TABLE.indexOf(c) < 0;
	}

	/**
	 * 加密轮盘
	 *
	 * @param c      被加密字符
	 * @param offset 偏移量
	 * @return 加密后的字符
	 */
	private static char encodeChar(final char c, final int offset) {
		int position = (TABLE.indexOf(c) + offset) % 52;
		if (position < 0) {
			// 偏移量为负数时取模结果为负数，此处修正
			position += 52;
		}
		return TABLE.charAt(position);

	}

	/**
	 * 解密轮盘
	 *
	 * @param c 字符
	 * @return 解密后的字符
	 */
	private static char decodeChar(final char c, final int offset) {
		int position = (TABLE.indexOf(c) - offset) % 52;
		if (position < 0) {
			position += 52;
		}
		return TABLE.charAt(position);
	}
	// ----------------------------------------------------------------------------------------- Private method end
}
