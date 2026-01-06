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

package cn.hutool.v7.setting.toml;

import cn.hutool.v7.core.array.ArrayUtil;
import cn.hutool.v7.setting.props.Props;
import cn.hutool.v7.setting.props.PropsUtil;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Issue3008Test {
	/**
	 * 数组字段追加后生成新的数组，造成赋值丢失<br>
	 * 修复见：BeanUtil.setFieldValue
	 */
	@Test
	public void toBeanTest() {
		final Props props = PropsUtil.get("issue3008");
		final MyUser user = props.toBean(MyUser.class, "person");
		Assertions.assertEquals("[LOL, KFC, COFFE]", ArrayUtil.toString(user.getHobby()));
	}

	@Data
	static class MyUser {
		private String[] hobby;
	}
}
