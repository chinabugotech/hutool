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

package cn.hutool.v7.core.text.replacer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

/**
 * @author cmm
 */
public class HighMultiReplacerV2Test {
	@Test
	public void test1(){
		final HashMap<String, String> replaceMap = new HashMap<>();
		replaceMap.put("sha","SHA");
		replaceMap.put("asa","ASA");
		replaceMap.put("ha","HA");
		replaceMap.put("hex","HEX");
		final HighMultiReplacerV2 replacer = new HighMultiReplacerV2(replaceMap);
		final String text = "asdasahhxxeshaaahexaaasa";
		final CharSequence apply = replacer.apply(text);
//		replaceMap.forEach((k,v) -> {
//			System.out.println(k + ":" + v);
//		});
		Assertions.assertEquals("asdASAhhxxeSHAaaHEXaaASA", apply.toString());
	}


	@Test
	public void test2(){
		final HashMap<String, String> replaceMap = new HashMap<>();
		replaceMap.put("沙漠","什么");
		replaceMap.put("撒","厦");
		replaceMap.put("哈","蛤");
		replaceMap.put("海克斯","害可是");
		final HighMultiReplacerV2 replacer = new HighMultiReplacerV2(replaceMap);
		final String text = "撒哈拉大沙漠，你看哈哈哈。hex码中文写成海克斯科技，海克，沙子收拾收拾，撤退，撒下了句点";
		final CharSequence apply = replacer.apply(text);
//		replaceMap.forEach((k,v) -> {
//			System.out.println(k + ":" + v);
//		});
		Assertions.assertEquals("厦蛤拉大什么，你看蛤蛤蛤。hex码中文写成害可是科技，海克，沙子收拾收拾，撤退，厦下了句点", apply.toString());
	}
}
