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

import cn.hutool.v7.core.lang.Console;
import cn.hutool.v7.db.ds.DSUtil;
import cn.hutool.v7.db.ds.DSWrapper;
import cn.hutool.v7.db.meta.MetaUtil;
import cn.hutool.v7.db.meta.Table;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class IssueI9BANETest {
	@Test
	@Disabled
	void metaTest() {
		final DSWrapper ds = DSUtil.getDS("orcl");
		final Table tableMeta = MetaUtil.getTableMeta(ds, null, null, "\"1234\"");
		Console.log("remarks: " + tableMeta.getRemarks());
		Console.log("pks: " + tableMeta.getPkNames());
		Console.log("columns: " + tableMeta.getColumns());
		Console.log("index: " + tableMeta.getIndexInfoList());
	}
}
