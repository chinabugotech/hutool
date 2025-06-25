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

package cn.hutool.v7.json.issues;

import cn.hutool.v7.json.JSON;
import cn.hutool.v7.json.JSONUtil;
import cn.hutool.v7.json.serializer.JSONSerializer;
import cn.hutool.v7.json.serializer.TypeAdapterManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IssueIB9MH0Test {
	@Test
	void parseTest() {
		// 自定义序列化
		TypeAdapterManager.getInstance().register(TabTypeEnum.class, (JSONSerializer<TabTypeEnum>) (bean, context) ->
			context.getOrCreateObj().putValue("code", bean.getCode()).putValue("title", bean.getTitle()));

		final JSON parse = JSONUtil.parse(TabTypeEnum._01);
		Assertions.assertEquals("{\"code\":\"tab_people_home\",\"title\":\"首页\"}", parse.toString());
	}

	public enum TabTypeEnum  {
		_01("tab_people_home","首页"),
		_02("tab_people_hospital","医院");

		private final String code;
		private final String title;

		TabTypeEnum(final String code, final String title) {
			this.code = code;
			this.title = title;
		}

		public String getCode() {
			return code;
		}

		public String getTitle() {
			return title;
		}
	}
}
