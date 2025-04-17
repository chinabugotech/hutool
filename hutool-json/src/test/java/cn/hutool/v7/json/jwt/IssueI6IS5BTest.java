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

package cn.hutool.v7.json.jwt;

import lombok.Data;
import cn.hutool.v7.core.date.DateUtil;
import cn.hutool.v7.core.date.TimeUtil;
import cn.hutool.v7.core.date.format.DateFormatManager;
import cn.hutool.v7.json.JSONConfig;
import cn.hutool.v7.json.JSONObject;
import cn.hutool.v7.json.JSONUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * https://gitee.com/chinabugotech/hutool/issues/I6IS5B
 */
public class IssueI6IS5BTest {

	@Test
	public void payloadToBeanTest() {
		final LocalDateTime iat = TimeUtil.of(DateUtil.parse("2023-03-03"));
		final JwtToken jwtToken = new JwtToken();
		jwtToken.setIat(iat);

		final JSONObject payloadsData = JSONUtil.parseObj(jwtToken, JSONConfig.of().setDateFormat(DateFormatManager.FORMAT_SECONDS));

		final String token = JWTUtil.createToken(payloadsData, "123".getBytes(StandardCharsets.UTF_8));
		Assertions.assertEquals("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2Nzc3NzI4MDB9.SXU_mm1wT5lNoK-Dq5Y8f3BItv_44zuAlyeWLqajpXg", token);
		final JSONObject payloads = JWTUtil.parseToken(token).getPayloads();
		Assertions.assertEquals("{\"iat\":1677772800}", payloads.toString());
		final JwtToken o = payloads.toBean(JwtToken.class);
		Assertions.assertEquals(iat, o.getIat());
	}

	@Data
	static class JwtToken {
		private LocalDateTime iat;
	}

	@Test
	public void payloadToBeanTest2() {
		final Date iat = DateUtil.parse("2023-03-03");
		final JwtToken2 jwtToken = new JwtToken2();
		jwtToken.setIat(iat);

		final JSONObject payloadsData = JSONUtil.parseObj(jwtToken, JSONConfig.of().setDateFormat(DateFormatManager.FORMAT_SECONDS));

		final String token = JWTUtil.createToken(payloadsData, "123".getBytes(StandardCharsets.UTF_8));
		Assertions.assertEquals("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2Nzc3NzI4MDB9.SXU_mm1wT5lNoK-Dq5Y8f3BItv_44zuAlyeWLqajpXg", token);
		final JSONObject payloads = JWTUtil.parseToken(token).getPayloads();
		Assertions.assertEquals("{\"iat\":1677772800}", payloads.toString());
		final JwtToken2 o = payloads.toBean(JwtToken2.class);
		Assertions.assertEquals(iat, o.getIat());
	}

	@Data
	static class JwtToken2 {
		private Date iat;
	}
}
