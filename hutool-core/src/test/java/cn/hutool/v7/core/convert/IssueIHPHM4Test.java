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

package cn.hutool.v7.core.convert;

import cn.hutool.v7.core.bean.BeanUtil;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Issue IHPHM4: BeanUtil不支持Record到Record的转换
 *
 * @author Hutool Contributors
 */
class IssueIHPHM4Test {

	// 定义测试用的Record类
	record SourceRecord(String name, int age) {
	}

	record TargetRecord(String name, int age) {
	}

	record SourceRecordWithExtra(String name, int age, String extra) {
	}

	record TargetRecordPartial(String name, int age) {
	}

	/**
	 * 测试Record到Record的单对象属性复制
	 */
	@Test
	void copyPropertiesRecordToRecordTest() {
		final SourceRecord source = new SourceRecord("张三", 25);
		final TargetRecord target = BeanUtil.copyProperties(source, TargetRecord.class);

		assertNotNull(target);
		assertEquals("张三", target.name());
		assertEquals(25, target.age());
	}

	/**
	 * 测试Record到Record的列表属性复制
	 */
	@Test
	void copyToListRecordToRecordTest() {
		final List<SourceRecord> sourceList = new ArrayList<>();
		sourceList.add(new SourceRecord("张三", 25));
		sourceList.add(new SourceRecord("李四", 30));

		final List<TargetRecord> targetList = BeanUtil.copyToList(sourceList, TargetRecord.class);

		assertNotNull(targetList);
		assertEquals(2, targetList.size());

		assertEquals("张三", targetList.get(0).name());
		assertEquals(25, targetList.get(0).age());

		assertEquals("李四", targetList.get(1).name());
		assertEquals(30, targetList.get(1).age());
	}

	/**
	 * 测试Record到Record的部分字段属性复制
	 */
	@Test
	void copyPropertiesRecordToRecordPartialTest() {
		final SourceRecordWithExtra source = new SourceRecordWithExtra("张三", 25, "额外字段");
		final TargetRecordPartial target = BeanUtil.copyProperties(source, TargetRecordPartial.class);

		assertNotNull(target);
		assertEquals("张三", target.name());
		assertEquals(25, target.age());
	}
}