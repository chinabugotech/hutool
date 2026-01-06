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

import cn.hutool.v7.core.array.ArrayUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 安全的 UDP 包动态构建器（自动扩容）
 *
 * <p>该类提供了链式 API 用于构建二进制数据包，支持多种数据类型的写入操作。
 * 内部采用动态扩容机制，初始容量为 64 字节，当空间不足时会自动扩容。</p>
 *
 * <h3>使用示例：</h3>
 * <pre>{@code
 * byte[] packet = new PacketBuilder()
 *     .writeByte((byte) 0x01)
 *     .writeInt(12345)
 *     .writeString("Hello World")
 *     .build();
 * }</pre>
 *
 * <h3>特性：</h3>
 * <ul>
 *   <li>支持设置字节序（大端/小端）</li>
 *   <li>链式调用设计</li>
 *   <li>自动内存管理（扩容和缩容）</li>
 *   <li>空值安全处理</li>
 *   <li>支持基本数据类型和字符串写入</li>
 * </ul>
 *
 * @author Looly
 * @since 7.0.0
 */
public class PacketBuilder {

	/**
	 * 数据缓冲区，初始容量为 64 字节
	 */
	private byte[] buffer = new byte[64]; // initial capacity

	/**
	 * 当前写入位置指针
	 */
	private int position = 0;

	/**
	 * 字节序，默认为大端字节序
	 */
	private ByteOrder order = ByteOrder.BIG_ENDIAN;

	/**
	 * 设置字节序
	 *
	 * @param order 字节序，支持 {@link ByteOrder#BIG_ENDIAN} 或 {@link ByteOrder#LITTLE_ENDIAN}
	 * @return this，支持链式调用
	 */
	public PacketBuilder order(final ByteOrder order) {
		this.order = order;
		return this;
	}

	/**
	 * 写入一个字节
	 *
	 * @param b 要写入的字节值
	 * @return this，支持链式调用
	 */
	public PacketBuilder writeByte(final byte b) {
		ensureCapacity(1);
		buffer[position++] = b;
		return this;
	}

	/**
	 * 写入一个 short 值（2字节）
	 *
	 * @param value 要写入的 short 值
	 * @return this，支持链式调用
	 */
	public PacketBuilder writeShort(final short value) {
		ensureCapacity(2);
		ByteBuffer.wrap(buffer, position, 2).order(order).putShort(value);
		position += 2;
		return this;
	}

	/**
	 * 写入一个 int 值（4字节）
	 *
	 * @param value 要写入的 int 值
	 * @return this，支持链式调用
	 */
	public PacketBuilder writeInt(final int value) {
		ensureCapacity(4);
		ByteBuffer.wrap(buffer, position, 4).order(order).putInt(value);
		position += 4;
		return this;
	}

	/**
	 * 写入一个 long 值（8字节）
	 *
	 * @param value 要写入的 long 值
	 * @return this，支持链式调用
	 */
	public PacketBuilder writeLong(final long value) {
		ensureCapacity(8);
		ByteBuffer.wrap(buffer, position, 8).order(order).putLong(value);
		position += 8;
		return this;
	}

	/**
	 * 写入一个 float 值（4字节）
	 *
	 * @param value 要写入的 float 值
	 * @return this，支持链式调用
	 */
	public PacketBuilder writeFloat(final float value) {
		ensureCapacity(4);
		ByteBuffer.wrap(buffer, position, 4).order(order).putFloat(value);
		position += 4;
		return this;
	}

	/**
	 * 写入一个 double 值（8字节）
	 *
	 * @param value 要写入的 double 值
	 * @return this，支持链式调用
	 */
	public PacketBuilder writeDouble(final double value) {
		ensureCapacity(8);
		ByteBuffer.wrap(buffer, position, 8).order(order).putDouble(value);
		position += 8;
		return this;
	}

	/**
	 * 写入字节数组
	 *
	 * @param bytes 要写入的字节数组，如果为 null 或空数组则不进行任何操作
	 * @return this，支持链式调用
	 */
	public PacketBuilder writeBytes(final byte[] bytes) {
		if (bytes == null || bytes.length == 0) return this;
		ensureCapacity(bytes.length);
		System.arraycopy(bytes, 0, buffer, position, bytes.length);
		position += bytes.length;
		return this;
	}

	/**
	 * 写入字符串，使用指定字符集编码
	 *
	 * @param str     要写入的字符串，如果为 null 则写入空字符串
	 * @param charset 字符集
	 * @return this，支持链式调用
	 */
	public PacketBuilder writeString(String str, final Charset charset) {
		if (str == null) str = "";
		return writeBytes(str.getBytes(charset));
	}

	/**
	 * 写入字符串，使用 UTF-8 编码
	 *
	 * @param str 要写入的字符串，如果为 null 则写入空字符串
	 * @return this，支持链式调用
	 */
	public PacketBuilder writeString(final String str) {
		return writeString(str, StandardCharsets.UTF_8);
	}

	/**
	 * 构建最终的字节数组
	 *
	 * <p>如果缓冲区已满，直接返回原数组；否则返回一个大小为当前写入位置的新数组</p>
	 *
	 * @return 构建完成的字节数组
	 */
	public byte[] build() {
		return position == buffer.length ? buffer : ArrayUtil.resize(buffer, position);
	}

	/**
	 * 获取当前已写入的数据长度
	 *
	 * @return 当前写入的字节数
	 */
	public int length() {
		return position;
	}

	/**
	 * 确保缓冲区有足够容量
	 *
	 * <p>当所需空间超过当前容量时，按照两倍扩容或所需大小的较大值进行扩容</p>
	 *
	 * @param needed 需要的额外字节数
	 */
	private void ensureCapacity(final int needed) {
		final int required = position + needed;
		if (required > buffer.length) {
			final int newCap = Math.max(buffer.length << 1, required);
			buffer = ArrayUtil.resize(buffer, newCap);
		}
	}
}
