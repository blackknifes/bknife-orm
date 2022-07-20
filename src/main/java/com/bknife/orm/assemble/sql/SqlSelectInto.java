package com.bknife.orm.assemble.sql;

public interface SqlSelectInto {
    public SqlSelectColumn name(String name);

    public SqlSelectColumn name(String table, String name);

    public SqlSelectColumn distinctName(String name);

    public SqlSelectColumn distinctName(String table, String name);

    public SqlSelectColumn nameAlias(String name, String alias);

    public SqlSelectColumn nameAlias(String table, String name, String alias);

    public SqlSelectColumn sqlAlias(String sql, String alias);

    public SqlSelectColumn distinctNameAlias(String name, String alias);

    public SqlSelectColumn distinctNameAlias(String table, String name, String alias);

    public SqlSelectColumn distinctSqlAlias(String sql, String alias);
}
