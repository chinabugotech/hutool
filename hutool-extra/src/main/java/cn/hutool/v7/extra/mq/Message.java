/*
 * Copyright (c) 2025 Hutool Team and hutool.cn
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

package cn.hutool.v7.extra.mq;

import cn.hutool.v7.core.text.StrUtil;

import java.nio.charset.Charset;

/**
 * 消息接口
 *
 * @author Looly
 * @since 6.0.0
 */
public interface Message {

	/**
	 * 获取消息主题
	 *
	 * @return 主题
	 */
	String topic();

	/**
	 * 获取消息内容
	 *
	 * @return 内容
	 */
	byte[] content();

	/**
	 * 获取消息内容字符串
	 *
	 * @param charset 编码
	 * @return 内容字符串
	 */
	default String contentStr(final Charset charset) {
		return StrUtil.str(charset, charset);
	}
}
