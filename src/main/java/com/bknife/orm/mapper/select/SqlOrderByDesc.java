package com.bknife.orm.mapper.select;

public class SqlOrderByDesc extends SqlOrderBy {

    public SqlOrderByDesc(String column) {
        super(column);
    }

    @Override
    public boolean isDesc() {
        return true;
    }

}
