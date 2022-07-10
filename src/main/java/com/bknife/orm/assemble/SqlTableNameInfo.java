package com.bknife.orm.assemble;

public class SqlTableNameInfo implements SqlMapperInfo {
    private String name;
    private String sqlName;

    public SqlTableNameInfo(String name) {
        this.name = name;
        this.sqlName = "`" + name + "`";
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getSqlName() {
        return sqlName;
    }
}
