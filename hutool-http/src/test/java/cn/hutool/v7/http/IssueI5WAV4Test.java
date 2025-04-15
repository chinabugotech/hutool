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

import cn.hutool.v7.http.client.Request;
import cn.hutool.v7.json.JSONUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class IssueI5WAV4Test {

	@Test
	@Disabled
	public void getTest(){
		//测试代码
		final Map<String, Object> map = new HashMap<>();
		map.put("taskID", 370);
		map.put("flightID", 2879);


		@SuppressWarnings("resource")
		final String body = Request.of("http://localhost:8884/api/test/testHttpUtilGetWithBody").body(JSONUtil.toJsonStr(map)).send().bodyStr();
		System.out.println("使用hutool返回结果:" + body);
	}
}
