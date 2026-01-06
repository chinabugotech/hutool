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

package cn.hutool.v7.http.server.engine;

import cn.hutool.v7.core.io.file.FileUtil;
import cn.hutool.v7.core.lang.Console;
import cn.hutool.v7.core.net.ssl.SSLContextUtil;
import cn.hutool.v7.crypto.KeyStoreUtil;
import cn.hutool.v7.http.server.ServerConfig;
import cn.hutool.v7.http.server.handler.RouteHttpHandler;

import javax.net.ssl.SSLContext;
import java.security.KeyStore;

public class UndertowTest {
	public static void main(final String[] args) {
		final char[] pwd = "123456".toCharArray();
		final KeyStore keyStore = KeyStoreUtil.readJKSKeyStore(FileUtil.file("d:/test/keystore.jks"), pwd);
		// 初始化SSLContext
		final SSLContext sslContext = SSLContextUtil.createSSLContext(keyStore, pwd);

		final ServerEngine engine = ServerEngineFactory.createEngine("undertow");
		engine.init(ServerConfig.of().setSslContext(sslContext));

		// 自定义路由策略
		engine.setHandler(RouteHttpHandler.of((request, response) -> {
			Console.log(request.getPath());
			response.write("Hutool Undertow response test");
		}).route("/test", (request, response) -> {
			Console.log(request.getPath());
			response.write("test path");
		}));

		engine.start();
	}
}
