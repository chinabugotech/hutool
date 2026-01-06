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

package cn.hutool.v7.json.jwt;

import cn.hutool.v7.core.codec.binary.Base64;
import cn.hutool.v7.core.text.StrUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

public class Issue4105Test {
	@Test
	void verifyNoneTest() {
		// {"alg": "none"}.{"exp": 1642196407}
		// 当定义alg为none时，校验总是成功
		final String head = Base64.encode("{\"alg\": \"none\"}");
		final String payload = Base64.encode("{\"exp\": 1642196407}");
		final String token = StrUtil.format("{}.{}.", head, payload);

		final JWT jwt = JWTUtil.parseToken(token);
		Assertions.assertNull(jwt.getSigner());
		// 对于签名为none的JWT，verify()方法总是返回true
		Assertions.assertTrue(jwt.verify());

		// 对于签名为none的JWT，但是定义了key，不一致报错
		final JWT jwt2 = JWTUtil.parseToken(token);
		Assertions.assertThrows(JWTException.class, ()-> jwt2.setKey("123".getBytes(StandardCharsets.UTF_8)).verify());
	}

	@Test
	void verifyEmptyTest() {
		// {"alg": "none"}.{"exp": 1642196407}
		// 当定义alg为none时，校验总是成功
		final String head = Base64.encode("{\"alg\": \"\"}");
		final String payload = Base64.encode("{\"exp\": 1642196407}");
		final String token = StrUtil.format("{}.{}.", head, payload);

		final JWT jwt = JWTUtil.parseToken(token);
		Assertions.assertNull(jwt.getSigner());
		// 对于签名为none的JWT，verify()方法总是返回true
		Assertions.assertTrue(jwt.verify());

		// 对于签名为none的JWT，但是定义了key，不一致报错
		final JWT jwt2 = JWTUtil.parseToken(token);
		Assertions.assertThrows(JWTException.class, ()-> jwt2.setKey("123".getBytes(StandardCharsets.UTF_8)).verify());
	}

	@Test
	void verifyHs256Test() {
		// {"alg": "none"}.{"exp": 1642196407}
		// 当定义alg为none时，校验总是成功
		final String head = Base64.encode("{\"alg\": \"HS256\"}");
		final String payload = Base64.encode("{\"exp\": 1642196407}");
		final String token = StrUtil.format("{}.{}.", head, payload);

		final JWT jwt = JWTUtil.parseToken(token);
		Assertions.assertNull(jwt.getSigner());

		// 未定义签名器或key，但是JWT中要求了签名算法，异常
		Assertions.assertThrows(JWTException.class, jwt::verify);

		// 手动定义签名器，但是签名部分为空或不一致，返回false
		final JWT jwt2 = JWTUtil.parseToken(token);
		Assertions.assertFalse(jwt2.setKey("123".getBytes(StandardCharsets.UTF_8)).verify());
	}
}
