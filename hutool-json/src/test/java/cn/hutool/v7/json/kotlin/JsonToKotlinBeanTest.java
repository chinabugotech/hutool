/*
 * Copyright (c) 2026 Hutool Team.
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

package cn.hutool.v7.json.kotlin;

import cn.hutool.v7.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonToKotlinBeanTest {
	@Test
	void toBeanTest() {
		final TestKBeanForJson bean = JSONUtil.parseObj("{\"id\":\"VampireAchao\", \"name\":\"阿超\", \"country\":\"中国\"}").toBean(TestKBeanForJson.class);
		assertEquals("VampireAchao", bean.getId());
		assertEquals("阿超", bean.getName());
		assertEquals("中国", bean.getCountry());
	}

	@Test
	void toBeanByFastJsonTest() {
		final TestKBeanForJson javaObject = JSON.parseObject("{\"id\":\"VampireAchao\", \"name\":\"阿超\", \"country\":\"中国\"}").toJavaObject(TestKBeanForJson.class);
		assertEquals("VampireAchao", javaObject.getId());
		assertEquals("阿超", javaObject.getName());
		assertEquals("中国", javaObject.getCountry());
	}
}
