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

package cn.hutool.v7.socket.udp.protocol;

import cn.hutool.v7.socket.SocketRuntimeException;

/**
 * UDP 数据编码器：业务对象 → 字节数组
 *
 * @param <T> 业务对象类型
 * @author Looly
 * @since 7.0.0
 */
@FunctionalInterface
public interface UdpEncoder<T> {
	/**
	 * 将业务对象编码为字节数组
	 *
	 * @param data 业务数据（非 null）
	 * @return 非 null 字节数组，长度 ≥ 0
	 * @throws SocketRuntimeException 编码失败（如字段缺失、溢出）
	 */
	byte[] encode(T data) throws SocketRuntimeException;
}
