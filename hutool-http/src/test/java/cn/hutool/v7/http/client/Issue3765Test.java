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

package cn.hutool.v7.http.client;

import cn.hutool.v7.http.HttpUtil;
import cn.hutool.v7.http.client.engine.ClientEngine;
import cn.hutool.v7.http.client.engine.ClientEngineFactory;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class Issue3765Test {

	public static void main(final String[] args) {
		HttpUtil.createServer(8888)
			.setRoot("d:/test/www")
			.start();
	}
	@Test
	@Disabled
	void downloadTest() {
		final String url = "http://localhost:8888/a.mp3";
		final ClientEngine engine = ClientEngineFactory.createEngine("httpclient4");
		Request.of(url)
			.send(engine)
				.body().write("d:/test/");
	}
}
