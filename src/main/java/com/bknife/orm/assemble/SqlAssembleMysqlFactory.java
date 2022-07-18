package com.bknife.orm.assemble;

public class SqlAssembleMysqlFactory implements SqlAssembleFactory {

    @Override
    public SqlAssemble getAssemble(SqlContext context) throws Exception {
        return new SqlAssembleMysql(context);
    }

}
