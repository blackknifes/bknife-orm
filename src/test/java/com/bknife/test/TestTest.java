package com.bknife.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.junit.Assert;
import org.junit.Test;

import com.bknife.orm.annotion.Column;
import com.bknife.orm.annotion.Column.Type;
import com.bknife.orm.annotion.Table;
import com.bknife.orm.assemble.SqlAssemble;
import com.bknife.orm.assemble.SqlConstants;
import com.bknife.orm.assemble.SqlContext;
import com.bknife.orm.assemble.SqlFactoryImpl;
import com.bknife.orm.assemble.assembled.SqlAssembled;
import com.bknife.orm.assemble.assembled.SqlAssembledQuery;
import com.bknife.orm.mapper.Updater;
import com.bknife.orm.mapper.where.Condition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

public class TestTest {

    private static final String URL = "jdbc:mysql://localhost:3306/test?useSSL=false&characterEncoding=utf-8";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    private Connection getConnection() throws Exception {
        Class.forName(DRIVER);
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    @Data
    @Accessors(chain = true)
    @ToString()
    @NoArgsConstructor
    @AllArgsConstructor
    @Table(name = "test", comment = "测试注释")
    private static class TestDbo {
        @Column(name = "name", type = Type.STRING, length = 128, primaryKey = true, comment = "测试名字")
        private String name;
        @Column(name = "age", type = Type.INTEGER, comment = "测试年龄")
        private int age;
    }

    @Test
    public void testTable() throws Exception {
        java.util.Unsafe;

        SqlContext context = new SqlContext(new SqlFactoryImpl());
        SqlAssemble assemble = context.getAssemble(TestDbo.class, SqlConstants.MYSQL);

        Connection connection = getConnection();
        connection.setAutoCommit(false);

        try {
            {
                PreparedStatement ps = connection.prepareStatement(assemble.assembleCreateTable().getSql());
                System.out.println(ps.toString());
                ps.execute();
            }

            {
                SqlAssembled assembled = assemble.assembleInsert();
                PreparedStatement ps = connection.prepareStatement(assembled.getSql());
                for (int i = 0; i < 50; i++) {
                    TestDbo dbo = new TestDbo().setName(Integer.toString(i)).setAge(i);
                    assembled.setParameter(ps, dbo);
                    System.out.println(ps.toString());
                    Assert.assertTrue(ps.executeUpdate() > 0);
                }
            }

            {
                SqlAssembled assembled = assemble.assembleReplace();
                PreparedStatement ps = connection.prepareStatement(assembled.getSql());
                for (int i = 0; i < 50; i++) {
                    TestDbo dbo = new TestDbo().setName(Integer.toString(i)).setAge(50 - i);
                    assembled.setParameter(ps, dbo);
                    System.out.println(ps.toString());
                    Assert.assertTrue(ps.executeUpdate() > 0);
                }
            }

            {
                for (int i = 0; i < 50; i++) {
                    Updater updater = new Updater().addEqual("name", Integer.toString(i)).update("age", i);
                    SqlAssembled assembled = assemble.assembleUpdate(updater);
                    PreparedStatement ps = connection.prepareStatement(assembled.getSql());
                    assembled.setParameter(ps, null);
                    System.out.println(ps.toString());
                    Assert.assertTrue(ps.executeUpdate() > 0);
                }
            }

            {
                Condition condition = new Condition().addGreaterEqual("age", 10).addOrderAsc("age");
                condition.addLike("name", "0");
                SqlAssembled query = assemble.assembleCount(condition);
                PreparedStatement ps = connection.prepareStatement(query.getSql());
                query.setParameter(ps, null);
                System.out.println(query.toString());
                ResultSet resultSet = ps.executeQuery();
                Assert.assertTrue(resultSet.next());
                System.out.println(resultSet.getInt(1));
            }

            {
                Condition condition = new Condition().addGreaterEqual("age", 10).addOrderAsc("age").limit(1, 15);
                condition.addLike("name", "0");
                SqlAssembledQuery query = assemble.assembleSelect(condition);
                PreparedStatement ps = connection.prepareStatement(query.getSql());
                query.setParameter(ps, null);
                System.out.println(query.toString());
                ResultSet resultSet = ps.executeQuery();
                StringBuffer buffer = new StringBuffer();
                while (resultSet.next()) {
                    TestDbo dbo = (TestDbo) query.createFromResultSet(resultSet);
                    buffer.append(dbo).append("\n");
                }
                System.out.println(buffer.toString());
            }

            connection.prepareStatement("DROP TABLE test;").execute();
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
        }
    }
}
