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

import lombok.Data;
import cn.hutool.v7.json.JSONObject;
import cn.hutool.v7.json.JSONUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

/**
 * https://github.com/chinabugotech/hutool/issues/1399<br>
 * Throwable的默认序列化策略
 */
public class Issue1399Test {
	@Test
	void sqlExceptionTest() {
		final JSONObject set = JSONUtil.ofObj().putValue("error", new SQLException("test"));

		final String jsonStr = set.toString();
		Assertions.assertEquals("{\"error\":\"java.sql.SQLException: test\"}", jsonStr);

		final ErrorBean bean = set.toBean(ErrorBean.class);
		Assertions.assertNotNull(bean);
		Assertions.assertNotNull(bean.getError());
		Assertions.assertEquals(SQLException.class, bean.getError().getClass());
		Assertions.assertEquals("test", bean.getError().getMessage());
	}

	@Data
	private static class ErrorBean {
		private SQLException error;
	}
}
