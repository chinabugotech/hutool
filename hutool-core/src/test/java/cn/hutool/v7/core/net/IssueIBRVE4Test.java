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

package cn.hutool.v7.core.net;

import cn.hutool.v7.core.net.url.UrlQueryUtil;
import cn.hutool.v7.core.util.CharsetUtil;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IssueIBRVE4Test {
	@Test
	public void decodeParamMapNoParamTest() {
		// 参数值不存在分界标记等号时
		// 无参数值时
		final Map<String, String> paramMap = UrlQueryUtil.decodeQuery("https://hutool.cn/api.action", CharsetUtil.UTF_8);
	assertEquals(0,paramMap.size());
}
@Test
	public void decodeParamMapListNoParamTest() {
		// 参数值不存在分界标记等号时
		// 无参数值时
		final Map<String, String> paramMap1 = UrlQueryUtil.decodeQuery("https://hutool.cn/api.action", CharsetUtil.UTF_8);
		assertEquals(0,paramMap1.size());
	}
}
