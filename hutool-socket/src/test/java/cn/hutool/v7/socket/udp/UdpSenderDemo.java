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

package cn.hutool.v7.socket.udp;

import cn.hutool.v7.core.lang.Console;
import cn.hutool.v7.core.thread.ThreadUtil;

public class UdpSenderDemo {
	public static void main(final String[] args) {
		// 创建客户端会话，连接到 localhost:9999
		final UdpSession<String> client = UdpUtil.ofSender("127.0.0.1", 9999, new StringUdpCodec());
		Console.log("[Sender] UDP sender started. Sending messages...");

		// 模拟发几条消息
		client.send("Hello from sender!");
		client.send("How are you?");

		// 启动心跳（每 3 秒一次）
		final String heartbeatMsg = "PING";
		client.scheduleHeartbeat(heartbeatMsg, 3000); // 3s

		// 保持运行
		ThreadUtil.sleep(1000 * 30); // 30秒

		// 关闭
		client.close();
		Console.log("[Sender] Closed.");
	}
}
