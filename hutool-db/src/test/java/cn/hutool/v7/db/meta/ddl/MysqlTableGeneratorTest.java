package cn.hutool.v7.db.meta.ddl;

import cn.hutool.v7.db.meta.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

public class MysqlTableGeneratorTest {

	@Test
	public void testGenerateCreateTableSql_WithIndexes() {
		Table table = new Table("products");

		// 列定义
		Column idCol = new Column().setName("id").setType(new ColumnType(-5, "BIGINT", 20));
		Column nameCol = new Column().setName("name").setType(new ColumnType(12, "VARCHAR", 100));
		Column priceCol = new Column().setName("price").setType(new ColumnType(3, "DECIMAL", 10));

		// 索引定义
		IndexInfo uniqueIndex = new IndexInfo(false, "idx_name", "products", null, null);
		ColumnIndex nameIdxCol = new ColumnIndex("name", "A");
		uniqueIndex.setColumnIndexInfoList(Collections.singletonList(nameIdxCol));

		IndexInfo normalIndex = new IndexInfo(true, "idx_price", "products", null, null);
		ColumnIndex priceIdxCol = new ColumnIndex("price", "D");
		normalIndex.setColumnIndexInfoList(Collections.singletonList(priceIdxCol));

		table.addColumn(idCol).addColumn(nameCol).addColumn(priceCol);
		table.setIndexInfoList(Arrays.asList(uniqueIndex, normalIndex));

		String sql = MysqlTableGenerator.generateCreateTableSql(table);
		Assertions.assertEquals("""
			CREATE TABLE `products` (
			  `id` BIGINT NOT NULL,
			  `name` VARCHAR NOT NULL,
			  `price` DECIMAL NOT NULL,
			  UNIQUE INDEX `idx_name` (`name` ASC),
			  INDEX `idx_price` (`price` DESC)
			);""", sql);
	}
}
