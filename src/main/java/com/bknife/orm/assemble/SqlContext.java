package com.bknife.orm.assemble;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class SqlContext {
    private SqlFactory factory;
    private Map<Class<?>, SqlAssemble> assembles = new HashMap<Class<?>, SqlAssemble>();
    private Map<Class<?>, SqlMapperInfo> mappers = new HashMap<Class<?>, SqlMapperInfo>();

    public SqlContext(SqlFactory factory) {
        this.factory = factory;
    }

    public SqlAssemble getAssemble(Class<?> mapperClass, String sqlType) throws Exception {
        SqlMapperInfo mapper = getMapperInfo(mapperClass);
        if (mapper == null)
            return null;

        SqlAssemble assemble = assembles.get(mapperClass);
        if (assemble != null)
            return assemble;
        assemble = factory.getAssemble(this, mapperClass, sqlType);
        if (assemble == null)
            return null;
        assembles.put(mapperClass, assemble);
        return assemble;
    }

    public SqlMapperInfo getMapperInfo(Class<?> mapperClass) throws Exception {
        SqlMapperInfo mapper = mappers.get(mapperClass);
        if (mapper == null) {
            mapper = factory.getMapperInfo(this, mapperClass);
            if (mapper == null)
                return null;
            mappers.put(mapperClass, mapper);
        }

        return mapper;
    }
}
