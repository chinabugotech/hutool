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

package cn.hutool.v7.extra.ssh;

import cn.hutool.v7.core.io.IoUtil;
import cn.hutool.v7.core.lang.Console;
import cn.hutool.v7.core.util.CharsetUtil;
import cn.hutool.v7.extra.ssh.engine.jsch.JschSession;
import cn.hutool.v7.extra.ssh.engine.jsch.JschSftp;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;

/**
 * Jsch工具类单元测试
 *
 * @author Looly
 *
 */
public class JschTest {

	@SuppressWarnings("resource")
	@Test
	@Disabled
	public void bindPortTest() {
		//新建会话，此会话用于ssh连接到跳板机（堡垒机），此处为10.1.1.1:22
		final JschSession session = new JschSession(Connector.of("Looly.centos", 22, "test", "123456"));
		// 将堡垒机保护的内网8080端口映射到localhost，我们就可以通过访问http://localhost:8080/访问内网服务了
		session.bindLocalPort(8080, new InetSocketAddress("172.20.12.123", 8080));
	}

	@SuppressWarnings("resource")
	@Test
	@Disabled
	public void bindRemotePort() {
		// 建立会话
		final JschSession session = new JschSession(Connector.of("Looly.centos", 22, "test", "123456"));
		// 绑定ssh服务端8089端口到本机的8000端口上
		session.bindRemotePort(new InetSocketAddress(8089), new InetSocketAddress("localhost", 8000));
		// 保证一直运行
	}

	@SuppressWarnings("resource")
	@Test
	@Disabled
	public void sftpTest() {
		final JschSession session = new JschSession(Connector.of("Looly.centos", 22, "root", "123456"));
		final JschSftp jschSftp = session.openSftp(CharsetUtil.UTF_8);
		jschSftp.mkDirs("/opt/test/aaa/bbb");
		Console.log("OK");
	}

	@SuppressWarnings("CallToPrintStackTrace")
	@Test
	@Disabled
	public void reconnectIfTimeoutTest() throws InterruptedException {
		final JschSession session = new JschSession(Connector.of("sunnyserver", 22,"mysftp","liuyang1234"));
		final JschSftp jschSftp = session.openSftp(CharsetUtil.UTF_8);

		Console.log("打印pwd: " + jschSftp.pwd());
		Console.log("cd / : " + jschSftp.cd("/"));
		Console.log("休眠一段时间，查看是否超时");
		Thread.sleep(30 * 1000);

		try{
			// 当连接超时时，isConnected()仍然返回true，pwd命令也能正常返回，因此，利用发送cd命令的返回结果，来判断是否连接超时
			Console.log("isConnected " + jschSftp.getClient().isConnected());
			Console.log("打印pwd: " + jschSftp.pwd());
			Console.log("cd / : " + jschSftp.cd("/"));
		}catch (final SshException e) {
			e.printStackTrace();
		}

		Console.log("调用reconnectIfTimeout方法，判断是否超时并重连");
		jschSftp.reconnectIfTimeout();

		Console.log("打印pwd: " + jschSftp.pwd());

		IoUtil.closeQuietly(jschSftp);
	}
}
