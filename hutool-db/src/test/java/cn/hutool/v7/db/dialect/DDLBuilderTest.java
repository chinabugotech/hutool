/*
 * Copyright (c) 2025 Hutool Team and hutool.cn
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

package cn.hutool.v7.db.dialect;

import cn.hutool.v7.db.meta.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

public class DDLBuilderTest {

	@Test
	public void testGenerateCreateTableSqlWithIndexes() {
		TableMeta tableMeta = new TableMeta("products");

		// 列定义
		Column idCol = new Column().setName("id").setType(new ColumnType(-5, "BIGINT", 20)).setPk(true);
		Column nameCol = new Column().setName("name").setType(new ColumnType(12, "VARCHAR", 100));
		Column priceCol = new Column().setName("price").setType(new ColumnType(3, "DECIMAL", 10));

		// 索引定义
		IndexInfo uniqueIndex = new IndexInfo(false, "idx_name", "products", null, null);
		ColumnIndex nameIdxCol = new ColumnIndex("name", "A");
		uniqueIndex.setColumnIndexInfoList(Collections.singletonList(nameIdxCol));

		IndexInfo normalIndex = new IndexInfo(true, "idx_price", "products", null, null);
		ColumnIndex priceIdxCol = new ColumnIndex("price", "D");
		normalIndex.setColumnIndexInfoList(Collections.singletonList(priceIdxCol));

		tableMeta.addColumn(idCol).addColumn(nameCol).addColumn(priceCol);
		tableMeta.setIndexInfoList(Arrays.asList(uniqueIndex, normalIndex));

		final DDLBuilder ddlBuilder = new DDLBuilder();
		String sql = ddlBuilder.buildCreateTableSql(tableMeta);
		Assertions.assertEquals("""
			CREATE TABLE products (
			  `id` BIGINT NOT NULL,
			  `name` VARCHAR NOT NULL,
			  `price` DECIMAL NOT NULL,
			  PRIMARY KEY (id),
			  UNIQUE INDEX `idx_name` (`name` ASC),
			  INDEX `idx_price` (`price` DESC)
			);""", sql);
	}
}
