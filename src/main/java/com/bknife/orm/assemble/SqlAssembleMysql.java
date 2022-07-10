package com.bknife.orm.assemble;

import com.bknife.orm.assemble.assembled.SqlAssembled;
import com.bknife.orm.assemble.assembled.SqlAssembledQuery;
import com.bknife.orm.mapper.Updater;
import com.bknife.orm.mapper.where.Condition;

public class SqlAssembleMysql implements SqlAssemble {
    private SqlTableInfo tableInfo;

    public SqlAssembleMysql(SqlAssembleFactory factory, Class<T> clazz) throws Exception {
        SqlMapperInfo mapperInfo = factory.getMapperInfo(clazz);
        if (mapperInfo == null || !(mapperInfo instanceof SqlTableInfo))
            throw new Exception("class [" + clazz + "] is not a table class");
        tableInfo = (SqlTableInfo) mapperInfo;
    }

    @Override
    public Class<?> getResultType() {
        return tableInfo.getTableClass();
    }

    @Override
    public SqlAssembled assembleCreateTable() throws Exception {

        return null;
    }

    @Override
    public SqlAssembled assembleCount(Condition condition) throws Exception {

        return null;
    }

    @Override
    public SqlAssembledQuery assembleSelect(Condition condition) throws Exception {

        return null;
    }

    @Override
    public SqlAssembled assembleDelete(Condition condition) throws Exception {

        return null;
    }

    @Override
    public SqlAssembled assembleUpdate(Updater updater) throws Exception {

        return null;
    }

    @Override
    public SqlAssembled assembleInsert() throws Exception {

        return null;
    }

    @Override
    public SqlAssembled assembleReplace() throws Exception {

        return null;
    }

}
