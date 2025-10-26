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

package cn.hutool.v7.extra.template.engine.beetl;

import cn.hutool.v7.core.io.IORuntimeException;
import cn.hutool.v7.core.lang.Assert;
import cn.hutool.v7.extra.template.Template;
import cn.hutool.v7.extra.template.TemplateConfig;
import cn.hutool.v7.extra.template.engine.TemplateEngine;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.ResourceLoader;
import org.beetl.core.resource.ClasspathResourceLoader;
import org.beetl.core.resource.CompositeResourceLoader;
import org.beetl.core.resource.FileResourceLoader;
import org.beetl.core.resource.StringTemplateResourceLoader;
import org.beetl.core.resource.WebAppResourceLoader;

import java.io.IOException;

/**
 * Beetl模板引擎封装
 *
 * @author Looly
 */
public class BeetlEngine implements TemplateEngine {

	private GroupTemplate engine;

	// --------------------------------------------------------------------------------- Constructor start
	/**
	 * 默认构造
	 */
	public BeetlEngine() {
		// SPI方式加载时检查库是否引入
		Assert.notNull(GroupTemplate.class);
	}

	/**
	 * 构造
	 *
	 * @param config 模板配置
	 */
	public BeetlEngine(final TemplateConfig config) {
		init(config);
	}

	/**
	 * 构造
	 *
	 * @param engine {@link GroupTemplate}
	 */
	public BeetlEngine(final GroupTemplate engine) {
		init(engine);
	}
	// --------------------------------------------------------------------------------- Constructor end


	@Override
	public TemplateEngine init(final TemplateConfig config) {
		init(createEngine(config));
		return this;
	}

	/**
	 * 初始化引擎
	 * @param engine 引擎
	 */
	private void init(final GroupTemplate engine){
		this.engine = engine;
	}

	@Override
	public Template getTemplate(final String resource) {
		if(null == this.engine){
			init(TemplateConfig.DEFAULT);
		}
		return BeetlTemplate.wrap(engine.getTemplate(resource));
	}

	/**
	 * 获取原始引擎的钩子方法，用于自定义特殊属性，如插件等
	 *
	 * @return {@link GroupTemplate}
	 * @since 5.8.7
	 */
	@Override
	public GroupTemplate getRaw() {
		return this.engine;
	}

	/**
	 * 创建引擎
	 *
	 * @param config 模板配置
	 * @return {@link GroupTemplate}
	 */
	private static GroupTemplate createEngine(TemplateConfig config) {
		if (null == config) {
			config = TemplateConfig.DEFAULT;
		}

		return switch (config.getResourceMode()) {
			case CLASSPATH ->
				createGroupTemplate(new ClasspathResourceLoader(config.getPath(), config.getCharsetStr()));
			case FILE -> createGroupTemplate(new FileResourceLoader(config.getPath(), config.getCharsetStr()));
			case WEB_ROOT -> createGroupTemplate(new WebAppResourceLoader(config.getPath(), config.getCharsetStr()));
			case STRING -> createGroupTemplate(new StringTemplateResourceLoader());
			case COMPOSITE ->
				//TODO 需要定义复合资源加载器
				createGroupTemplate(new CompositeResourceLoader());
		};
	}

	/**
	 * 创建自定义的模板组 {@link GroupTemplate}，配置文件使用全局默认<br>
	 * 此时自定义的配置文件可在ClassPath中放入beetl.properties配置
	 *
	 * @param loader {@link ResourceLoader}，资源加载器
	 * @return {@link GroupTemplate}
	 * @since 3.2.0
	 */
	private static GroupTemplate createGroupTemplate(final ResourceLoader<?> loader) {
		try {
			return createGroupTemplate(loader, Configuration.defaultConfiguration());
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 创建自定义的 {@link GroupTemplate}
	 *
	 * @param loader {@link ResourceLoader}，资源加载器
	 * @param conf {@link Configuration} 配置文件
	 * @return {@link GroupTemplate}
	 */
	private static GroupTemplate createGroupTemplate(final ResourceLoader<?> loader, final Configuration conf) {
		return new GroupTemplate(loader, conf);
	}
}
