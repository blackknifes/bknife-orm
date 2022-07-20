package com.bknife.orm.mapper;

import javax.sql.DataSource;

import com.bknife.orm.assemble.SqlContext;

public interface MapperFactory {
    public SqlContext getContext();

    public <T> TableMapperProxy<T> createTableMapper(Class<T> clazz, DataSource dataSource) throws Exception;

    public <T> ViewMapper<T> createViewMapper(Class<T> clazz, DataSource dataSource) throws Exception;
}
