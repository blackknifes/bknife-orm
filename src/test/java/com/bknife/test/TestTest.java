package com.bknife.test;

import org.junit.Test;

import com.bknife.orm.annotion.Column;
import com.bknife.orm.annotion.Column.Type;
import com.bknife.orm.annotion.Table;

import lombok.Data;

public class TestTest {

    @Data
    @Table(name = "test", comment = "测试注释")
    private static class TestDbo
    {
        @Column(name = "name", type = Type.STRING, length = 128, primaryKey = true, comment = "测试名字")
        private String name;
    }

    @Test
    public void test() throws Exception {
        
    }
}
