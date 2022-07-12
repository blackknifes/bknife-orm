package com.bknife.orm.assemble;

public class SqlTableNameInfo implements SqlNamed {
    private String name;

    public SqlTableNameInfo(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
