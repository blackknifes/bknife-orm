package com.bknife.orm.assemble.sql;

public interface SqlSelectIntoable extends SqlSelect {
    public SqlSelect into(String table);
}
