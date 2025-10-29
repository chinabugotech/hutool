package cn.hutool.v7.db.dialect;

import cn.hutool.v7.core.collection.CollUtil;
import cn.hutool.v7.core.lang.Assert;
import cn.hutool.v7.core.text.StrJoiner;
import cn.hutool.v7.core.text.StrUtil;
import cn.hutool.v7.db.meta.*;
import cn.hutool.v7.db.sql.QuoteWrapper;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class DDLBuilder {

	private QuoteWrapper quoteWrapper;

	public String buildCreateTableSql(TableMeta tableMeta) {
		StringBuilder sqlBuilder = new StringBuilder();

		// 表名
		final String tableName = tableMeta.getTableName();
		Assert.notBlank(tableName, "Table name cannot be blank!");

		buildCreateTableStart(sqlBuilder, tableName);
		sqlBuilder.append(StrUtil.LF);

		// 列定义
		Collection<Column> columns = tableMeta.getColumns();
		Assert.notEmpty(columns, "Table must have at least one column");
		final String columnsDefinition = StrJoiner.of(",\n")
			.append(columns, column -> "  " + buildColumnDefinition(column)).toString();
		sqlBuilder.append(columnsDefinition);

		// 主键约束定义
		buildPrimaryKey(sqlBuilder, tableMeta.getPkNames());

		// 索引定义 (如果有)
		List<IndexInfo> indexInfoList = tableMeta.getIndexInfoList();
		if (CollUtil.isNotEmpty(indexInfoList)) {
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

		buildCreateTableEnd(sqlBuilder);

		// 表注释
		String remarks = tableMeta.getRemarks();
		if (remarks != null && !remarks.isEmpty()) {
			sqlBuilder.append(" COMMENT='").append(remarks.replace("'", "''")).append("'");
		}

		sqlBuilder.append(";");

		return sqlBuilder.toString();
	}

	/**
	 * 通用方法：建表开头（可被子类重写，如Oracle可能有特殊语法），生成如：<br>
	 * <pre>{@code
	 *     CREATE TABLE <tableName> (
	 * }</pre>
	 *
	 * @param tableName 表名
	 */
	protected void buildCreateTableStart(StringBuilder sqlBuilder,String tableName) {
		if (StrUtil.isBlank(tableName)) {
			throw new IllegalArgumentException("Table name cannot be blank!");
		}
		sqlBuilder.append("CREATE TABLE ").append(wrap(tableName)).append(" (");
	}

	protected void buildPrimaryKey(StringBuilder sqlBuilder, Set<String> pkNames) {
		if (pkNames.isEmpty()) {
			return;
		}

		sqlBuilder.append(",\n  PRIMARY KEY (");
		boolean firstPk = true;
		for (String pkName : pkNames) {
			if (!firstPk) {
				sqlBuilder.append(", ");
			}
			sqlBuilder.append(wrap(pkName));
			firstPk = false;
		}
		sqlBuilder.append(")");
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
	private static String buildColumnDefinition(Column column) {
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

	/**
	 * 通用方法：建表结尾（可被子类重写，如MySQL可能有特殊语法），生成如：<br>
	 * <pre>{@code
	 *     )
	 * }</pre>
	 */
	protected void buildCreateTableEnd(StringBuilder sqlBuilder) {
		sqlBuilder.append("\n)");
	}

	/**
	 * 字段名包装
	 *
	 * @param field 字段名
	 * @return 包装后的字段名
	 */
	private String wrap(String field) {
		if (null != quoteWrapper) {
			return quoteWrapper.wrap(field);
		}
		return field;
	}
}
