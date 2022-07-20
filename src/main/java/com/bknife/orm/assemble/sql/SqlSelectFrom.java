package com.bknife.orm.assemble.sql;

public interface SqlSelectFrom extends SqlLimit, SqlFinished {
    public SqlSelectJoin leftJoin(String table);

    public SqlSelectJoin rightJoin(String table);

    public SqlSelectJoin innerJoin(String table);

    public SqlSelectJoin outterJoin(String table);

    public SqlWhere where();

    public SqlOrderBy orderBy();
}
