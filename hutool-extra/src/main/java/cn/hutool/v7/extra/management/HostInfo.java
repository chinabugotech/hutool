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

package cn.hutool.v7.extra.management;

import cn.hutool.v7.core.net.NetUtil;

import java.io.Serial;
import java.io.Serializable;
import java.net.InetAddress;

/**
 * 代表当前主机的信息。
 */
public class HostInfo implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	private final String name;
	private final String address;

	/**
	 * 构造当前主机信息。
	 */
	public HostInfo() {
		final InetAddress localhost = NetUtil.getLocalhostV4();
		if(null != localhost){
			name = localhost.getHostName();
			address = localhost.getHostAddress();
		} else{
			name = null;
			address = null;
		}
	}

	/**
	 * 取得当前主机的名称。
	 *
	 * <p>
	 * 例如：{@code "webserver1"}
	 * </p>
	 *
	 * @return 主机名
	 */
	public final String getName() {
		return name;
	}

	/**
	 * 取得当前主机的地址。
	 *
	 * <p>
	 * 例如：{@code "192.168.0.1"}
	 * </p>
	 *
	 * @return 主机地址
	 */
	public final String getAddress() {
		return address;
	}

	/**
	 * 将当前主机的信息转换成字符串。
	 *
	 * @return 主机信息的字符串表示
	 */
	@Override
	public final String toString() {
		final StringBuilder builder = new StringBuilder();

		ManagementUtil.append(builder, "Host Name:    ", getName());
		ManagementUtil.append(builder, "Host Address: ", getAddress());

		return builder.toString();
	}

}
