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

import cn.hutool.v7.core.thread.ThreadUtil;
import cn.hutool.v7.socket.SocketRuntimeException;
import cn.hutool.v7.socket.udp.protocol.UdpDecoder;
import cn.hutool.v7.socket.udp.protocol.UdpEncoder;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;

/**
 * UDP工具类，用于创建UDP会话
 *
 * @author Looly
 * @since 7.0.0
 */
public class UdpUtil {

	// region ----- client
	/**
	 * 通过UDP发送消息
	 *
	 * @param <T>     消息类型
	 * @param host    目标主机地址
	 * @param port    目标端口号
	 * @param message 要发送的消息内容
	 * @param encoder 消息编码器，用于将指定类型的消息转为bytes
	 */
	public static <T> void sendByUdp(final String host, final int port, final T message, final UdpEncoder<T> encoder) {
		try (final UdpSession<T> session = UdpUtil.ofSender(host, port, encoder)) {
			session.send(message);
		}
	}

	/**
	 * 创建UDP客户端消息发送器，发送器没有状态，没有连接，不会接收消息，只是单纯发送
	 *
	 * @param host    远程主机地址
	 * @param port    端口号
	 * @param encoder 编码器
	 * @param <T>     消息类型
	 * @return UDP会话
	 */
	public static <T> UdpSession<T> ofSender(final String host, final int port, final UdpEncoder<T> encoder) {
		return ofSender(new InetSocketAddress(host, port), encoder);
	}

	/**
	 * 创建UDP客户端消息发送器，发送器没有状态，没有连接，不会接收消息，只是单纯发送
	 *
	 * @param remoteAddress 远程主机地址和端口
	 * @param encoder       编码器
	 * @param <T>           消息类型
	 * @return UDP会话
	 */
	public static <T> UdpSession<T> ofSender(final SocketAddress remoteAddress, final UdpEncoder<T> encoder) {
		final UdpSession<T> udpSession;
		try {
			udpSession = new UdpSession<>(new DatagramSocket(), encoder, null);
		} catch (final SocketException e) {
			throw new SocketRuntimeException(e);
		}
		return udpSession.setRemoteAddress(remoteAddress);
	}

	/**
	 * 创建UDP客户端会话，支持服务端的消息接收
	 *
	 * @param host    远程主机地址
	 * @param port    端口号
	 * @param encoder 编码器
	 * @param decoder 解码器，用于处理服务端返回的消息
	 * @param <T>     消息类型
	 * @return UDP会话
	 */
	public static <T> UdpSession<T> ofClient(final String host, final int port, final UdpEncoder<T> encoder, final UdpDecoder<T> decoder) {
		return ofClient(new InetSocketAddress(host, port), encoder, decoder);
	}

	/**
	 * 创建UDP客户端会话，支持服务端的消息接收
	 *
	 * @param remoteAddress 远程主机地址和端口号
	 * @param encoder       编码器
	 * @param decoder       解码器，用于处理服务端返回的消息
	 * @param <T>           消息类型
	 * @return UDP会话
	 */
	public static <T> UdpSession<T> ofClient(final SocketAddress remoteAddress, final UdpEncoder<T> encoder, final UdpDecoder<T> decoder) {
		final UdpSession<T> udpSession;
		try {
			udpSession = new UdpSession<>(new DatagramSocket(), encoder, decoder);
		} catch (final SocketException e) {
			throw new SocketRuntimeException(e);
		}
		return udpSession.setRemoteAddress(remoteAddress);
	}
	// endregion

	// region ----- server
	/**
	 * 创建UDP服务端会话，用于接收客户端消息，而无需发送消息
	 *
	 * @param bindAddress 绑定地址和端口
	 * @param decoder     解码器
	 * @param <T>         消息类型
	 * @return UDP会话
	 */
	public static <T> UdpSession<T> ofServer(final SocketAddress bindAddress, final UdpDecoder<T> decoder) {
		return ofServer(bindAddress, null, decoder);
	}

	/**
	 * 创建UDP服务端会话
	 *
	 * @param bindAddress 绑定地址和端口
	 * @param encoder     编码器
	 * @param decoder     解码器
	 * @param <T>         消息类型
	 * @return UDP会话
	 */
		public static <T> UdpSession<T> ofServer(final SocketAddress bindAddress, final UdpEncoder<T> encoder, final UdpDecoder<T> decoder) {
		return ofServer(bindAddress, encoder, decoder, ThreadUtil.newExecutor(16));
	}

	/**
	 * 创建UDP服务端会话
	 *
	 * @param bindAddress 绑定地址和端口
	 * @param encoder     编码器
	 * @param decoder     解码器
	 * @param executor    执行器
	 * @param <T>         消息类型
	 * @return UDP会话
	 */
	public static <T> UdpSession<T> ofServer(final SocketAddress bindAddress, final UdpEncoder<T> encoder, final UdpDecoder<T> decoder, final ExecutorService executor) {
		try {
			return new UdpSession<>(new DatagramSocket(bindAddress), encoder, decoder)
				.setExecutor(executor);
		} catch (final SocketException e) {
			throw new SocketRuntimeException(e);
		}
	}
	// endregion
}
