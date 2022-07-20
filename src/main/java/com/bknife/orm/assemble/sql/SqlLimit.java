package com.bknife.orm.assemble.sql;

public interface SqlLimit {
    public SqlFinished limit(int offset, int total);
}
