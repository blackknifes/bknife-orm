package com.bknife.orm.mapper;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.bknife.orm.annotion.ForeignKey;
import com.bknife.orm.annotion.GroupBy;
import com.bknife.orm.annotion.Join;
import com.bknife.orm.annotion.Table;
import com.bknife.orm.annotion.View;
import com.bknife.orm.assemble.SqlAssemble;
import com.bknife.orm.assemble.SqlConstants;
import com.bknife.orm.assemble.SqlContext;

public class MapperFactoryImpl implements MapperFactory, SqlConstants {
    private Map<Class<?>, SqlAssemble> assembles = new HashMap<Class<?>, SqlAssemble>();
    private Map<Class<?>, Map<DataSource, Mapper<?>>> mappers = new HashMap<Class<?>, Map<DataSource, Mapper<?>>>();
    private SqlContext context;
    private boolean showSql = false;

    public MapperFactoryImpl(SqlContext context, boolean showSql) {
        this.context = context;
        this.showSql = showSql;
    }

    private boolean isView(Class<?> clazz) throws Exception {
        while (clazz != Object.class) {
            if (clazz.getDeclaredAnnotation(View.class) != null)
                return true;
            else if (clazz.getDeclaredAnnotation(Table.class) != null)
                return false;
            clazz = clazz.getSuperclass();
        }
        throw new Exception(clazz + " is not a mapper class");
    }

    @SuppressWarnings("unchecked")
    private <T> SqlAssemble getAssemble(Class<T> clazz) throws Exception {
        if (assembles.containsKey(clazz))
            return (SqlAssemble) assembles.get(clazz);
        SqlAssemble assemble = context.getAssemble(clazz, MYSQL);
        assembles.put(clazz, assemble);
        return assemble;
    }

    /**
     * 创建表对象
     * 
     * @param clazz
     * @param dataSource
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private <T> Mapper<T> createTableMapper(Class<T> clazz, DataSource dataSource) throws Exception {
        Map<DataSource, Mapper<?>> mapperMap = mappers.get(clazz);
        if (mapperMap == null) {
            mapperMap = new LinkedHashMap<DataSource, Mapper<?>>();
            mappers.put(clazz, mapperMap);
        }
        Mapper<?> mapper = mapperMap.get(dataSource);
        if (mapper != null)
            return (Mapper<T>) mapper;
        mapper = new TableMapper<T>(getAssemble(clazz), dataSource, showSql);
        mapperMap.put(dataSource, mapper);
        mapper.create();
        return (Mapper<T>) mapper;

    }

    /**
     * 创建表对象
     * 
     * @param clazz
     * @param dataSource
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private <T> Mapper<T> createViewMapper(Class<T> clazz, DataSource dataSource) throws Exception {
        Map<DataSource, Mapper<?>> mapperMap = mappers.get(clazz);
        if (mapperMap == null) {
            mapperMap = new LinkedHashMap<DataSource, Mapper<?>>();
            mappers.put(clazz, mapperMap);
        }
        Mapper<?> mapper = mapperMap.get(dataSource);
        if (mapper != null)
            return (Mapper<T>) mapper;
        mapper = new ViewMapper<T>(getAssemble(clazz), dataSource, showSql);
        mapperMap.put(dataSource, mapper);
        return (Mapper<T>) mapper;
    }

    /**
     * 创建表对象
     * 
     * @param clazz
     * @param dataSource
     * @return
     * @throws Exception
     */
    @SuppressWarnings("all")
    @Override
    public <T> Mapper<T> createMapperByType(Class<T> clazz, DataSource dataSource) throws Exception {
        Class<?> mapperClass = clazz;
        while (mapperClass != Object.class) {
            for (GroupBy groupBy : mapperClass.getDeclaredAnnotationsByType(GroupBy.class)) {
                if (groupBy.tableClass() != Object.class && groupBy.tableClass() != clazz)
                    createMapperByType(groupBy.tableClass(), dataSource);
            }

            for (ForeignKey foreignKey : mapperClass.getDeclaredAnnotationsByType(ForeignKey.class)) {
                if (foreignKey.tableClass() != Object.class && foreignKey.tableClass() != clazz)
                    createMapperByType(foreignKey.tableClass(), dataSource);
            }

            for (Join join : mapperClass.getDeclaredAnnotationsByType(Join.class)) {
                if (join.tableClass() != Object.class && join.tableClass() != clazz)
                    createMapperByType(join.tableClass(), dataSource);
            }
            mapperClass = mapperClass.getSuperclass();
        }

        if (isView(clazz))
            return createViewMapper(clazz, dataSource);
        return createTableMapper(clazz, dataSource);
    }

    @Override
    public <T> MapperService<T> createServiceByType(Class<T> clazz, DataSource dataSource) throws Exception {
        return new MapperServiceBase<T>(createMapperByType(clazz, dataSource));
    }
}
