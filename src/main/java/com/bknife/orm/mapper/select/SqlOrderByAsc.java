package com.bknife.orm.mapper.select;

public class SqlOrderByAsc extends SqlOrderBy {

    public SqlOrderByAsc(String column) {
        super(column);
    }

    @Override
    public boolean isDesc() {
        return false;
    }
}
