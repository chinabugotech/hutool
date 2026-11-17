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

package cn.hutool.v7.core.bean;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class Issue4364Test {
	private static Locale originalLocale;

	@BeforeAll
	public static void initTurkishLocale() {
		originalLocale = Locale.getDefault();
		Locale.setDefault(new Locale("tr", "TR"));
	}

	@AfterAll
	public static void restoreLocale() {
		Locale.setDefault(originalLocale);
	}

	@Test
	public void isPrefixedGetterSetterFoundUnderTurkishLocaleTest() {
		final BeanDesc beanDesc = new StrictBeanDesc(SampleBean.class);
		final PropDesc activeProp = beanDesc.getProp("active");
		assertNotNull(activeProp);
		assertNotNull(activeProp.getGetter(), "isActIve getter should be found under Turkish locale");
		assertNotNull(activeProp.getSetter(), "setActIve setter should be found under Turkish locale");
		assertEquals(Boolean.TRUE, activeProp.getValue(new SampleBean(), false));
	}

	public static class SampleBean {
		public boolean active;

		public boolean isActIve() {
			return true;
		}

		public void setActIve(final boolean active) {
			this.active = active;
		}
	}
}
