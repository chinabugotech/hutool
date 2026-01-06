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

package cn.hutool.v7.http.useragent;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * https://android-developers.googleblog.com/2024/12/user-agent-reduction-on-android-webview.html
 */
public class Issue3813Test {
	@Test
	void parseTest() {
		final String ua = "Mozilla/5.0 (Linux; Android 10; K; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/125.000 Mobile Safari/537.36";
		final UserAgent parse = UserAgentUtil.parse(ua);
		Assertions.assertEquals("UserAgent{mobile=true, browser=Chrome, version='125.000', platform=Android, os=Android, osVersion='10', engine=Webkit, engineVersion='537.36'}", parse.toString());
	}
}
