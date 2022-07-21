package com.bknife.orm.assemble.impl.mysql;

import com.bknife.orm.assemble.SqlAssemble;
import com.bknife.orm.assemble.SqlAssembleFactory;
import com.bknife.orm.assemble.SqlContext;

public class SqlAssembleFactoryMysql implements SqlAssembleFactory {

    @Override
    public SqlAssemble getAssemble(SqlContext context) throws Exception {
        return new SqlAssembleMysql(context);
    }
}
