package com.bknife.orm.mapper;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.bknife.orm.assemble.SqlConstants;
import com.bknife.orm.assemble.SqlContext;
import com.bknife.orm.assemble.SqlMapperInfo;
import com.bknife.orm.assemble.SqlTableInfo;
import com.bknife.orm.assemble.SqlViewInfo;

public class MapperFactoryImpl implements MapperFactory, SqlConstants {
    private static MapperFactoryImpl factory = new MapperFactoryImpl();

    public static MapperFactoryImpl getFactory() {
        return factory;
    }

    private SqlContext context = new SqlContext();
    private Map<Class<?>, Map<DataSource, TableMapperProxy<?>>> tableMappers = new HashMap<>();
    private Map<Class<?>, Map<DataSource, ViewMapper<?>>> viewMappers = new HashMap<>();

    private Map<DataSource, String> dataSourceDriverType = new HashMap<>();

    public MapperFactoryImpl() {

    }

    public MapperFactoryImpl(boolean verbose) {
        context.setVerbose(verbose);
    }

    @Override
    public SqlContext getContext() {
        return context;
    }

    @Override
    public void setVerbose(boolean verbose) {
        context.setVerbose(verbose);
    }

    @SuppressWarnings("unchecked")
    @Override
    public synchronized <T> TableMapper<T> getTableMapper(Class<T> clazz, DataSource dataSource)
            throws Exception {
        Map<DataSource, TableMapperProxy<?>> tableMapperMap = tableMappers.get(clazz);
        if (tableMapperMap == null) {
            tableMapperMap = new HashMap<>();
            tableMappers.put(clazz, tableMapperMap);
        } else {
            TableMapperProxy<T> tableMapper = (TableMapperProxy<T>) tableMapperMap.get(dataSource);
            if (tableMapper != null)
                return tableMapper;
        }

        SqlMapperInfo<T> mapperInfo = context.getMapperInfo(clazz);
        if (!(mapperInfo instanceof SqlTableInfo))
            throw new IllegalArgumentException("class [" + clazz + "] has not Table annotation");
        TableMapperProxy<T> tableMapper = TableMapperProxy.create(context.getAssemble(getDBType(dataSource)),
                dataSource,
                (SqlTableInfo<T>) mapperInfo);
        tableMapperMap.put(dataSource, tableMapper);
        return tableMapper;
    }

    @SuppressWarnings("unchecked")
    @Override
    public synchronized <T> ViewMapper<T> getViewMapper(Class<T> clazz, DataSource dataSource) throws Exception {
        Map<DataSource, ViewMapper<?>> viewMapperMap = viewMappers.get(clazz);
        if (viewMapperMap == null) {
            viewMapperMap = new HashMap<>();
            viewMappers.put(clazz, viewMapperMap);
        } else {
            ViewMapper<T> viewMapper = (ViewMapper<T>) viewMapperMap.get(dataSource);
            if (viewMapper != null)
                return viewMapper;
        }

        SqlMapperInfo<T> mapperInfo = context.getMapperInfo(clazz);
        if (!(mapperInfo instanceof SqlViewInfo))
            throw new IllegalArgumentException("class [" + clazz + "] is not a view class");
        ViewMapper<T> viewMapper = ViewMapper.create(context.getAssemble(getDBType(dataSource)), dataSource,
                (SqlViewInfo<T>) mapperInfo);
        viewMapperMap.put(dataSource, viewMapper);
        return viewMapper;
    }

    private String getDBType(DataSource dataSource) {
        String dbType = dataSourceDriverType.get(dataSource);
        if (dbType != null)
            return dbType;
        try (Connection connection = dataSource.getConnection()) {
            dbType = connection.getMetaData().getDatabaseProductName().toLowerCase();
            dataSourceDriverType.put(dataSource, dbType);
        } catch (Exception e) {
            dbType = "unknown";
            dataSourceDriverType.put(dataSource, dbType);
        }
        return dbType;
    }
}
