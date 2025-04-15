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

package cn.hutool.v7.extra.template.engine.wit;

import cn.hutool.v7.core.convert.ConvertUtil;
import cn.hutool.v7.core.reflect.TypeReference;
import org.febit.wit.Template;

import java.io.OutputStream;
import java.io.Serializable;
import java.io.Writer;
import java.util.Map;

/**
 * Wit模板实现
 *
 * @author Looly
 */
public class WitTemplate implements cn.hutool.v7.extra.template.Template, Serializable{
	private static final long serialVersionUID = 1L;

	private final Template rawTemplate;

	/**
	 * 包装Wit模板
	 *
	 * @param witTemplate Wit的模板对象 {@link Template}
	 * @return WitTemplate
	 */
	public static WitTemplate wrap(final Template witTemplate) {
		return (null == witTemplate) ? null : new WitTemplate(witTemplate);
	}

	/**
	 * 构造
	 *
	 * @param witTemplate Wit的模板对象 {@link Template}
	 */
	public WitTemplate(final Template witTemplate) {
		this.rawTemplate = witTemplate;
	}

	@Override
	public void render(final Map<?, ?> bindingMap, final Writer writer) {
		final Map<String, Object> map = ConvertUtil.convert(new TypeReference<Map<String, Object>>() {}, bindingMap);
		rawTemplate.merge(map, writer);
	}

	@Override
	public void render(final Map<?, ?> bindingMap, final OutputStream out) {
		final Map<String, Object> map = ConvertUtil.convert(new TypeReference<Map<String, Object>>() {}, bindingMap);
		rawTemplate.merge(map, out);
	}

}
