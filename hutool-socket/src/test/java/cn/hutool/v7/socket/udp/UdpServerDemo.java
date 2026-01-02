/*
 * Copyright (c) 2026 Hutool Team.
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

import java.net.InetSocketAddress;

public class UdpServerDemo {
	@SuppressWarnings("resource")
	public static void main(final String[] args) {
		// 创建服务端会话，绑定 9999 端口
		final UdpSession<String> server = UdpUtil.ofServer(new InetSocketAddress(9999), new StringUdpCodec(), new StringUdpCodec());
			// 设置消息处理器：回显客户端消息 + " [server echo]"
			server.setMsgHandler((msg, context) -> {
				Console.log("[Server] From [{}] received: {}", context.remoteAddress(), msg);
				// 回发消息
				server.send("Server received: " + msg, context.remoteAddress());
				//UdpSession.ofSender(context.remoteAddress(), new StringUdpCodec()).send(msg + " [server echo]");
			})
			// 异常处理器
			.setErrorHandler(Throwable::printStackTrace)
			// 启动接收循环
			.start();

		Console.log("[Server] UDP server started on port 9999. Waiting for messages...");

		// 保持主线程运行
		ThreadUtil.waitForDie();
	}
}
