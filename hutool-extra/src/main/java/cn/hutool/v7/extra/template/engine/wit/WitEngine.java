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

import cn.hutool.v7.core.io.file.FileUtil;
import cn.hutool.v7.core.lang.Assert;
import cn.hutool.v7.core.map.Dict;
import cn.hutool.v7.extra.template.Template;
import cn.hutool.v7.extra.template.TemplateConfig;
import cn.hutool.v7.extra.template.engine.TemplateEngine;
import cn.hutool.v7.extra.template.TemplateException;
import org.febit.wit.Engine;
import org.febit.wit.exceptions.ResourceNotFoundException;
import org.febit.wit.util.Props;

import java.io.File;

/**
 * Wit(http://zqq90.github.io/webit-script/)模板引擎封装
 *
 * @author Looly
 */
public class WitEngine implements TemplateEngine {

	private Engine engine;

	// --------------------------------------------------------------------------------- Constructor start
	/**
	 * 默认构造
	 */
	public WitEngine() {
		// SPI方式加载时检查库是否引入
		Assert.notNull(Engine.class);
	}

	/**
	 * 构造
	 *
	 * @param config 模板配置
	 */
	public WitEngine(final TemplateConfig config) {
		init(config);
	}

	/**
	 * 构造
	 *
	 * @param engine {@link Engine}
	 */
	public WitEngine(final Engine engine) {
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
	private void init(final Engine engine){
		this.engine = engine;
	}

	@Override
	public Template getTemplate(final String resource) {
		if(null == this.engine){
			init(TemplateConfig.DEFAULT);
		}
		try {
			return WitTemplate.wrap(engine.getTemplate(resource));
		} catch (final ResourceNotFoundException e) {
			throw new TemplateException(e);
		}
	}

	/**
	 * 获取原始引擎的钩子方法，用于自定义特殊属性，如插件等
	 *
	 * @return {@link Engine}
	 * @since 5.8.7
	 */
	@Override
	public Engine getRaw() {
		return this.engine;
	}

	/**
	 * 创建引擎
	 *
	 * @param config 模板配置
	 * @return {@link Engine}
	 */
	private static Engine createEngine(final TemplateConfig config) {
		final Props configProps = Engine.createConfigProps("");
		Dict dict = null;

		if (null != config) {
			dict = Dict.of();
			// 自定义编码
			dict.set("DEFAULT_ENCODING", config.getCharset());

			switch (config.getResourceMode()){
				case CLASSPATH:
					configProps.set("pathLoader.root", config.getPath());
					configProps.set("routeLoader.defaultLoader", "classpathLoader");
					break;
				case STRING:
					configProps.set("routeLoader.defaultLoader", "stringLoader");
					break;
				case FILE:
					configProps.set("pathLoader.root", config.getPath());
					configProps.set("routeLoader.defaultLoader", "fileLoader");
					break;
				case WEB_ROOT:
					final File root = FileUtil.file(FileUtil.getWebRoot(), config.getPath());
					configProps.set("pathLoader.root", FileUtil.getAbsolutePath(root));
					configProps.set("routeLoader.defaultLoader", "fileLoader");
					break;
			}
		}

		return Engine.create(configProps,dict);
	}
}
