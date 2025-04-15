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

package cn.hutool.v7.extra.tokenizer.engine;

import cn.hutool.v7.core.lang.Singleton;
import cn.hutool.v7.core.spi.ServiceLoader;
import cn.hutool.v7.core.spi.SpiUtil;
import cn.hutool.v7.core.text.StrUtil;
import cn.hutool.v7.extra.tokenizer.TokenizerException;
import cn.hutool.v7.log.LogUtil;

/**
 * 简单分词引擎工厂，用于根据用户引入的分词引擎jar，自动创建对应的引擎
 *
 * @author Looly
 */
public class TokenizerEngineFactory {

	/**
	 * 根据用户引入的模板引擎jar，自动创建对应的分词引擎对象<br>
	 * 获得的是单例的TokenizerEngine
	 *
	 * @return 单例的TokenizerEngine
	 */
	public static TokenizerEngine getEngine() {
		final TokenizerEngine engine = Singleton.get(TokenizerEngine.class.getName(), TokenizerEngineFactory::createEngine);
		LogUtil.debug("Use [{}] Tokenizer Engine As Default.", StrUtil.removeSuffix(engine.getClass().getSimpleName(), "Engine"));
		return engine;
	}

	/**
	 * 根据用户引入的分词引擎jar，自动创建对应的分词引擎对象
	 *
	 * @return {@link TokenizerEngine}
	 */
	public static TokenizerEngine createEngine() {
		return doCreateEngine();
	}

	/**
	 * 创建自定义引擎
	 *
	 * @param engineName 引擎名称，忽略大小写，如`Analysis`、`Ansj`、`HanLP`、`IKAnalyzer`、`Jcseg`、`Jieba`、`Mmseg`、`Mynlp`、`Word`
	 * @return 引擎
	 * @throws TokenizerException 无对应名称的引擎
	 */
	public static TokenizerEngine createEngine(String engineName) throws TokenizerException {
		if (!StrUtil.endWithIgnoreCase(engineName, "Engine")) {
			engineName = engineName + "Engine";
		}
		final ServiceLoader<TokenizerEngine> list = SpiUtil.loadList(TokenizerEngine.class);
		for (final String serviceName : list.getServiceNames()) {
			if (StrUtil.endWithIgnoreCase(serviceName, engineName)) {
				return list.getService(serviceName);
			}
		}
		throw new TokenizerException("No such engine named: " + engineName);
	}

	/**
	 * 根据用户引入的分词引擎jar，自动创建对应的分词引擎对象
	 *
	 * @return {@link TokenizerEngine}
	 */
	private static TokenizerEngine doCreateEngine() {
		final TokenizerEngine engine = SpiUtil.loadFirstAvailable(TokenizerEngine.class);
		if (null != engine) {
			return engine;
		}

		throw new TokenizerException("No tokenizer found !Please add some tokenizer jar to your project !");
	}
}
