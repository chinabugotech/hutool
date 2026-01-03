/*
 * Copyright (c) 2013-2025 Hutool Team and hutool.cn
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

package cn.hutool.v7.socket;

import cn.hutool.v7.core.io.IORuntimeException;
import cn.hutool.v7.socket.aio.AioClient;
import cn.hutool.v7.socket.aio.AioServer;
import cn.hutool.v7.socket.aio.IoAction;
import cn.hutool.v7.socket.nio.ChannelHandler;
import cn.hutool.v7.socket.nio.NioClient;
import cn.hutool.v7.socket.nio.NioServer;
import cn.hutool.v7.socket.udp.UdpUtil;
import cn.hutool.v7.socket.udp.protocol.UdpEncoder;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;

/**
 * Socket相关工具类
 *
 * @author Looly
 * @since 4.5.0
 */
public class SocketUtil {

	// region ----- connect

	/**
	 * 创建Socket并连接到指定地址的服务器
	 *
	 * @param hostname 地址
	 * @param port     端口
	 * @return {@link Socket}
	 * @throws IORuntimeException IO异常
	 * @since 5.7.8
	 */
	public static Socket connect(final String hostname, final int port) throws IORuntimeException {
		return connect(hostname, port, -1);
	}

	/**
	 * 创建Socket并连接到指定地址的服务器
	 *
	 * @param hostname          地址
	 * @param port              端口
	 * @param connectionTimeout 连接超时
	 * @return {@link Socket}
	 * @throws IORuntimeException IO异常
	 * @since 5.7.8
	 */
	public static Socket connect(final String hostname, final int port, final int connectionTimeout) throws IORuntimeException {
		return connect(new InetSocketAddress(hostname, port), connectionTimeout);
	}

	/**
	 * 创建Socket并连接到指定地址的服务器
	 *
	 * @param address           地址
	 * @param connectionTimeout 连接超时
	 * @return {@link Socket}
	 * @throws IORuntimeException IO异常
	 * @since 5.7.8
	 */
	public static Socket connect(final InetSocketAddress address, final int connectionTimeout) throws IORuntimeException {
		final Socket socket = new Socket();
		try {
			if (connectionTimeout <= 0) {
				socket.connect(address);
			} else {
				socket.connect(address, connectionTimeout);
			}
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
		return socket;
	}
	// endregion

	// region ----- udp
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
		UdpUtil.sendByUdp(host, port, message, encoder);
	}
	// endregion

	// region ----- aio

	/**
	 * 创建AIO客户端
	 *
	 * @param address  服务器地址
	 * @param ioAction 数据读写回调
	 * @return AIO客户端实例
	 */
	public static AioClient ofAioClient(final InetSocketAddress address, final IoAction<ByteBuffer> ioAction) {
		return new AioClient(address, ioAction);
	}

	/**
	 * 创建AIO客户端
	 *
	 * @param address  服务器地址
	 * @param ioAction 数据读写回调
	 * @param config   Socket配置
	 * @return AIO客户端实例
	 */
	public static AioClient ofAioClient(final InetSocketAddress address, final IoAction<ByteBuffer> ioAction, final SocketConfig config) {
		return new AioClient(address, ioAction, config);
	}

	/**
	 * 创建AIO服务器
	 *
	 * @param port     端口号
	 * @param ioAction 数据读写回调
	 * @return AIO服务器实例
	 */
	@SuppressWarnings("resource")
	public static AioServer ofAioServer(final int port, final IoAction<ByteBuffer> ioAction) {
		return new AioServer(port).setIoAction(ioAction);
	}

	/**
	 * 创建一个AIO服务器
	 *
	 * @param address  服务器地址
	 * @param config   套接字配置
	 * @param ioAction IO操作回调
	 * @return AIO服务器
	 */
	@SuppressWarnings("resource")
	public static AioServer ofAioServer(final SocketAddress address, final SocketConfig config, final IoAction<ByteBuffer> ioAction) {
		return new AioServer(address, config).setIoAction(ioAction);
	}
	// endregion

	// region ----- nio

	/**
	 * 创建NIO客户端
	 *
	 * @param address        服务器地址
	 * @param channelHandler 数据读写回调
	 * @return NIO客户端实例
	 */
	@SuppressWarnings("resource")
	public static NioClient ofNioClient(final SocketAddress address, final ChannelHandler channelHandler) {
		return new NioClient(address).setChannelHandler(channelHandler);
	}

	/**
	 * 创建NIO服务器
	 *
	 * @param port           端口号
	 * @param channelHandler 数据读写回调
	 * @return NIO服务器实例
	 */
	@SuppressWarnings("resource")
	public static NioServer ofNioServer(final int port, final ChannelHandler channelHandler) {
		return new NioServer(port).setChannelHandler(channelHandler);
	}
	// endregion
}
