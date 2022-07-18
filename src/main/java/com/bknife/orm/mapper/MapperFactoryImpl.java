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
    private SqlContext context = new SqlContext();
    private Map<Class<?>, Map<DataSource, TableMapper<?>>> tableMappers = new HashMap<>();
    private Map<Class<?>, Map<DataSource, ViewMapper<?>>> viewMappers = new HashMap<>();

    public MapperFactoryImpl() {

    }

    public MapperFactoryImpl(boolean verbose) {
        context.setVerbose(verbose);
    }

    @Override
    public SqlContext getContext() {
        return context;
    }

    @SuppressWarnings("unchecked")
    @Override
    public synchronized <T> TableMapper<T> createTableMapper(Class<T> clazz, DataSource dataSource) throws Exception {
        Map<DataSource, TableMapper<?>> tableMapperMap = tableMappers.get(clazz);
        if (tableMapperMap == null) {
            tableMapperMap = new HashMap<>();
            tableMappers.put(clazz, tableMapperMap);
        } else {
            TableMapper<T> tableMapper = (TableMapper<T>) tableMapperMap.get(dataSource);
            if (tableMapper != null)
                return tableMapper;
        }

        SqlMapperInfo<T> mapperInfo = context.getMapperInfo(clazz);
        if (!(mapperInfo instanceof SqlTableInfo))
            throw new IllegalArgumentException("class [" + clazz + "] has not Table annotation");
        TableMapper<T> tableMapper = TableMapper.create(context.getAssemble(getDBType(dataSource)), dataSource,
                (SqlTableInfo<T>) mapperInfo);
        tableMapperMap.put(dataSource, tableMapper);
        return tableMapper;
    }

    @SuppressWarnings("unchecked")
    @Override
    public synchronized <T> ViewMapper<T> createViewMapper(Class<T> clazz, DataSource dataSource) throws Exception {
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
        try (Connection connection = dataSource.getConnection()) {
            return connection.getMetaData().getDatabaseProductName().toLowerCase();
        } catch (Exception e) {
            return "unknown";
        }
    }
}
