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

package cn.hutool.v7.socket.udp.protocol;

import cn.hutool.v7.socket.SocketRuntimeException;

/**
 * UDP 数据解码器：字节数组 → 业务对象
 *
 * @param <T> 业务对象类型
 * @author Looly
 * @since 7.0.0
 */
@FunctionalInterface
public interface UdpDecoder<T> {
	/**
	 * 将原始字节解码为业务对象
	 *
	 * @param bytes 原始数据（非 null，长度 ≥ 0）
	 * @return 非 null 业务对象
	 * @throws SocketRuntimeException 解码失败（校验失败、格式错误等）
	 */
	T decode(byte[] bytes) throws SocketRuntimeException;

	/**
	 * 可选：预校验（快速失败）
	 *
	 * @param bytes 原始数据（非 null，长度 ≥ 0）
	 * @return true 表示可能合法，false 表示一定非法
	 */
	default boolean isValid(final byte[] bytes) {
		return bytes != null && bytes.length > 0;
	}

	/**
	 * 可选：最小合法长度
	 *
	 * @return >0 表示需至少这么多字节；≤0 表示无限制
	 */
	default int getMinLength() {
		return 0;
	}
}
