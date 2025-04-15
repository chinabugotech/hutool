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
import cn.hutool.v7.json.JSONConfig;
import cn.hutool.v7.json.JSONObject;
import cn.hutool.v7.json.JSONUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Issue3051Test {

	@Test
	public void parseTest() {
		// 空Bean按照Bean对待，转为空对象
		// 逻辑见：BeanTypeAdapter
		final JSONObject jsonObject = JSONUtil.parseObj(new EmptyBean(),
			JSONConfig.of().setIgnoreError(true));

		Assertions.assertEquals("{}", jsonObject.toString());
	}

	@Test
	public void parseTest2() {
		final JSONObject jsonObject = JSONUtil.parseObj(new EmptyBean());
		Assertions.assertEquals("{}", jsonObject.toString());
	}

	@Data
	static class EmptyBean {

	}
}
