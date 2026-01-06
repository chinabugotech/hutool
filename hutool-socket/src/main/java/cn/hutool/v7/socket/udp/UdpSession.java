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

import cn.hutool.v7.core.codec.binary.HexUtil;
import cn.hutool.v7.core.io.IORuntimeException;
import cn.hutool.v7.core.io.IoUtil;
import cn.hutool.v7.core.lang.Assert;
import cn.hutool.v7.core.thread.ThreadUtil;
import cn.hutool.v7.log.Log;
import cn.hutool.v7.socket.udp.protocol.UdpDecoder;
import cn.hutool.v7.socket.udp.protocol.UdpEncoder;

import java.io.Closeable;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * UDP会话
 *
 * @param <T> 消息类型
 */
public class UdpSession<T> implements Closeable {
	private static final Log log = Log.get();

	private final DatagramSocket socket;
	private final UdpEncoder<T> encoder;
	private final UdpDecoder<T> decoder;

	private volatile ExecutorService executor;
	private volatile ScheduledExecutorService scheduler;

	private volatile SocketAddress remoteAddress;
	private volatile BiConsumer<T, UdpContext> msgHandler;
	private volatile Consumer<Throwable> errorHandler;

	/**
	 * 缓存大小
	 */
	private int bufferSize;

	/**
	 * 构造
	 *
	 * @param socket  UDP socket
	 * @param encoder 编码器，作为UDP服务时可以为{@code null}
	 * @param decoder 解码器，作为UDP客户端时可以为{@code null}
	 */
	public UdpSession(final DatagramSocket socket, final UdpEncoder<T> encoder, final UdpDecoder<T> decoder) {
		this.socket = Assert.notNull(socket);
		this.encoder = encoder;
		this.decoder = decoder;
		bufferSize = IoUtil.DEFAULT_LARGE_BUFFER_SIZE;
	}

	/**
	 * 设置执行器
	 *
	 * @param executor 执行器
	 * @return this
	 */
	public UdpSession<T> setExecutor(final ExecutorService executor) {
		this.executor = Assert.notNull(executor);
		return this;
	}

	/**
	 * 设置远程地址
	 * <p>仅作为客户端时需要设置</p>
	 *
	 * @param remoteAddress 远程地址
	 * @return this
	 */
	public UdpSession<T> setRemoteAddress(final SocketAddress remoteAddress) {
		this.remoteAddress = remoteAddress;
		return this;
	}

	/**
	 * 设置接收到的UDP消息的处理逻辑
	 *
	 * @param msgHandler 接收到的UDP消息的处理逻辑
	 * @return this
	 */
	public UdpSession<T> setMsgHandler(final BiConsumer<T, UdpContext> msgHandler) {
		this.msgHandler = msgHandler;
		return this;
	}

	/**
	 * 设置异常处理逻辑
	 *
	 * @param errorHandler 异常处理逻辑，{@code null}表示不处理
	 * @return this
	 */
	public UdpSession<T> setErrorHandler(final Consumer<Throwable> errorHandler) {
		this.errorHandler = errorHandler;
		return this;
	}

	/**
	 * 设置缓存大小
	 *
	 * @param bufferSize 缓存大小
	 * @return this
	 */
	public UdpSession<T> setBufferSize(final int bufferSize) {
		this.bufferSize = bufferSize;
		return this;
	}

	/**
	 * 发送数据
	 *
	 * @param data 发送的数据包
	 * @return this
	 * @throws IORuntimeException IO异常
	 */
	public UdpSession<T> send(final T data) throws IORuntimeException {
		return send(data, this.remoteAddress);
	}

	/**
	 * 发送数据到指定地址
	 *
	 * @param data          发送的数据包
	 * @param remoteAddress 远程地址
	 * @return this
	 * @throws IORuntimeException IO异常
	 */
	public UdpSession<T> send(final T data, final SocketAddress remoteAddress) throws IORuntimeException {
		Assert.notNull(encoder, "Encoder can not be null when send data");
		final byte[] payload = encoder.encode(data);
		final DatagramPacket packet = new DatagramPacket(payload, payload.length, remoteAddress);
		try {
			socket.send(packet);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
		return this;
	}

	/**
	 * 发送心跳包（用户实现 heartbeat 消息类型）
	 *
	 * @param heartbeatMsg 心跳消息
	 * @throws IORuntimeException IO异常
	 * @return this
	 */
	public UdpSession<T> sendHeartbeat(final T heartbeatMsg) throws IORuntimeException {
		return send(heartbeatMsg);
	}

	/**
	 * 启动定时心跳任务，定时发送心跳包（用户实现 heartbeat 消息类型）
	 *
	 * @param heartbeatMsg 心跳消息
	 * @param interval     间隔时间
	 * @return 定时任务
	 */
	public ScheduledFuture<?> scheduleHeartbeat(final T heartbeatMsg, final long interval) {
		if (null == this.scheduler) {
			this.scheduler = ThreadUtil.newScheduledExecutor(1);
		}
		return scheduleHeartbeat(heartbeatMsg, interval, this.scheduler);
	}

	/**
	 * 启动定时心跳任务，定时发送心跳包（用户实现 heartbeat 消息类型）
	 *
	 * @param heartbeatMsg 心跳消息
	 * @param interval     间隔时间
	 * @param scheduler    定时线程
	 * @return 定时任务
	 */
	@SuppressWarnings("resource")
	public ScheduledFuture<?> scheduleHeartbeat(final T heartbeatMsg, final long interval, final ScheduledExecutorService scheduler) {
		return scheduler.scheduleAtFixedRate(() -> {
			try {
				sendHeartbeat(heartbeatMsg);
			} catch (final Exception e) {
				log.warn("Send heartbeat failed", e);
				safeInvoke(() -> errorHandler.accept(e));
			}
		}, interval, interval, TimeUnit.MILLISECONDS);
	}

	/**
	 * Session是否保持连接
	 *
	 * @return 是否保持连接
	 */
	public boolean isOpen() {
		return null != socket && !socket.isClosed();
	}

	@Override
	public void close() {
		if (null != this.executor) {
			this.executor.shutdown();
			this.executor = null;
		}
		if (null != this.scheduler) {
			this.scheduler.shutdown();
		}
		IoUtil.closeQuietly(socket);
	}

	/**
	 * 启动接收循环（非阻塞）
	 *
	 * @return this
	 */
	public UdpSession<T> start() {
		Assert.notNull(decoder, "Decoder can not be null when start receive loop");
		if (null == executor) {
			executor = ThreadUtil.newExecutor();
		}
		executor.submit(this::receiveLoop);
		return this;
	}

	private void receiveLoop() {
		final byte[] buffer = new byte[bufferSize];
		final DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

		while (!Thread.currentThread().isInterrupted()) {
			try {
				socket.receive(packet);
				final int len = packet.getLength();
				// 校验 & 解码
				if (decoder.getMinLength() > 0 && len < decoder.getMinLength()) {
					log.warn("Packet too short: {} < {}", len, decoder.getMinLength());
					continue;
				}

				final byte[] data = new byte[len];
				System.arraycopy(packet.getData(), packet.getOffset(), data, 0, len);
				if (!decoder.isValid(data)) {
					log.warn("Packet validation failed. Hex: {}", HexUtil.encodeStr(data));
					continue;
				}

				onMessage(decoder.decode(data), new UdpContext(packet.getSocketAddress()));
			} catch (final SocketException e) {
				onError(e);
				break; // socket closed
			} catch (final Exception e) {
				onError(e);
			} finally {
				packet.setLength(buffer.length); // reset for next receive
			}
		}
		close();
	}

	private void onMessage(final T msg, final UdpContext context) {
		safeInvoke(() -> Optional.of(this.msgHandler).ifPresent(c -> c.accept(msg, context)));
	}

	private void onError(final Throwable e) {
		safeInvoke(() -> Optional.of(this.errorHandler).ifPresent(c -> c.accept(e)));
	}

	private void safeInvoke(final Runnable task) {
		try {
			executor.execute(task);
		} catch (final RejectedExecutionException e) {
			log.warn("Callback executor rejected task", e);
		}
	}
}
