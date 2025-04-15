/*
 * Copyright (c) 2025 Hutool Team and hutool.cn
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

import cn.hutool.v7.json.jwt.signers.JWTSigner;
import cn.hutool.v7.json.jwt.signers.JWTSignerUtil;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Issue3732Test {
	@Test
	void hmacTest() {
		final JWTSigner SIGNER = JWTSignerUtil.hs256("6sf2f5j2a62a3s8f9032hsf".getBytes());
		final Map<String, Object> headers = new HashMap<>();
		headers.put("typ", "JWT");

		final Map<String, Object> payload = new HashMap<>();
		payload.put("name", "test");
		payload.put("role", "admin");

		// 创建 JWT token
		final String token = JWTUtil.createToken(headers, payload, SIGNER);
		assertEquals("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiYWRtaW4iLCJuYW1lIjoidGVzdCJ9.pD3Xz41rtXvU3G1c_yS7ir01FXmDvtjjAOU2HYd8MdA", token);
	}
}
