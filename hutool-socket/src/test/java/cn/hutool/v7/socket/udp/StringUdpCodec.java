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

import cn.hutool.v7.core.util.CharsetUtil;
import cn.hutool.v7.socket.udp.protocol.UdpDecoder;
import cn.hutool.v7.socket.udp.protocol.UdpEncoder;

import java.nio.charset.StandardCharsets;

public class StringUdpCodec implements UdpEncoder<String>, UdpDecoder<String> {
	@Override
	public byte[] encode(final String msg) {
		return msg.getBytes(CharsetUtil.UTF_8);
	}

	@Override
	public int getMinLength() {
		return 1; // 至少 1 字节
	}

	@Override
	public boolean isValid(final byte[] data) {
		// 简单校验：非空即可；实际项目可加校验和、魔数等
		return data != null && data.length > 0;
	}

	@Override
	public String decode(final byte[] data) {
		return new String(data, StandardCharsets.UTF_8);
	}
}
