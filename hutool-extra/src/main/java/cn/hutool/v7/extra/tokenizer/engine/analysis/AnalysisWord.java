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

package cn.hutool.v7.extra.tokenizer.engine.analysis;

import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.util.Attribute;

import cn.hutool.v7.extra.tokenizer.Word;

import java.io.Serial;

/**
 * Lucene-analysis分词中的一个单词包装
 *
 * @author Looly
 *
 */
public class AnalysisWord implements Word {
	@Serial
	private static final long serialVersionUID = 1L;

	private final Attribute word;

	/**
	 * 构造
	 *
	 * @param word {@link CharTermAttribute}
	 */
	public AnalysisWord(final CharTermAttribute word) {
		this.word = word;
	}

	@Override
	public String getText() {
		return word.toString();
	}

	@Override
	public int getStartOffset() {
		if(this.word instanceof OffsetAttribute) {
			return ((OffsetAttribute)this.word).startOffset();
		}
		return -1;
	}

	@Override
	public int getEndOffset() {
		if(this.word instanceof OffsetAttribute) {
			return ((OffsetAttribute)this.word).endOffset();
		}
		return -1;
	}

	@Override
	public String toString() {
		return getText();
	}
}
