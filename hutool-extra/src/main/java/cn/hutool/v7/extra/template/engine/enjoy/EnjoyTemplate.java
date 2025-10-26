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

package cn.hutool.v7.extra.template.engine.enjoy;

import cn.hutool.v7.extra.template.Template;

import java.io.OutputStream;
import java.io.Serial;
import java.io.Serializable;
import java.io.Writer;
import java.util.Map;

/**
 * Engoy模板实现
 *
 * @author Looly
 * @since 4.1.9
 */
public class EnjoyTemplate implements Template, Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 包装Enjoy模板
	 *
	 * @param EnjoyTemplate Enjoy的模板对象 {@link com.jfinal.template.Template}
	 * @return {@code EnjoyTemplate}
	 */
	public static EnjoyTemplate wrap(final com.jfinal.template.Template EnjoyTemplate) {
		return (null == EnjoyTemplate) ? null : new EnjoyTemplate(EnjoyTemplate);
	}

	private final com.jfinal.template.Template rawTemplate;

	/**
	 * 构造
	 *
	 * @param EnjoyTemplate Enjoy的模板对象 {@link com.jfinal.template.Template}
	 */
	public EnjoyTemplate(final com.jfinal.template.Template EnjoyTemplate) {
		this.rawTemplate = EnjoyTemplate;
	}

	@Override
	public void render(final Map<?, ?> bindingMap, final Writer writer) {
		rawTemplate.render(bindingMap, writer);
	}

	@Override
	public void render(final Map<?, ?> bindingMap, final OutputStream out) {
		rawTemplate.render(bindingMap, out);
	}

}
