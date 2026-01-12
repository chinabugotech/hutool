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

package cn.hutool.v7.core.exception;

import cn.hutool.v7.core.text.StrUtil;

import java.io.Serial;

/**
 * Hutool工具类异常
 *
 * @author Looly
 * @since 7.0.0
 */
public class HutoolException extends RuntimeException {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 构造
	 */
	public HutoolException() {
		super();
	}

	/**
	 * 构造
	 *
	 * @param e 异常
	 */
	public HutoolException(final Throwable e) {
		super(e);
	}

	/**
	 * 构造
	 *
	 * @param message 消息
	 */
	public HutoolException(final String message) {
		super(message);
	}

	/**
	 * 构造
	 *
	 * @param messageTemplate 消息模板
	 * @param params          参数
	 */
	public HutoolException(final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	/**
	 * 构造
	 *
	 * @param message 消息
	 * @param cause   被包装的子异常
	 */
	public HutoolException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * 构造
	 *
	 * @param message            消息
	 * @param cause              被包装的子异常
	 * @param enableSuppression  是否启用抑制
	 * @param writableStackTrace 堆栈跟踪是否应该是可写的
	 */
	public HutoolException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * 构造
	 *
	 * @param cause           被包装的子异常
	 * @param messageTemplate 消息模板
	 * @param params          参数
	 */
	public HutoolException(final Throwable cause, final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params), cause);
	}
}
