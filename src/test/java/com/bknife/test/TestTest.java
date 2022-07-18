package com.bknife.test;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.junit.Test;

import com.bknife.base.json.Jsons;
import com.bknife.orm.PageResult;
import com.bknife.orm.annotion.Column;
import com.bknife.orm.annotion.Column.Type;
import com.bknife.orm.annotion.ForeignKey;
import com.bknife.orm.annotion.Join;
import com.bknife.orm.annotion.Table;
import com.bknife.orm.annotion.View;
import com.bknife.orm.mapper.MapperFactory;
import com.bknife.orm.mapper.MapperFactoryImpl;
import com.bknife.orm.mapper.TableMapper;
import com.bknife.orm.mapper.ViewMapper;
import com.bknife.orm.mapper.where.Condition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

public class TestTest {

    private static final String URL = "jdbc:mysql://localhost:3306/test?useSSL=false&characterEncoding=utf-8";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    private static class TestDataSource implements DataSource {
        public TestDataSource() throws ClassNotFoundException {
            Class.forName(DRIVER);
        }

        @Override
        public PrintWriter getLogWriter() throws SQLException {
            return DriverManager.getLogWriter();
        }

        @Override
        public void setLogWriter(PrintWriter out) throws SQLException {
            DriverManager.setLogWriter(out);
        }

        @Override
        public void setLoginTimeout(int seconds) throws SQLException {
            DriverManager.setLoginTimeout(seconds);
        }

        @Override
        public int getLoginTimeout() throws SQLException {
            return DriverManager.getLoginTimeout();
        }

        @Override
        public Logger getParentLogger() throws SQLFeatureNotSupportedException {
            return null;
        }

        @Override
        public <T> T unwrap(Class<T> iface) throws SQLException {
            return null;
        }

        @Override
        public boolean isWrapperFor(Class<?> iface) throws SQLException {
            return false;
        }

        @Override
        public Connection getConnection() throws SQLException {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        }

        @Override
        public Connection getConnection(String username, String password) throws SQLException {
            return DriverManager.getConnection(URL, username, password);
        }
    }

    @Data
    @Accessors(chain = true, fluent = true)
    @ToString
    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor
    @Table(name = "test", comment = "测试注释")
    private static class TestDbo {
        @Column(name = "name", type = Type.STRING, length = 128, primaryKey = true, comment = "测试名字")
        private String name;
        @Column(name = "age", type = Type.INTEGER, comment = "测试年龄")
        private int age;
    }

    @Data
    @Accessors(chain = true, fluent = true)
    @ToString()
    @NoArgsConstructor
    @AllArgsConstructor
    @Table(name = "test_ext", comment = "测试注释")
    @ForeignKey(tableClass = TestDbo.class, sources = "name", targets = "name")
    private static class TestExtDbo {
        @Column(name = "name", type = Type.STRING, length = 128, primaryKey = true, comment = "测试名字")
        private String name;
        @Column(name = "sex", type = Type.INTEGER, comment = "测试性别")
        private int sex;
    }

    @Data
    @Accessors(chain = true, fluent = true)
    @ToString(callSuper = true)
    @EqualsAndHashCode(callSuper = true)
    @View
    @Join(tableClass = TestExtDbo.class, sources = "name", targets = "name")
    private static class TestView extends TestDbo {
        @Column(tableClass = TestExtDbo.class, name = "sex")
        private int sex;
    }

    @Test
    public void testTable() throws Exception {
        DataSource dataSource = new TestDataSource();
        MapperFactory factory = new MapperFactoryImpl(true);
        TableMapper<TestDbo> TestDbo = factory.createTableMapper(TestDbo.class, dataSource);
        TableMapper<TestExtDbo> TestExtDbo = factory.createTableMapper(TestExtDbo.class, dataSource);
        ViewMapper<TestView> viewDbo = factory.createViewMapper(TestView.class, dataSource);

        {
            List<TestDbo> dbos = new ArrayList<>();
            List<TestExtDbo> exts = new ArrayList<>();
            for (int i = 0; i < 50; i++) {
                dbos.add(new TestDbo().name("test_" + i).age(i));
                exts.add(new TestExtDbo().name("test_" + i).sex(i % 2));
            }
            TestDbo.insert(dbos);
            TestExtDbo.insert(exts);
        }
        PageResult<TestView> views = viewDbo.page(2, new Condition().addGreaterEqual("age", 10));
        System.out.println(Jsons.toString(views));

        TestDbo.delete(new Condition().addRightLike("name", "test_"));

        Connection connection = dataSource.getConnection();
        connection.prepareStatement("DROP TABLE test_ext;").execute();
        connection.prepareStatement("DROP TABLE test;").execute();
    }
}
