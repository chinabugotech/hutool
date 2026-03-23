package cn.hutool.db.dialect;

/**
 * 常用数据库驱动池
 *
 * @author looly
 * @since 5.6.3
 */
public interface DriverNamePool {

	/**
	 * JDBC 驱动 MySQL
	 */
	String DRIVER_MYSQL = "com.mysql.jdbc.Driver";
	/**
	 * JDBC 驱动 Oceanbase
	 */
	String DRIVER_OCEANBASE = "com.oceanbase.jdbc.Driver";
	/**
	 * JDBC 驱动 MySQL，在6.X版本中变动驱动类名，且使用SPI机制
	 */
	String DRIVER_MYSQL_V6 = "com.mysql.cj.jdbc.Driver";
	/**
	 * JDBC 驱动 MariaDB
	 */
	String DRIVER_MARIADB = "org.mariadb.jdbc.Driver";
	/**
	 * JDBC 驱动 Oracle
	 */
	String DRIVER_ORACLE = "oracle.jdbc.OracleDriver";
	/**
	 * JDBC 驱动 Oracle，旧版使用
	 */
	String DRIVER_ORACLE_OLD = "oracle.jdbc.driver.OracleDriver";
	/**
	 * JDBC 驱动 PostgreSQL
	 */
	String DRIVER_POSTGRESQL = "org.postgresql.Driver";
	/**
	 * JDBC 驱动 SQLLite3
	 */
	String DRIVER_SQLLITE3 = "org.sqlite.JDBC";
	/**
	 * JDBC 驱动 SQLServer
	 */
	String DRIVER_SQLSERVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	/**
	 * JDBC 驱动 Hive
	 */
	String DRIVER_HIVE = "org.apache.hadoop.hive.jdbc.HiveDriver";
	/**
	 * JDBC 驱动 Hive2
	 */
	String DRIVER_HIVE2 = "org.apache.hive.jdbc.HiveDriver";
	/**
	 * JDBC 驱动 H2
	 */
	String DRIVER_H2 = "org.h2.Driver";
	/**
	 * JDBC 驱动 Derby
	 */
	String DRIVER_DERBY = "org.apache.derby.jdbc.AutoloadedDriver";
	/**
	 * JDBC 驱动 HSQLDB
	 */
	String DRIVER_HSQLDB = "org.hsqldb.jdbc.JDBCDriver";
	/**
	 * JDBC 驱动 达梦7
	 */
	String DRIVER_DM7 = "dm.jdbc.driver.DmDriver";
	/**
	 * JDBC 驱动 人大金仓
	 */
	String DRIVER_KINGBASE8 = "com.kingbase8.Driver";
	/**
	 * JDBC 驱动 Ignite thin
	 */
	String DRIVER_IGNITE_THIN = "org.apache.ignite.IgniteJdbcThinDriver";
	/**
	 * JDBC 驱动 ClickHouse
	 */
	String DRIVER_CLICK_HOUSE = "com.clickhouse.jdbc.ClickHouseDriver";
	/**
	 * JDBC 驱动 瀚高数据库
	 */
	String DRIVER_HIGHGO = "com.highgo.jdbc.Driver";
	/**
	 * JDBC 驱动 DB2
	 */
	String DRIVER_DB2 = "com.ibm.db2.jdbc.app.DB2Driver";
	/**
	 * JDBC 驱动 虚谷数据库
	 */
	String DRIVER_XUGU = "com.xugu.cloudjdbc.Driver";
	/**
	 * JDBC 驱动 Apache Phoenix
	 */
	String DRIVER_PHOENIX = "org.apache.phoenix.jdbc.PhoenixDriver";
	/**
	 * JDBC 驱动 华为高斯
	 */
	String DRIVER_GAUSS = "com.huawei.gauss.jdbc.ZenithDriver";
	/**
	 * JDBC 驱动 南大通用 GBase 8a
	 */
	String DRIVER_GBASE = "com.gbase.jdbc.Driver";
	/**
	 * JDBC 驱动 南大通用 GBase 8s<br>
	 * 见：https://www.gbase.cn/community/post/4029
	 */
	String DRIVER_GBASE8S = "com.gbasedbt.jdbc.Driver";
	/**
	 * JDBC 驱动 南大通用 GBase 8c<br>
	 * 见：https://www.gbase.cn/download/gbase-8c?category=DRIVER_PACKAGE 页面 GBase8c_JDBC.zip 中的《JDBC 使用手册_V1.0_20230818.pdf》p14
	 */
	String DRIVER_GBASE8C = "cn.gbase8c.Driver";
	/**
	 * JDBC 驱动 腾讯 TDSQL PostgreSQL 版本<br>
	 * 见：https://cloud.tencent.com/document/product/1129/116487
	 */
	String DRIVER_TDSQL_POSTGRESQL = "com.tencentcloud.tdsql.pg.jdbc.Driver";
	/**
	 * JDBC 驱动 腾讯 TDSQL-H LibraDB<br>
	 * 见：https://cloud.tencent.com/document/product/1488/79810
	 */
	String DRIVER_TDSQL_H_LIBRADB = "ru.yandex.clickhouse.ClickHouseDriver";
	/**
	 * JDBC 驱动 Snowflake<br>
	 * 见：https://docs.snowflake.cn/zh/developer-guide/jdbc/jdbc-configure#label-jdbc-connection-string
	 */
	String DRIVER_SNOWFLAKE = "net.snowflake.client.jdbc.SnowflakeDriver";
	/**
	 * JDBC 驱动 Teradata<br>
	 * 见：https://teradata-docs.s3.amazonaws.com/doc/connectivity/jdbc/reference/current/frameset.html 页面 JDBC Interfaces A-L 部分
	 */
	String DRIVER_TERADATA = "com.teradata.jdbc.TeraDriver";
	/**
	 * JDBC 驱动 神州数据库
	 */
	String DRIVER_OSCAR = "com.oscar.Driver";
	/**
	 * JDBC 驱动 Sybase
	 */
	String DRIVER_SYBASE = "com.sybase.jdbc4.jdbc.SybDriver";
	/**
	 * JDBC 驱动 OpenGauss
	 */
	String DRIVER_OPENGAUSS = "org.opengauss.Driver";
	/**
	 * JDBC 驱动 GoldenDB
	 */
	String DRIVER_GOLDENDB = "com.goldendb.jdbc.Driver";
	/**
	 * JDBC 驱动 Sap Hana
	 */
	String DRIVER_HANA = "com.sap.db.jdbc.Driver";
}
