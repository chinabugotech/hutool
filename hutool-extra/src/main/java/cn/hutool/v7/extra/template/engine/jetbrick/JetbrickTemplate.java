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

package cn.hutool.v7.extra.template.engine.jetbrick;

import cn.hutool.v7.core.convert.ConvertUtil;
import cn.hutool.v7.core.reflect.TypeReference;
import cn.hutool.v7.extra.template.Template;
import jetbrick.template.JetTemplate;

import java.io.OutputStream;
import java.io.Serial;
import java.io.Serializable;
import java.io.Writer;
import java.util.Map;

/**
 * Jetbrick模板实现<br>
 * 见：https://github.com/subchen/jetbrick-template-2x
 *
 * @author Looly
 * @since 5.7.21
 */
public class JetbrickTemplate implements Template, Serializable{
	@Serial
	private static final long serialVersionUID = 1L;

	private final JetTemplate rawTemplate;

	/**
	 * 包装Jetbrick模板
	 *
	 * @param jetTemplate Jetbrick的模板对象 {@link JetTemplate }
	 * @return JetbrickTemplate
	 */
	public static JetbrickTemplate wrap(final JetTemplate jetTemplate) {
		return (null == jetTemplate) ? null : new JetbrickTemplate(jetTemplate);
	}

	/**
	 * 构造
	 *
	 * @param jetTemplate Jetbrick的模板对象 {@link JetTemplate }
	 */
	public JetbrickTemplate(final JetTemplate jetTemplate) {
		this.rawTemplate = jetTemplate;
	}

	@Override
	public void render(final Map<?, ?> bindingMap, final Writer writer) {
		final Map<String, Object> map = ConvertUtil.convert(new TypeReference<>() {
		}, bindingMap);
		rawTemplate.render(map, writer);
	}

	@Override
	public void render(final Map<?, ?> bindingMap, final OutputStream out) {
		final Map<String, Object> map = ConvertUtil.convert(new TypeReference<>() {
		}, bindingMap);
		rawTemplate.render(map, out);
	}

}
