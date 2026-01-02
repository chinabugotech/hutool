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
import cn.hutool.v7.core.io.IoUtil;
import cn.hutool.v7.core.thread.ThreadFactoryBuilder;
import cn.hutool.v7.socket.nio.Operation;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.Selector;
import java.util.concurrent.ExecutionException;

/**
 * Channel相关封装
 *
 * @author Looly
 * @since 5.8.2
 */
public class ChannelUtil {

	/**
	 * 注册通道的指定操作到指定Selector上
	 *
	 * @param selector Selector
	 * @param channel 通道
	 * @param ops 注册的通道监听（操作）类型
	 */
	public static void registerChannel(final Selector selector, final SelectableChannel channel, final Operation ops) {
		if (channel == null) {
			return;
		}

		try {
			channel.configureBlocking(false);
			// 注册通道
			//noinspection MagicConstant
			channel.register(selector, ops.getValue());
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 创建{@link AsynchronousChannelGroup}
	 *
	 * @param poolSize 线程池大小
	 * @return {@link AsynchronousChannelGroup}
	 */
	public static AsynchronousChannelGroup createFixedGroup(final int poolSize) {

		try {
			return AsynchronousChannelGroup.withFixedThreadPool(//
					poolSize, // 默认线程池大小
					ThreadFactoryBuilder.of().setNamePrefix("Huool-socket-").build()//
			);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 连接到指定地址
	 *
	 * @param group   {@link AsynchronousChannelGroup}
	 * @param address 地址信息，包括地址和端口
	 * @return {@link AsynchronousSocketChannel}
	 */
	public static AsynchronousSocketChannel connect(final AsynchronousChannelGroup group, final InetSocketAddress address) {
		final AsynchronousSocketChannel channel;
		try {
			channel = AsynchronousSocketChannel.open(group);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}

		try {
			channel.connect(address).get();
		} catch (final InterruptedException | ExecutionException e) {
			IoUtil.closeQuietly(channel);
			throw new SocketRuntimeException(e);
		}
		return channel;
	}
}
