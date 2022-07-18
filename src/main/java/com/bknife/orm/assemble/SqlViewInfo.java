package com.bknife.orm.assemble;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.bknife.orm.annotion.Join;

public class SqlViewInfo<T> implements SqlMapperInfo<T> {
    private final SqlTableInfo<?> tableInfo; // 主表信息
    private final Class<T> viewClass; // 视图类
    private final Map<String, SqlColumnInfo> columnInfos; // 列信息
    private final List<Join> joins = new ArrayList<Join>();

    public static <T> SqlViewInfo<T> create(SqlTableInfo<?> tableInfo, Class<T> viewClass,
            Collection<SqlColumnInfo.Builder> builders) {
        return new SqlViewInfo<>(tableInfo, viewClass, builders);
    }

    private SqlViewInfo(SqlTableInfo<?> tableInfo, Class<T> viewClass, Collection<SqlColumnInfo.Builder> builders) {
        this.tableInfo = tableInfo;
        this.viewClass = viewClass;
        this.columnInfos = new LinkedHashMap<String, SqlColumnInfo>();
        for (SqlColumnInfo.Builder builder : builders) {
            SqlColumnInfo info = builder.build();
            this.columnInfos.put(info.getField().getName(), info);
        }

        Class<?> tempClass = viewClass;
        do {
            Join[] joins = tempClass.getDeclaredAnnotationsByType(Join.class);
            for (Join join : joins)
                this.joins.add(join);
        } while ((tempClass = tempClass.getSuperclass()) != null);
    }

    @Override
    public Class<T> getMapperClass() {
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

    public SqlTableInfo<?> getTableInfo() {
        return tableInfo;
    }

    @Override
    public Iterable<SqlTableColumnInfo> getPrimaryKeys() {
        return tableInfo.getPrimaryKeys();
    }
}
