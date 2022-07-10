package com.bknife.orm.assemble;

import java.util.HashMap;
import java.util.Map;

public class MysqlAssembleFactory implements SqlAssembleFactory {
    private Map<Class<?>, SqlAssemble<?>> assembles = new HashMap<Class<?>, SqlAssemble<?>>();
    private Map<Class<?>, SqlMapperInfo> mapperInfos = new HashMap<Class<?>, SqlMapperInfo>();

    @SuppressWarnings("unchecked")
    @Override
    public <T> SqlAssemble<T> getAssemble(Class<T> clazz) throws Exception {
        SqlAssemble<T> assemble;
        synchronized (assembles) {
            assemble = (SqlAssemble<T>) assembles.get(clazz);
            if (assemble == null) {
                assemble = SqlAssembleMysql.create(clazz);
                assembles.put(clazz, assemble);
            }
        }
        return assemble;
    }

    @Override
    public SqlMapperInfo getMapperInfo(Class<?> clazz) throws Exception {
        SqlMapperInfo sqlMapperInfo = mapperInfos.get(clazz);
        if (sqlMapperInfo == null) {
            sqlMapperInfo = new SqlTableInfo(this, clazz);
            mapperInfos.put(clazz, sqlMapperInfo);
        }

        return sqlMapperInfo;
    }

}