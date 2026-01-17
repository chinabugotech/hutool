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

package cn.hutool.v7.json.issues;

import cn.hutool.v7.json.JSONUtil;
import lombok.Data;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Issue4214Test {

	@Test
	void toBeanTest(){
		final LicenseInfo licenseInfo = new LicenseInfo();
		licenseInfo.setAuthTypeEnum(AuthTypeEnum.OFFICIAL);

		final String jsonStr = JSONUtil.toJsonStr(licenseInfo);
		assertEquals("{\"authTypeEnum\":\"OFFICIAL\"}", jsonStr);

		// 这里反序列化会报错
		final LicenseInfo bean = JSONUtil.toBean(jsonStr, LicenseInfo.class);
		assertEquals(AuthTypeEnum.OFFICIAL, bean.getAuthTypeEnum());
	}

	@Data
	static class LicenseInfo{
		private AuthTypeEnum authTypeEnum;
	}

	enum AuthTypeEnum{
		OFFICIAL,
		SELF_BUILD
	}
}
