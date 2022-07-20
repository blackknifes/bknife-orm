package com.bknife.orm.mapper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import javax.sql.DataSource;

import com.bknife.orm.assemble.SqlTableInfo;

public class TableMapperProxyHandler implements InvocationHandler {
    private DataSource dataSource;
    private SqlTableInfo<?> tableInfo;

    public TableMapperProxyHandler(DataSource dataSource, SqlTableInfo<?> tableInfo) {
        this.dataSource = dataSource;
        this.tableInfo = tableInfo;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
    }

}
