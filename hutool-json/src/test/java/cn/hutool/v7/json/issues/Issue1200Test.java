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
import cn.hutool.v7.core.io.resource.ResourceUtil;
import cn.hutool.v7.json.JSONConfig;
import cn.hutool.v7.json.JSONObject;
import cn.hutool.v7.json.JSONUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * 转换失败，则在设置setIgnoreError(true)不报错
 */
public class Issue1200Test {

	@Test
	public void toBeanTest() {
		final JSONObject jsonObject = JSONUtil.parseObj(
			ResourceUtil.readUtf8Str("issue1200.json"),
			JSONConfig.of().setIgnoreError(true));

		final ResultBean resultBean = jsonObject.toBean(ResultBean.class);
		// json对象转ItemsBean，但是字段对应不上，保留空对象
		Assertions.assertNull(resultBean.getItems().get(0).get(0).get(0).getDetail());

		// 字符串转对象，失败，为null
		Assertions.assertNull(resultBean.getItems().get(0).get(0).get(1));
		Assertions.assertNull(resultBean.getItems().get(0).get(0).get(2));
	}

	@Data
	static
	class ResultBean {
		public List<List<List<ResultBean.ItemsBean>>> items;

		@Data
		public static class ItemsBean {
			public ResultBean.ItemsBean.DetailBean detail;

			@Data
			public static class DetailBean {
				public String visitorStatus;
			}
		}
	}
}
