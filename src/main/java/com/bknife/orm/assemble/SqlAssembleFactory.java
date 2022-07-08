package com.bknife.orm.assemble;

public interface SqlAssembleFactory {
    public <T> SqlAssemble<T> getAssemble(Class<T> clazz) throws Exception;
}
