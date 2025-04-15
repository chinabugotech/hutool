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

package cn.hutool.v7.extra.tokenizer.engine.ansj;

import org.ansj.splitWord.Analysis;
import org.ansj.splitWord.analysis.ToAnalysis;

import cn.hutool.v7.core.text.StrUtil;
import cn.hutool.v7.extra.tokenizer.Result;
import cn.hutool.v7.extra.tokenizer.engine.TokenizerEngine;

/**
 * Ansj分词引擎实现<br>
 * 项目地址：https://github.com/NLPchina/ansj_seg
 *
 * @author Looly
 *
 */
public class AnsjEngine implements TokenizerEngine {

	private final Analysis analysis;

	/**
	 * 构造
	 */
	public AnsjEngine() {
		this(new ToAnalysis());
	}

	/**
	 * 构造
	 *
	 * @param analysis {@link Analysis}
	 */
	public AnsjEngine(final Analysis analysis) {
		this.analysis = analysis;
	}

	@Override
	public Result parse(final CharSequence text) {
		return new AnsjResult(analysis.parseStr(StrUtil.toStringOrEmpty(text)));
	}

}
