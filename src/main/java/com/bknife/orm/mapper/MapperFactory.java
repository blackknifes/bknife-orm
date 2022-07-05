package com.bknife.orm.mapper;

import javax.sql.DataSource;

public interface MapperFactory {
    public <T> Mapper<T> createMapperByType(Class<T> clazz, DataSource dataSource) throws Exception;

    public <T> MapperService<T> createServiceByType(Class<T> clazz, DataSource dataSource) throws Exception;
}
