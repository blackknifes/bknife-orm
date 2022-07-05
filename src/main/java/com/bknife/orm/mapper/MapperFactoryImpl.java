package com.bknife.orm.mapper;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.bknife.orm.annotion.DBForeignKey;
import com.bknife.orm.annotion.DBGroupBy;
import com.bknife.orm.annotion.DBJoin;
import com.bknife.orm.annotion.DBTable;
import com.bknife.orm.annotion.DBView;
import com.bknife.orm.mapper.assemble.SqlAssemble;
import com.bknife.orm.mapper.assemble.SqlAssembleFactory;

public class MapperFactoryImpl implements MapperFactory {
    private Map<Class<?>, SqlAssemble<?>> assembles = new HashMap<Class<?>, SqlAssemble<?>>();
    private Map<Class<?>, Map<DataSource, Mapper<?>>> mappers = new HashMap<Class<?>, Map<DataSource, Mapper<?>>>();
    private SqlAssembleFactory sqlAssembleFactory;
    private boolean showSql = false;

    public MapperFactoryImpl(SqlAssembleFactory sqlAssembleFactory, boolean showSql) {
        this.sqlAssembleFactory = sqlAssembleFactory;
        this.showSql = showSql;
    }

    private boolean isView(Class<?> clazz) throws Exception {
        while (clazz != Object.class) {
            if (clazz.getDeclaredAnnotation(DBView.class) != null)
                return true;
            else if (clazz.getDeclaredAnnotation(DBTable.class) != null)
                return false;
            clazz = clazz.getSuperclass();
        }
        throw new Exception(clazz + " is not a mapper class");
    }

    @SuppressWarnings("unchecked")
    private <T> SqlAssemble<T> getAssemble(Class<T> clazz) throws Exception {
        if (assembles.containsKey(clazz))
            return (SqlAssemble<T>) assembles.get(clazz);
        SqlAssemble<T> assemble = sqlAssembleFactory.getAssemble(clazz);
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
            for (DBGroupBy groupBy : mapperClass.getDeclaredAnnotationsByType(DBGroupBy.class)) {
                if (groupBy.tableClass() != Object.class && groupBy.tableClass() != clazz)
                    createMapperByType(groupBy.tableClass(), dataSource);
            }

            for (DBForeignKey foreignKey : mapperClass.getDeclaredAnnotationsByType(DBForeignKey.class)) {
                if (foreignKey.tableClass() != Object.class && foreignKey.tableClass() != clazz)
                    createMapperByType(foreignKey.tableClass(), dataSource);
            }

            for (DBJoin join : mapperClass.getDeclaredAnnotationsByType(DBJoin.class)) {
                if (join.tableClass() != Object.class && join.tableClass() != clazz)
                    createMapperByType(join.tableClass(), dataSource);
            }
            mapperClass = mapperClass.getSuperclass();
        }

        if (isView(clazz))
            return createViewMapper(clazz, dataSource);
        return createTableMapper(clazz, dataSource);
    }
}
