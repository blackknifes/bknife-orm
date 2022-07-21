package com.bknife.orm.mapper;

import javax.sql.DataSource;

import com.bknife.orm.assemble.SqlContext;

public interface MapperFactory {
    public static MapperFactory getDefaultFactory() {
        return MapperFactoryImpl.getFactory();
    }

    public SqlContext getContext();

    public void setVerbose(boolean verbose);

    public <T> TableMapper<T> getTableMapper(Class<T> clazz, DataSource dataSource) throws Exception;

    public <T> ViewMapper<T> getViewMapper(Class<T> clazz, DataSource dataSource) throws Exception;
}
