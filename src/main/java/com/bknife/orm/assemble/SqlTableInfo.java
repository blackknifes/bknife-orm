package com.bknife.orm.assemble;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import com.bknife.orm.annotion.Column;
import com.bknife.orm.annotion.ForeignKey;
import com.bknife.orm.annotion.Index;
import com.bknife.orm.annotion.Join;
import com.bknife.orm.annotion.Table;
import com.bknife.orm.annotion.Unique;

/**
 * 表信息
 */
public class SqlTableInfo implements SqlMapperInfo {
    private String sqlName;
    private Class<?> tableClass;
    private Table annotation;

    private Map<String, SqlColumnInfo> columns = new LinkedHashMap<String, SqlColumnInfo>();
    private Map<String, SqlColumnInfo> fieldNameColumns = new LinkedHashMap<String, SqlColumnInfo>();
    private Collection<SqlColumnInfo> primaryKeys = new ArrayList<SqlColumnInfo>();

    public SqlTableInfo(SqlAssembleFactory factory, Class<?> tableClass) throws Exception {
        this.tableClass = tableClass;
        annotation = tableClass.getAnnotation(Table.class);
        if (annotation == null)
            throw new Exception("the class [" + tableClass + "] is not table class");
        sqlName = "`" + annotation.name() + "`";
        do {
            for (Field field : tableClass.getDeclaredFields()) {
                Column column = field.getAnnotation(Column.class);
                if (column != null) {
                    SqlColumnInfo columnInfo = new SqlColumnInfo(factory, this, field, column);
                    columns.put(column.name(), columnInfo);
                    fieldNameColumns.put(column.name(), columnInfo);
                    if (column.primaryKey())
                        primaryKeys.add(columnInfo);
                }
            }
        } while ((tableClass = tableClass.getSuperclass()) != null);
    }

    @Override
    public String getName() {
        return annotation.name();
    }

    @Override
    public String getSqlName() {
        return sqlName;
    }

    /**
     * 获取所有外键
     * 
     * @return
     */
    public ForeignKey[] getForeignKeys() {
        return tableClass.getDeclaredAnnotationsByType(ForeignKey.class);
    }

    /**
     * 获取所有索引
     * 
     * @return
     */
    public Index[] getIndexs() {
        return tableClass.getDeclaredAnnotationsByType(Index.class);
    }

    /**
     * 获取所有Join
     * 
     * @return
     */
    public Join[] getJoins() {
        return tableClass.getDeclaredAnnotationsByType(Join.class);
    }

    /**
     * 获取所有Unique
     * 
     * @return
     */
    public Unique[] getUniques() {
        return tableClass.getDeclaredAnnotationsByType(Unique.class);
    }

    /**
     * 获取所有列
     * 
     * @return
     */
    public Collection<SqlColumnInfo> getColumns() {
        return columns.values();
    }

    /**
     * 获取列
     * 
     * @param name 可以是字段名，也可以使列名
     * @return
     */
    public SqlColumnInfo getColumn(String name) {
        SqlColumnInfo columnInfo = fieldNameColumns.get(name);
        if (columnInfo != null)
            return columnInfo;
        return columns.get(name);
    }

    /**
     * 获取主键集合
     * 
     * @return
     */
    public Collection<SqlColumnInfo> getPrimaryKeys() {
        return primaryKeys;
    }

    /**
     * 获取表类
     * 
     * @return
     */
    public Class<?> getTableClass() {
        return tableClass;
    }

    /**
     * 获取注解
     * 
     * @return
     */
    public Table getAnnotation() {
        return annotation;
    }

    /**
     * 表编码，默认utf8mb4
     * 
     * @return
     */
    public String getCharset() {
        return annotation.charset();
    }

    /**
     * 表排序
     * 
     * @return
     */
    public String getCollate() {
        return annotation.collate();
    }

    /**
     * 表引擎，默认InnoDB
     * 
     * @return
     */
    public String getEngine() {
        return annotation.engine();
    }

    /**
     * 自动增长数值
     * 
     * @return
     */
    public int getIncrement() {
        return annotation.increment();
    }

    /**
     * 注释
     * 
     * @return
     */
    public String getComment() {
        return annotation.comment();
    }
}
