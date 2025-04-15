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

package cn.hutool.v7.extra.template;

import cn.hutool.v7.core.map.Dict;
import cn.hutool.v7.core.text.StrUtil;
import cn.hutool.v7.extra.template.engine.TemplateEngine;
import cn.hutool.v7.extra.template.engine.TemplateEngineFactory;
import cn.hutool.v7.extra.template.engine.jetbrick.JetbrickEngine;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JetbrickTest {

	@Test
	public void jetbrickEngineTest() {
		//classpath模板
		final TemplateConfig config = new TemplateConfig("templates", TemplateConfig.ResourceMode.CLASSPATH)
				.setCustomEngine(JetbrickEngine.class);
		final TemplateEngine engine = TemplateEngineFactory.createEngine(config);
		final Template template = engine.getTemplate("jetbrick_test.jetx");
		final String result = template.render(Dict.of().set("name", "hutool"));
		Assertions.assertEquals("你好,hutool", StrUtil.trim(result));
	}

	@Test
	public void jetbrickEngineWithStringTest() {
		// 字符串模板
		final TemplateConfig config = new TemplateConfig("templates", TemplateConfig.ResourceMode.STRING)
				.setCustomEngine(JetbrickEngine.class);
		final TemplateEngine engine = TemplateEngineFactory.createEngine(config);
		final Template template = engine.getTemplate("hello,${name}");
		final String result = template.render(Dict.of().set("name", "hutool"));
		Assertions.assertEquals("hello,hutool", StrUtil.trim(result));
	}
}
