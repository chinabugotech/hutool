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

package cn.hutool.v7.http.client;

import cn.hutool.v7.http.HttpGlobalConfig;
import cn.hutool.v7.http.HttpUtil;
import cn.hutool.v7.http.client.engine.okhttp.OkHttpEngine;
import cn.hutool.v7.http.meta.HeaderName;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class IssueI8WU4MTest {

	@Test
	@Disabled
	void timeoutTest() {
		//设置超时，单位毫秒，这里1毫秒，按道理100%超时
		HttpGlobalConfig.setTimeout(1);

		final String body = HttpUtil.createGet("https://restapi.amap.com/v3/ip?Key=ad054b1810672fb0ff6107cd71518837")
			.header(HeaderName.CONTENT_TYPE, "application/x-www-form-urlencoded; charset=utf-8")
			.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36 Edg/120.0.0.0")
			.send(new OkHttpEngine())
			.body().
			getString();
		System.out.println(body);
	}
}
