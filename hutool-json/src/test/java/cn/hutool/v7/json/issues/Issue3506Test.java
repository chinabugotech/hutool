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
import cn.hutool.v7.json.JSONFactory;
import cn.hutool.v7.json.JSONObject;
import cn.hutool.v7.json.serializer.impl.ClassTypeAdapter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Issue3506Test {

	@Test
	void toBeanTest() {
		final JSONFactory factory = JSONFactory.of(null, null);
		factory.register(Class.class, ClassTypeAdapter.INSTANCE);

		final Languages languages = new Languages();
		languages.setLanguageType(Java.class);

		final String hutoolJSONString = factory.parseObj(languages).toString();
		Assertions.assertEquals("{\"languageType\":\"cn.hutool.v7.json.issues.Issue3506Test$Java\"}", hutoolJSONString);

		final JSONObject jsonObject = factory.parseObj(hutoolJSONString);
		final Languages bean = jsonObject.toBean(Languages.class);

		Assertions.assertNotNull(bean);
		Assertions.assertEquals(bean.getLanguageType(), Java.class);
	}

	@Data
	static class Languages {
		private Class<? extends Language> languageType;
	}

	interface Language {
	}

	public static class Java implements Language {
	}
}
