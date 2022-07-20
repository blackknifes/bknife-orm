package com.bknife.orm.assemble.sql;

public interface SqlOrderBy extends SqlFinished {
    public SqlOrderBy asc(String name);

    public SqlOrderBy asc(String table, String name);

    public SqlOrderBy desc(String name);

    public SqlOrderBy desc(String table, String name);

    public SqlLimit endOrderBy();
}
