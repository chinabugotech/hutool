package cn.hutool.v7.db.meta.ddl;

import cn.hutool.v7.core.text.StrUtil;
import cn.hutool.v7.db.meta.*;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 根据Table对象生成MySQL的CREATE TABLE语句
 *
 * @author Looly
 */
public class MysqlTableGenerator {

	/**
	 * 根据Table对象生成MySQL的CREATE TABLE语句
	 *
	 * @param table Table对象
	 * @return MySQL的CREATE TABLE语句
	 */
	public static String generateCreateTableSql(Table table) {
		StringBuilder sqlBuilder = new StringBuilder();

		// 表名
		final String tableName = table.getTableName();
		if (StrUtil.isBlank(tableName)) {
			throw new IllegalArgumentException("Table name cannot be blank!");
		}

		sqlBuilder.append("CREATE TABLE `").append(tableName).append("` (\n");

		// 列定义
		Collection<Column> columns = table.getColumns();
		if (columns.isEmpty()) {
			throw new IllegalArgumentException("Table must have at least one column");
		}

		boolean firstColumn = true;
		for (Column column : columns) {
			if (!firstColumn) {
				sqlBuilder.append(",\n");
			}
			sqlBuilder.append("  ").append(generateColumnDefinition(column));
			firstColumn = false;
		}

		// 主键定义
		Set<String> pkNames = table.getPkNames();
		if (!pkNames.isEmpty()) {
			sqlBuilder.append(",\n  PRIMARY KEY (");
			boolean firstPk = true;
			for (String pkName : pkNames) {
				if (!firstPk) {
					sqlBuilder.append(", ");
				}
				sqlBuilder.append("`").append(pkName).append("`");
				firstPk = false;
			}
			sqlBuilder.append(")");
		}

		// 索引定义 (如果有)
		List<IndexInfo> indexInfoList = table.getIndexInfoList();
		if (indexInfoList != null && !indexInfoList.isEmpty()) {
			for (IndexInfo indexInfo : indexInfoList) {
				sqlBuilder.append(",\n  ");
				if (!indexInfo.isNonUnique()) {
					sqlBuilder.append("UNIQUE ");
				}
				sqlBuilder.append("INDEX `").append(indexInfo.getIndexName()).append("` (");

				// 遍历索引列信息列表
				List<ColumnIndex> columnIndexInfoList = indexInfo.getColumnIndexInfoList();
				if (columnIndexInfoList != null && !columnIndexInfoList.isEmpty()) {
					boolean firstIndexColumn = true;
					for (ColumnIndex columnIndex : columnIndexInfoList) {
						if (!firstIndexColumn) {
							sqlBuilder.append(", ");
						}
						sqlBuilder.append("`").append(columnIndex.getColumnName()).append("`");
						// 可以添加排序信息，如果需要的话
						String ascOrDesc = columnIndex.getAscOrDesc();
						if ("D".equalsIgnoreCase(ascOrDesc)) {
							sqlBuilder.append(" DESC");
						} else if ("A".equalsIgnoreCase(ascOrDesc)) {
							sqlBuilder.append(" ASC");
						}
						firstIndexColumn = false;
					}
				}
				sqlBuilder.append(")");
			}
		}

		sqlBuilder.append("\n)");

		// 表注释
		String remarks = table.getRemarks();
		if (remarks != null && !remarks.isEmpty()) {
			sqlBuilder.append(" COMMENT='").append(remarks.replace("'", "''")).append("'");
		}

		sqlBuilder.append(";");

		return sqlBuilder.toString();
	}

	/**
	 * 根据Column对象生成列定义
	 * <p>
	 * 注意: 这里是一个简化的实现，实际项目中需要根据Column类的具体属性
	 * 和数据库类型映射来完善
	 *
	 * @param column Column对象
	 * @return 列定义字符串
	 */
	private static String generateColumnDefinition(Column column) {
		StringBuilder columnBuilder = new StringBuilder();

		// 列名
		columnBuilder.append("`").append(column.getName()).append("`");

		// 数据类型 (这里假设Column有getTypeName方法)
		// 实际应用中需要更复杂的类型映射
		ColumnType type = column.getType();
		if (type != null) {
			columnBuilder.append(" ").append(type.getTypeName());
		} else {
			// 默认使用VARCHAR
			columnBuilder.append(" VARCHAR(255)");
		}

		// 是否允许为空 (假设Column有isNullable方法)
		if (!column.isNullable()) {
			columnBuilder.append(" NOT NULL");
		}

		// 默认值 (假设Column有getDefaultValue方法)
		String defaultValue = column.getColumnDef();
		if (defaultValue != null) {
			columnBuilder.append(" DEFAULT '").append(defaultValue.replace("'", "''")).append("'");
		}

		// 注释 (假设Column有getRemarks方法)
		String remarks = column.getRemarks();
		if (remarks != null && !remarks.isEmpty()) {
			columnBuilder.append(" COMMENT '").append(remarks.replace("'", "''")).append("'");
		}

		return columnBuilder.toString();
	}
}
