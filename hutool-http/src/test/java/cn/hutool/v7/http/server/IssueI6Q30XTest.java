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

package cn.hutool.v7.http.server;

import cn.hutool.v7.core.date.DateUtil;
import cn.hutool.v7.core.date.StopWatch;
import cn.hutool.v7.core.lang.Console;
import cn.hutool.v7.http.HttpUtil;
import cn.hutool.v7.http.multipart.MultipartFormData;
import cn.hutool.v7.http.server.engine.sun.SimpleServer;
import cn.hutool.v7.http.server.engine.sun.SunServerRequest;

import java.util.concurrent.TimeUnit;

/**
 * 测试上传超时情况<br>
 * https://gitee.com/chinabugotech/hutool/issues/I6Q30X<br>
 *
 * post http://localhost:8888/file
 * form-data: file: file-data
 */
public class IssueI6Q30XTest {
	public static void main(final String[] args) {
		final SimpleServer server = HttpUtil.createServer(8888);
		server.addAction("/file", (request, response) -> {
			Console.log(((SunServerRequest)request).getHeaders().entrySet());

			final StopWatch stopWatch = DateUtil.createStopWatch();
			stopWatch.start();
			// graalvm编译后可能变慢
			final MultipartFormData multipart = request.getMultipart();
			stopWatch.stop();
			Console.log(stopWatch.prettyPrint(TimeUnit.MILLISECONDS));

			multipart.getFile("file").write("d:/test/uploadFile");

			response.write("OK");
		});
		server.start();
	}
}
