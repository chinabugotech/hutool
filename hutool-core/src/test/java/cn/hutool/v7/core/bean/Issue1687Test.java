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

package cn.hutool.v7.core.bean;

import cn.hutool.v7.core.annotation.Alias;
import cn.hutool.v7.core.bean.copier.CopyOptions;
import cn.hutool.v7.core.map.MapUtil;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.Serial;
import java.io.Serializable;

/**
 * https://github.com/chinabugotech/hutool/issues/1687
 */
public class Issue1687Test {

	@Test
	public void toBeanTest(){
		final SysUserFb sysUserFb = new SysUserFb();
		sysUserFb.setDepId("123");
		sysUserFb.setCustomerId("456");

		final SysUser sysUser = BeanUtil.toBean(sysUserFb, SysUser.class);
		// 别名错位导致找不到字段
		Assertions.assertNull(sysUser.getDepart());
		Assertions.assertEquals(Long.valueOf(456L), sysUser.getOrgId());
	}

	@Test
	public void toBeanTest2(){
		final SysUserFb sysUserFb = new SysUserFb();
		sysUserFb.setDepId("123");
		sysUserFb.setCustomerId("456");

		// 补救别名错位
		final CopyOptions copyOptions = CopyOptions.of().setFieldMapping(
				MapUtil.builder((Object)"depart", (Object)"depId").build()
		);
		final SysUser sysUser = BeanUtil.toBean(sysUserFb, SysUser.class, copyOptions);

		Assertions.assertEquals(Long.valueOf(123L), sysUser.getDepart());
		Assertions.assertEquals(Long.valueOf(456L), sysUser.getOrgId());
	}

	@Data
	static class SysUserFb implements Serializable {

		@Serial
		private static final long serialVersionUID = 1L;

		@Alias("depart")
		private String depId;

		@Alias("orgId")
		private String customerId;
	}

	@Data
	static class SysUser implements Serializable {

		@Serial
		private static final long serialVersionUID = 1L;

		@Alias("depId")
		private Long depart;

		private Long orgId;
	}
}
