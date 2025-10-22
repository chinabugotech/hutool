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

package cn.hutool.v7.http.client.engine.okhttp;

import okhttp3.*;
import cn.hutool.v7.http.meta.HeaderName;

import java.net.PasswordAuthentication;

/**
 * 账号密码形式的代理验证<br>
 * 生成类似：
 * <pre>
 *     Proxy-Authorization: Basic YWxhZGRpbjpvcGVuc2VzYW1l
 * </pre>
 *
 * @author Looly
 * @since 6.0.0
 * @param auth 账号密码
 */
public record BasicProxyAuthenticator(PasswordAuthentication auth) implements Authenticator {

	@Override
	public Request authenticate(final Route route, final Response response) {
		final String credential = Credentials.basic(
			auth.getUserName(),
			String.valueOf(auth.getPassword()));
		return response.request().newBuilder()
			.addHeader(HeaderName.PROXY_AUTHORIZATION.getValue(), credential)
			.build();
	}
}
