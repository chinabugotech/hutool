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

package cn.hutool.v7.http;

import cn.hutool.v7.core.lang.Console;
import cn.hutool.v7.core.thread.ThreadUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class HttpsTest {

	/**
	 * 测试单例的SSLSocketFactory是否有线程安全问题
	 */
	@Test
	@Disabled
	public void getTest() {
		final AtomicInteger count = new AtomicInteger();
		for(int i =0; i < 100; i++){
			ThreadUtil.execute(()->{
				final String s = HttpUtil.get("https://www.baidu.com/");
				Console.log(count.incrementAndGet());
			});
		}
		ThreadUtil.sync(this);
	}
}
