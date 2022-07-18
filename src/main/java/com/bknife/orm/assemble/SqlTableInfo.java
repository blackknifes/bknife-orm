package com.bknife.orm.assemble;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.bknife.orm.annotion.Check;
import com.bknife.orm.annotion.ForeignKey;
import com.bknife.orm.annotion.GroupBy;
import com.bknife.orm.annotion.Index;
import com.bknife.orm.annotion.Table;
import com.bknife.orm.annotion.Unique;

/**
 * 表信息
 */
public class SqlTableInfo<T> implements SqlMapperInfo<T>, SqlNamed {
    private final Class<T> tableClass;
    private final Table tableAnnotation;
    private final Map<String, SqlColumnInfo> columns;

    private List<SqlTableColumnInfo> primaryKeys = new ArrayList<SqlTableColumnInfo>();

    public static <T> SqlTableInfo<T> create(Class<T> tableClass, Table tableAnnotation,
            Collection<SqlTableColumnInfo.Builder> builders) {
        return new SqlTableInfo<>(tableClass, tableAnnotation, builders);
    }

    private SqlTableInfo(Class<T> tableClass, Table tableAnnotation, Collection<SqlTableColumnInfo.Builder> builders) {
        this.tableClass = tableClass;
        this.tableAnnotation = tableAnnotation;
        this.columns = new LinkedHashMap<String, SqlColumnInfo>();
        for (SqlTableColumnInfo.Builder builder : builders) {
            builder.setTable(this);
            this.columns.put(builder.getField().getName(), builder.build());
        }

        for (SqlColumnInfo columnInfo : this.columns.values()) {
            SqlTableColumnInfo tableColumn = (SqlTableColumnInfo) columnInfo;
            if (tableColumn.isPrimaryKey())
                primaryKeys.add(tableColumn);
        }
    }

    @Override
    public String getName() {
        return tableAnnotation.name();
    }

    @Override
    public String getTableName() {
        return tableAnnotation.name();
    }

    @Override
    public Class<T> getMapperClass() {
        return tableClass;
    }

    @Override
    public Iterable<SqlColumnInfo> getColumns() {
        return columns.values();
    }

    @Override
    public SqlColumnInfo getColumn(String name) {
        return columns.get(name);
    }

    @Override
    public Iterable<SqlTableColumnInfo> getPrimaryKeys() {
        return primaryKeys;
    }

    /**
     * 获取所有外键
     * 
     * @return
     */
    public Iterable<ForeignKey> getForeignKeys() {
        return new ArrayIterable<ForeignKey>(tableClass.getDeclaredAnnotationsByType(ForeignKey.class));
    }

    /**
     * 获取所有group by
     * 
     * @return
     */
    public Iterable<GroupBy> getGroupBies() {
        return new ArrayIterable<GroupBy>(tableClass.getDeclaredAnnotationsByType(GroupBy.class));
    }

    /**
     * 获取所有索引
     * 
     * @return
     */
    public Iterable<Index> getIndices() {
        return new ArrayIterable<Index>(tableClass.getDeclaredAnnotationsByType(Index.class));
    }

    /**
     * 获取所有唯一约束
     * 
     * @return
     */
    public Iterable<Unique> getUniques() {
        return new ArrayIterable<Unique>(tableClass.getDeclaredAnnotationsByType(Unique.class));
    }

    /**
     * 获取所有check约束
     * 
     * @return
     */
    public Iterable<Check> getChecks() {
        return new ArrayIterable<Check>(tableClass.getDeclaredAnnotationsByType(Check.class));
    }

    public String getEngine() {
        return tableAnnotation.engine();
    }

    public String getCharset() {
        return tableAnnotation.charset();
    }

    public String getCollate() {
        return tableAnnotation.collate();
    }

    public String getComment() {
        return tableAnnotation.comment();
    }

    public int getAutoIncrement() {
        return tableAnnotation.increment();
    }
}
