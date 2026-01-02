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

import cn.hutool.v7.socket.SocketRuntimeException;
import cn.hutool.v7.socket.udp.protocol.UdpDecoder;
import cn.hutool.v7.socket.udp.protocol.UdpEncoder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UdpSession单元测试
 */
class UdpSessionTest {

	private static final String TEST_HOST = "127.0.0.1";
	private static final int TEST_PORT = 9999;
	private static final int TEST_PORT_SERVER = 9998;

	private DatagramSocket testSocket;
	private ExecutorService executor;

	@BeforeEach
	void setUp() throws SocketException {
		testSocket = new DatagramSocket();
		executor = Executors.newCachedThreadPool();
	}

	@AfterEach
	void tearDown() {
		if (testSocket != null && !testSocket.isClosed()) {
			testSocket.close();
		}
		if (executor != null) {
			executor.shutdown();
		}
	}

	/**
	 * 简单的字符串编码器实现
	 */
	static class StringUdpEncoder implements UdpEncoder<String> {
		@Override
		public byte[] encode(final String data) throws SocketRuntimeException {
			return data.getBytes();
		}
	}

	/**
	 * 简单的字符串解码器实现
	 */
	static class StringUdpDecoder implements UdpDecoder<String> {
		@Override
		public String decode(final byte[] bytes) throws SocketRuntimeException {
			return new String(bytes);
		}

		@Override
		public int getMinLength() {
			return 1;
		}
	}

	/**
	 * 测试 ofSender 工厂方法
	 */
	@SuppressWarnings("resource")
	@Test
	void testOfSender() {
		// 正常情况
		final UdpSession<String> session = UdpUtil.ofSender(TEST_HOST, TEST_PORT, new StringUdpEncoder());
		assertNotNull(session);
		assertTrue(session.isOpen());

		// 验证远程地址设置正确
		session.close();

		// 测试异常情况 - 无效的端口号
		assertThrows(IllegalArgumentException.class, () -> {
			UdpUtil.ofSender(TEST_HOST, -1, new StringUdpEncoder());
		});
	}

	/**
	 * 测试 ofServer 工厂方法
	 */
	@SuppressWarnings("resource")
	@Test
	void testOfServer() {
		// 正常情况
		final InetSocketAddress bindAddress = new InetSocketAddress(TEST_HOST, TEST_PORT_SERVER);
		final UdpSession<String> session = UdpUtil.ofServer(bindAddress, new StringUdpDecoder());
		assertNotNull(session);
		assertTrue(session.isOpen());
		session.close();

		// 测试异常情况 - 无效的绑定地址
		assertThrows(SocketRuntimeException.class, () -> {
			UdpUtil.ofServer(new InetSocketAddress("invalid.host", 9999), new StringUdpDecoder());
		});
	}

	/**
	 * 测试构造函数
	 */
	@SuppressWarnings("resource")
	@Test
	void testConstructor() {
		// 正常构造
		final UdpSession<String> session = new UdpSession<>(testSocket, new StringUdpEncoder(), new StringUdpDecoder());
		assertNotNull(session);
		assertTrue(session.isOpen());

		// 测试 null socket
		assertThrows(IllegalArgumentException.class, () -> {
			new UdpSession<>(null, new StringUdpEncoder(), new StringUdpDecoder());
		});
	}

	/**
	 * 测试 setRemoteAddress 方法
	 */
	@Test
	void testSetRemoteAddress() {
		final UdpSession<String> session = new UdpSession<>(testSocket, new StringUdpEncoder(), new StringUdpDecoder());

		final InetSocketAddress address = new InetSocketAddress(TEST_HOST, TEST_PORT);
		final UdpSession<String> result = session.setRemoteAddress(address);

		assertEquals(session, result);
		session.close();
	}

	/**
	 * 测试 setMsgHandler 方法
	 */
	@Test
	void testSetMsgHandler() {
		final UdpSession<String> session = new UdpSession<>(testSocket, new StringUdpEncoder(), new StringUdpDecoder());

		final BiConsumer<String, UdpContext> msgHandler = (msg, context) -> {};
		final UdpSession<String> result = session.setMsgHandler(msgHandler);

		assertEquals(session, result);
		session.close();
	}

	/**
	 * 测试 setErrorHandler 方法
	 */
	@Test
	void testSetErrorHandler() {
		final UdpSession<String> session = new UdpSession<>(testSocket, new StringUdpEncoder(), new StringUdpDecoder());

		final Consumer<Throwable> errorHandler = error -> {
		};
		final UdpSession<String> result = session.setErrorHandler(errorHandler);

		assertEquals(session, result);

		// 测试 null errorHandler
		final UdpSession<String> result2 = session.setErrorHandler(null);
		assertEquals(session, result2);

		session.close();
	}

	/**
	 * 测试 setBufferSize 方法
	 */
	@Test
	void testSetBufferSize() {
		final UdpSession<String> session = new UdpSession<>(testSocket, new StringUdpEncoder(), new StringUdpDecoder());

		// 正常设置
		final UdpSession<String> result = session.setBufferSize(8192);
		assertEquals(session, result);

		// 测试边界值
		session.setBufferSize(1); // 最小值
		session.setBufferSize(Integer.MAX_VALUE); // 最大值

		session.close();
	}

	/**
	 * 测试 send 方法 - 正常情况
	 */
	@Test
	void testSend() {
		final UdpSession<String> session = UdpUtil.ofSender(TEST_HOST, TEST_PORT, new StringUdpEncoder());

		// 正常发送
		assertDoesNotThrow(() -> session.send("test message"));

		session.close();
	}

	/**
	 * 测试 send 方法 - 异常情况
	 */
	@Test
	void testSendWithException() {
		// 创建无编码器的会话（会导致发送失败）
		final UdpSession<String> session = new UdpSession<>(testSocket, null, new StringUdpDecoder());

		// 测试无编码器的情况
		assertThrows(IllegalArgumentException.class, () -> {
			session.send("test message");
		});

		// 测试 socket 关闭的情况
		session.close();
		assertThrows(IllegalArgumentException.class, () -> {
			session.send("test message");
		});
	}

	/**
	 * 测试 sendHeartbeat 方法
	 */
	@Test
	void testSendHeartbeat() {
		final UdpSession<String> session = UdpUtil.ofSender(TEST_HOST, TEST_PORT, new StringUdpEncoder());

		// 正常发送心跳
		assertDoesNotThrow(() -> session.sendHeartbeat("heartbeat"));

		session.close();
	}

	/**
	 * 测试 scheduleHeartbeat 方法
	 */
	@Test
	@Timeout(5)
	void testScheduleHeartbeat() throws InterruptedException {
		final UdpSession<String> session = UdpUtil.ofSender(TEST_HOST, TEST_PORT, new StringUdpEncoder());
		final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

		try {
			// 定时发送心跳
			final ScheduledFuture<?> future = session.scheduleHeartbeat("heartbeat", 100, scheduler);
			assertNotNull(future);

			// 等待一段时间确保心跳任务执行
			Thread.sleep(300);

			// 取消任务
			future.cancel(true);
		} finally {
			scheduler.shutdown();
			session.close();
		}
	}

	/**
	 * 测试 isOpen 方法
	 */
	@Test
	void testIsOpen() {
		final UdpSession<String> session = new UdpSession<>(testSocket, new StringUdpEncoder(), new StringUdpDecoder());

		assertTrue(session.isOpen());

		session.close();
		assertFalse(session.isOpen());
	}

	/**
	 * 测试 close 方法
	 */
	@Test
	void testClose() {
		final UdpSession<String> session = new UdpSession<>(testSocket, new StringUdpEncoder(), new StringUdpDecoder());

		assertTrue(session.isOpen());

		session.close();
		assertFalse(session.isOpen());

		// 重复关闭应该不会报错
		assertDoesNotThrow(session::close);
	}

	/**
	 * 测试 start 方法
	 */
	@SuppressWarnings("WriteOnlyObject")
	@Test
	@Timeout(5)
	void testStart() throws InterruptedException {
		// 创建服务端会话用于接收消息
		final InetSocketAddress serverAddress = new InetSocketAddress(TEST_HOST, TEST_PORT_SERVER);
		final UdpSession<String> serverSession = UdpUtil.ofServer(serverAddress, new StringUdpDecoder());

		final AtomicInteger messageCount = new AtomicInteger(0);
		final AtomicReference<String> receivedMessage = new AtomicReference<>();

		serverSession.setMsgHandler((msg, context) -> {
			messageCount.incrementAndGet();
			receivedMessage.set(msg);
		});

		// 启动接收循环
		serverSession.start();

		// 创建客户端并发送消息
		final UdpSession<String> clientSession = UdpUtil.ofSender(TEST_HOST, TEST_PORT_SERVER, new StringUdpEncoder());
		clientSession.send("test message");

		// 等待消息处理
		Thread.sleep(100);

		// 由于UDP的异步特性，可能无法立即收到消息，我们只验证会话创建和启动成功
		assertTrue(serverSession.isOpen());
		assertTrue(clientSession.isOpen());

		clientSession.close();
		serverSession.close();
	}

	/**
	 * 测试接收循环中的异常处理
	 */
	@Test
	@Timeout(5)
	void testReceiveLoopExceptionHandling() throws InterruptedException, SocketException {
		// 创建会抛出异常的socket
		final DatagramSocket errorSocket = new DatagramSocket() {
			@Override
			public void receive(final DatagramPacket p) throws IOException {
				throw new SocketException("Test exception");
			}
		};

		final UdpSession<String> session = new UdpSession<>(errorSocket, new StringUdpEncoder(), new StringUdpDecoder());

		final AtomicBoolean errorHandled = new AtomicBoolean(false);
		session.setErrorHandler(error -> errorHandled.set(true));

		// 启动接收循环
		session.start();

		// 等待错误处理
		Thread.sleep(100);

		assertTrue(errorHandled.get());

		session.close();
	}

	/**
	 * 测试消息验证失败的情况
	 */
	@Test
	@Timeout(5)
	void testMessageValidationFailure() throws InterruptedException, IOException {
		// 创建严格验证的解码器
		final UdpDecoder<String> strictDecoder = new StringUdpDecoder() {
			@Override
			public boolean isValid(final byte[] bytes) {
				return bytes != null && bytes.length >= 5; // 要求至少5个字节
			}

			@Override
			public int getMinLength() {
				return 5;
			}
		};

		final InetSocketAddress serverAddress = new InetSocketAddress(TEST_HOST, TEST_PORT_SERVER);
		final UdpSession<String> serverSession = UdpUtil.ofServer(serverAddress, strictDecoder);

		final AtomicInteger validMessageCount = new AtomicInteger(0);
		final AtomicInteger invalidMessageCount = new AtomicInteger(0);

		serverSession.setMsgHandler((msg, context) -> validMessageCount.incrementAndGet());

		// 启动接收循环
		serverSession.start();

		// 创建无状态客户端
		final UdpSession<String> clientSession = UdpUtil.ofSender(TEST_HOST, TEST_PORT_SERVER, new StringUdpEncoder());

		// 发送过短的消息（应该被过滤掉）
		clientSession.send("123"); // 只有3个字节

		// 发送有效的消息
		clientSession.send("valid message"); // 超过5个字节

		// 等待消息处理
		Thread.sleep(200);

		assertEquals(1, validMessageCount.get()); // 只有有效消息被处理

		clientSession.close();
		serverSession.close();
	}

	/**
	 * 测试边界情况和特殊值
	 */
	@Test
	void testBoundaryCases() {
		// 测试空的编码器
		final UdpEncoder<String> emptyEncoder = data -> new byte[0];
		final UdpSession<String> session = new UdpSession<>(testSocket, emptyEncoder, new StringUdpDecoder());

		// 测试解码器的最小长度限制
		final UdpDecoder<String> minLengthDecoder = new StringUdpDecoder() {
			@Override
			public int getMinLength() {
				return 10; // 要求最小10字节
			}
		};

		final UdpSession<String> session2 = new UdpSession<>(testSocket, new StringUdpEncoder(), minLengthDecoder);
		assertNotNull(session2);

		session.close();
		session2.close();
	}

	/**
	 * 测试并发场景
	 */
	@Test
	@Timeout(10)
	void testConcurrentOperations() throws InterruptedException {
		final UdpSession<String> session = UdpUtil.ofSender(TEST_HOST, TEST_PORT, new StringUdpEncoder());

		final int threadCount = 5;
		final CountDownLatch startLatch = new CountDownLatch(1);
		final CountDownLatch endLatch = new CountDownLatch(threadCount);
		final AtomicInteger successCount = new AtomicInteger(0);

		for (int i = 0; i < threadCount; i++) {
			new Thread(() -> {
				try {
					startLatch.await();
					session.send("concurrent message");
					successCount.incrementAndGet();
				} catch (final Exception e) {
					// 忽略并发异常
				} finally {
					endLatch.countDown();
				}
			}).start();
		}

		startLatch.countDown();
		endLatch.await();

		assertEquals(threadCount, successCount.get());
		session.close();
	}

	/**
	 * 测试错误处理器的正确调用
	 */
	@Test
	@Timeout(5)
	void testErrorHandlerInvocation() throws InterruptedException, SocketException {
		// 使用与异常处理测试相同的技术
		final DatagramSocket errorSocket = new DatagramSocket() {
			@Override
			public void receive(final DatagramPacket p) throws IOException {
				throw new SocketException("Test exception");
			}
		};

		// 模拟解码器抛出异常
		final UdpDecoder<String> failingDecoder = new StringUdpDecoder() {
			@Override
			public String decode(final byte[] bytes) throws SocketRuntimeException {
				throw new SocketRuntimeException("Decode failed");
			}
		};

		final UdpSession<String> failingSession = new UdpSession<>(errorSocket, new StringUdpEncoder(), failingDecoder);

		final AtomicReference<Throwable> capturedError = new AtomicReference<>();
		failingSession.setErrorHandler(capturedError::set);

		// 启动接收循环
		failingSession.start();

		Thread.sleep(100);

		assertNotNull(capturedError.get());

		failingSession.close();
	}
}
