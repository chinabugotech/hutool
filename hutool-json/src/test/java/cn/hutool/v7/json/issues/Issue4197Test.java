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

import cn.hutool.v7.json.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class Issue4197Test {

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	static
	class TestDTO {

		private BigDecimal h;
	}

	@Test
	void toBeanTest() {
		final TestDTO bean = JSONUtil.toBean("{\"h\":\"123，456，789\"}", TestDTO.class);
		Assertions.assertEquals(new BigDecimal("123456789"), bean.getH());
	}

}
