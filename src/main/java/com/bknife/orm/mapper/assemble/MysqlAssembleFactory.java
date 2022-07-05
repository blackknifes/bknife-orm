package com.bknife.orm.mapper.assemble;

public class MysqlAssembleFactory implements SqlAssembleFactory {

    @Override
    public <T> SqlAssemble<T> getAssemble(Class<T> clazz) throws Exception {

        return SqlAssembleMysql.create(clazz);
    }

}
