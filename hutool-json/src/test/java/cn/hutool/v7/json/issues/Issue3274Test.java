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

package cn.hutool.v7.json.issues;

import lombok.Data;
import lombok.Getter;
import cn.hutool.v7.json.JSONArray;
import cn.hutool.v7.json.JSONConfig;
import cn.hutool.v7.json.JSONObject;
import cn.hutool.v7.json.JSONUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Issue3274Test {
	@Test
	public void toBeanTest() {
		final JSONObject entries = JSONUtil.parseObj("""
			{
			   \s
			    "age": 36,
			    "gender": "",
			    "id": "123123123"
			}""", JSONConfig.of().setIgnoreError(true));
		final LarkCoreHrPersonal larkCoreHrPersonal = entries.toBean(LarkCoreHrPersonal.class);
		Assertions.assertNotNull(larkCoreHrPersonal);
	}

	@Data
	static class LarkCoreHrPersonal {
		private String id;
		private String age = "";
		private Gender gender;
	}

	@Getter
	enum Gender {
		male("male", "Male", "男"),
		female("female", "Female", "女"),
		other("other", "Other", "其他");
		private final JSONArray display;
		private final String enum_name;

		Gender(final String enum_name, final String en_Us, final String zh_CN) {
			this.enum_name = enum_name;
			this.display = JSONUtil.parseArray("[{\"lang\": \"en-US\",\"value\": \"" + en_Us + "\"},{\"lang\": \"zh-CN\",\"value\": \"" + zh_CN + "\"}]");
		}
	}
}
