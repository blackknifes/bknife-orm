package com.bknife.orm.assemble;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.bknife.orm.annotion.Join;

public class SqlViewInfo implements SqlMapperInfo {
    private final SqlTableInfo tableInfo; // 主表信息
    private final Class<?> viewClass; // 视图类
    private final Map<String, SqlColumnInfo> columnInfos; // 列信息
    private final List<Join> joins = new ArrayList<Join>();

    public SqlViewInfo(SqlTableInfo tableInfo, Class<?> viewClass, Collection<SqlColumnInfo.Builder> builders) {
        this.tableInfo = tableInfo;
        this.viewClass = viewClass;
        this.columnInfos = new LinkedHashMap<String, SqlColumnInfo>();
        for (SqlColumnInfo.Builder builder : builders) {
            SqlColumnInfo info = builder.build();
            this.columnInfos.put(info.getField().getName(), info);
        }
    }

    @Override
    public Class<?> getMapperClass() {
        return viewClass;
    }

    @Override
    public String getTableName() {
        return tableInfo.getName();
    }

    @Override
    public Iterable<SqlColumnInfo> getColumns() {
        return columnInfos.values();
    }

    @Override
    public SqlColumnInfo getColumn(String name) {
        return columnInfos.get(name);
    }

    public Iterable<Join> getJoins() {
        return joins;
    }

    public SqlTableInfo getTableInfo() {
        return tableInfo;
    }

    @Override
    public Iterable<SqlTableColumnInfo> getPrimaryKeys() {
        return tableInfo.getPrimaryKeys();
    }
}
