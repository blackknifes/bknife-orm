package com.bknife.orm.assemble;

import java.util.HashMap;
import java.util.Map;

public class MysqlAssembleFactory implements SqlAssembleFactory {
    private Map<Class<?>, SqlAssemble<?>> assembles = new HashMap<Class<?>, SqlAssemble<?>>();

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

}
