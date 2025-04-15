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

package cn.hutool.v7.db;

import cn.hutool.v7.core.collection.ListUtil;
import cn.hutool.v7.core.lang.Console;
import cn.hutool.v7.core.thread.ThreadUtil;
import cn.hutool.v7.db.handler.EntityListHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * SqlRunner线程安全测试
 *
 * @author Looly
 *
 */
@Disabled
public class ConcurentTest {

	private Db db;

	@BeforeEach
	public void init() {
		db = Db.of("test");
	}

	@Test
	public void findTest() {
		for(int i = 0; i < 10000; i++) {
			ThreadUtil.execute(() -> {
				final List<Entity> find;
				find = db.find(ListUtil.of("name AS name2"), Entity.of("user"), EntityListHandler.of());
				Console.log(find);
			});
		}

		//主线程关闭会导致连接池销毁，sleep避免此情况引起的问题
		ThreadUtil.sleep(5000);
	}
}
