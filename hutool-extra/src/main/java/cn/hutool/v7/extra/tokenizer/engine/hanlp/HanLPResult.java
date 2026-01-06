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

package cn.hutool.v7.extra.tokenizer.engine.hanlp;

import cn.hutool.v7.extra.tokenizer.Result;
import cn.hutool.v7.extra.tokenizer.Word;
import com.hankcs.hanlp.seg.common.Term;

import java.util.Iterator;
import java.util.List;

/**
 * HanLP分词结果实现<br>
 * 项目地址：<a href="https://github.com/hankcs/HanLP">HanLP</a>
 *
 * @author Looly
 *
 */
public class HanLPResult implements Result {

	Iterator<Term> result;

	/**
	 * 构造
	 *
	 * @param termList 分词结果列表
	 */
	public HanLPResult(final List<Term> termList) {
		this.result = termList.iterator();
	}

	@Override
	public boolean hasNext() {
		return result.hasNext();
	}

	@Override
	public Word next() {
		return new HanLPWord(result.next());
	}

	@Override
	public void remove() {
		result.remove();
	}
}
