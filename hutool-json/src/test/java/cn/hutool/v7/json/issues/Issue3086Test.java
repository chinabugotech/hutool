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
import cn.hutool.v7.core.collection.ListUtil;
import cn.hutool.v7.json.JSON;
import cn.hutool.v7.json.JSONUtil;
import cn.hutool.v7.json.serializer.JSONContext;
import cn.hutool.v7.json.serializer.JSONSerializer;
import cn.hutool.v7.json.serializer.TypeAdapterManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Issue3086Test {

	@Test
	public void serializeTest() {
		TypeAdapterManager.getInstance().register(TestBean.class, new TestBean());

		final List<SimpleGrantedAuthority> strings = ListUtil.of(
			new SimpleGrantedAuthority("ROLE_admin"),
			new SimpleGrantedAuthority("ROLE_normal")
		);
		final TestBean testBean = new TestBean();
		testBean.setAuthorities(strings);

		Assertions.assertEquals("{\"authorities\":[\"ROLE_admin\",\"ROLE_normal\"]}",
			JSONUtil.toJsonStr(testBean));
	}

	static class SimpleGrantedAuthority {
		private final String role;

		public SimpleGrantedAuthority(final String role) {
			this.role = role;
		}

		public String getAuthority() {
			return this.role;
		}

		public String toString() {
			return this.role;
		}
	}

	@Data
	static class TestBean implements JSONSerializer<TestBean> {
		private Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

		@Override
		public JSON serialize(final TestBean bean, final JSONContext context) {
			final List<String> strings = bean.getAuthorities()
				.stream().map(SimpleGrantedAuthority::getAuthority).collect(Collectors.toList());
			return context.getOrCreateObj().putValue("authorities",strings);
		}
	}
}
