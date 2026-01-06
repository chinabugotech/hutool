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

import cn.hutool.v7.core.lang.Console;
import cn.hutool.v7.http.client.engine.ClientEngine;
import cn.hutool.v7.http.client.engine.okhttp.OkHttpEngine;
import cn.hutool.v7.http.meta.Method;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class OkHttpEngineTest {

	@SuppressWarnings("resource")
	@Test
	@Disabled
	public void getTest(){
		final ClientEngine engine = new OkHttpEngine();

		final Request req = Request.of("https://www.hutool.cn/").method(Method.GET);
		final Response res = engine.send(req);

		Console.log(res.getStatus());
		Console.log(res.body());
	}
}
