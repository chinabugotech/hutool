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

package cn.hutool.v7.extra.mq;

import cn.hutool.v7.core.thread.ThreadUtil;

import java.io.Closeable;

/**
 * 消息消费者接口
 *
 * @author Looly
 * @since 7.0.0
 */
public interface Consumer extends Closeable {
	/**
	 * 单次订阅消息
	 *
	 * @param messageHandler 消息处理器
	 */
	void subscribe(MessageHandler messageHandler);

	/**
	 * 持续订阅消息
	 *
	 * @param messageHandler 消息处理器
	 */
	default void listen(final MessageHandler messageHandler) {
		ThreadUtil.execAsync(() -> {
			for(;;) {
				this.subscribe(messageHandler);
			}
		});
	}
}
