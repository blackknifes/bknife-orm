package com.bknife.test;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.bknife.base.util.Jsons;
import com.bknife.orm.PageResult;
import com.bknife.orm.annotion.Column;
import com.bknife.orm.annotion.ForeignKey;
import com.bknife.orm.annotion.Join;
import com.bknife.orm.annotion.Table;
import com.bknife.orm.annotion.View;
import com.bknife.orm.mapper.MapperFactory;
import com.bknife.orm.mapper.TableMapper;
import com.bknife.orm.mapper.ViewMapper;
import com.bknife.orm.mapper.where.Condition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

public class TestOrm {

    private static final String URL = "jdbc:mysql://localhost:3306/test?useSSL=false&characterEncoding=utf-8";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    @Data
    @Accessors(chain = true, fluent = true)
    @ToString
    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor
    @Table(comment = "测试注释")
    private static class TestDbo {
        @Column(primaryKey = true, comment = "测试名字")
        private String name;
        @Column(comment = "测试年龄")
        private int age;
    }

    @Data
    @Accessors(chain = true, fluent = true)
    @ToString()
    @NoArgsConstructor
    @AllArgsConstructor
    @Table(comment = "测试注释")
    @ForeignKey(tableClass = TestDbo.class, sources = "name", targets = "name")
    private static class TestExtDbo {
        @Column(primaryKey = true, comment = "测试名字")
        private String name;
        @Column(comment = "测试性别")
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
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        dataSource.setDriverClassName(DRIVER);
        MapperFactory factory = MapperFactory.getDefaultFactory();
        factory.setVerbose(true);
        TableMapper<TestDbo> TestDbo = factory.getTableMapper(TestDbo.class, dataSource);
        TableMapper<TestExtDbo> TestExtDbo = factory.getTableMapper(TestExtDbo.class, dataSource);
        ViewMapper<TestView> viewDbo = factory.getViewMapper(TestView.class, dataSource);

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

        Collection<TestView> list = viewDbo.list(new Condition().addIn("name", "test_1", "test_2", "test_3"));
        System.out.println(Jsons.toString(list));

        TestDbo.delete(new Condition().addRightLike("name", "test_"));

        Connection connection = dataSource.getConnection();
        connection.prepareStatement("DROP TABLE test_ext_dbo;").execute();
        connection.prepareStatement("DROP TABLE test_dbo;").execute();
    }
}
