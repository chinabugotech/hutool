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

package cn.hutool.v7.json.issues;

import lombok.AllArgsConstructor;
import lombok.Data;
import cn.hutool.v7.json.JSONConfig;
import cn.hutool.v7.json.JSONUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IssueI50EGGTest {

	@Test
	public void toBeanTest(){
		final String data = "{\"return_code\": 1, \"return_msg\": \"成功\", \"return_data\" : null}";
		final ApiResult<?> apiResult = JSONUtil.toBean(data, JSONConfig.of().setIgnoreCase(true), ApiResult.class);
		Assertions.assertEquals(1, apiResult.getReturn_code());
	}

	@Data
	@AllArgsConstructor
	static class ApiResult<T>{
		private long Return_code;
		private String Return_msg;
		private T Return_data;
	}
}
