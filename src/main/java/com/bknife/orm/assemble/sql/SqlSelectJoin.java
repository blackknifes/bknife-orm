package com.bknife.orm.assemble.sql;

public interface SqlSelectJoin {
    public SqlSelectJoin andOn(String sourceTable, String sourceName, String targetTable, String targetName);

    public SqlSelectJoin orOn(String sourceTable, String sourceName, String targetTable, String targetName);

    public SqlSelectJoin andNotOn(String sourceTable, String sourceName, String targetTable, String targetName);

    public SqlSelectJoin orNotOn(String sourceTable, String sourceName, String targetTable, String targetName);

    public SqlSelectFrom endJoin();
}
