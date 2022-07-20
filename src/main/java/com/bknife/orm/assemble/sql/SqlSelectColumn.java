package com.bknife.orm.assemble.sql;

public interface SqlSelectColumn extends SqlSelectInto {
    public SqlSelectFrom from(String table);
}
