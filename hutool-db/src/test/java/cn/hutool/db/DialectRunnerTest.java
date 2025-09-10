package cn.hutool.db;

import org.junit.jupiter.api.Test;
import cn.hutool.db.dialect.Dialect;
import cn.hutool.db.dialect.DialectFactory;
import static org.junit.jupiter.api.Assertions.*;

class DialectRunnerTest {
    /**
     * 基础测试：简单的 ORDER BY 语句
     */
    @Test
    public void removeOuterOrderByTest1() {

        Dialect dialect = DialectFactory.newDialect("com.mysql.jdbc.Driver");
        DialectRunner dialectRunner = new DialectRunner(dialect);

        // 测试基本的ORDER BY移除
        String sql = "SELECT * FROM users ORDER BY name";
        String result = dialectRunner.removeOuterOrderBy(sql);

        assertEquals("SELECT * FROM users", result);
    }

    /**
     * 多字段 ORDER BY 测试：包含多个排序字段的复杂 ORDER BY语句
     */
    @Test
    public void removeOuterOrderByTest2() {

        Dialect dialect = DialectFactory.newDialect("com.mysql.jdbc.Driver");
        DialectRunner dialectRunner = new DialectRunner(dialect);

        // 测试多字段ORDER BY移除
        String sql = "SELECT id, name, age FROM users WHERE status = 'active' ORDER BY name ASC, age DESC, created_date";
        String result = dialectRunner.removeOuterOrderBy(sql);

        assertEquals("SELECT id, name, age FROM users WHERE status = 'active'", result);
    }
}
