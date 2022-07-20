package com.bknife.orm.assemble.sql;

public interface SqlSelect extends SqlSelectInto {
    public SqlSelectInto into(String table);
}
