package com.bknife.orm.assemble;

public interface SqlAssembleFactory {
    public SqlAssemble getAssemble(Class<?> clazz) throws Exception;

    public SqlMapperInfo getMapperInfo(Class<?> clazz) throws Exception;
}
