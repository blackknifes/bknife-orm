package com.bknife.orm.assemble;

import java.lang.reflect.Field;

import com.bknife.orm.annotion.Column;
import com.bknife.orm.annotion.Column.Type;

/**
 * 列信息
 */
public class SqlColumnInfo implements SqlElement {
    private String sqlName;
    private SqlMapperInfo mapperInfo;
    private Field field;
    private Column annotation;

    public SqlColumnInfo(SqlAssembleFactory factory, SqlTableInfo tableInfo, Field field, Column column)
            throws Exception {
        this.field = field;
        this.annotation = column;

        if (!annotation.table().isEmpty())
            mapperInfo = new SqlTableNameInfo(annotation.table());
        else if (annotation.tableClass() != Object.class && annotation.tableClass() != tableInfo.getTableClass()) {
            mapperInfo = factory.getMapperInfo(annotation.tableClass());
        } else if (annotation.sql().isEmpty())
            mapperInfo = tableInfo;

        sqlName = "`" + column.name() + "`";
    }

    /**
     * 获取来源类
     * 
     * @return
     */
    public SqlMapperInfo getMapperInfo() {
        return mapperInfo;
    }

    /**
     * 来源表类
     * 
     * @return
     */
    public Class<?> getTableClass() {
        if (mapperInfo == null)
            return null;
        if (mapperInfo instanceof SqlTableInfo)
            return ((SqlTableInfo) mapperInfo).getTableClass();
        return null;
    }

    /**
     * 来源表名
     * 
     * @return
     */
    public String getTableName() {
        if (mapperInfo == null)
            return "";
        return mapperInfo.getName();
    }

    /**
     * 获取对应属性
     * 
     * @return
     */
    public Field getField() {
        return field;
    }

    /**
     * 获取注解类
     * 
     * @return
     */
    public Column getAnnotation() {
        return annotation;
    }

    /**
     * 是否为一个sql语句列
     * 
     * @return
     */
    public boolean isSqlColumn() {
        return !annotation.sql().isEmpty();
    }

    /**
     * 是否为一个视图列
     * 
     * @return
     */
    public boolean isViewColumn() {
        return annotation.tableClass() != Object.class || !annotation.table().isEmpty();
    }

    /**
     * 是否为一个表列
     * 
     * @return
     */
    public boolean isTableColumn() {
        return !isSqlColumn() && !isViewColumn();
    }

    /**
     * 获取列名
     * 
     * @return
     */
    public String getName() {
        return annotation.name();
    }

    /**
     * 获取sql名
     * 
     * @return
     */
    @Override
    public String getSqlName() {
        return sqlName;
    }

    /**
     * 获取sql全名
     * 
     * @return
     */
    public String getSqlFullName() {
        return "`" + getTableName() + "`.`" + annotation.name() + "`";
    }

    /**
     * 类型
     */
    public Type getType() {
        if (annotation.type() == Type.AUTO)
            return SqlTypeMapper.getSqlType(field.getType());
        return annotation.type();
    }

    /**
     * 长度
     */
    public int getLength() {
        return annotation.length();
    }

    /**
     * 小数点
     */
    public int getDot() {
        return annotation.dot();
    }

    /**
     * 是否为无符号
     * 
     * @return
     */
    public boolean isUnsigned() {
        return annotation.unsigned();
    }

    /**
     * 唯一性
     * 
     * @return
     */
    public boolean isUnique() {
        return annotation.unique();
    }

    /**
     * 是否为主键
     */
    public boolean isPrimaryKey() {
        return annotation.primaryKey();
    }

    /**
     * 是否可为空
     */
    public boolean isNullable() {
        return annotation.nullable();
    };

    /**
     * 注释
     */
    public String getComment() {
        return annotation.comment();
    }

    /**
     * 自动增长
     */
    public boolean isIncrement() {
        return annotation.increment();
    }

    /**
     * 默认值
     */
    public String getDefaultValue() {
        return annotation.defaultValue();
    }

    /**
     * 字符集
     */
    public String getCharset() {
        return annotation.charset();
    }

    /**
     * 排序规则
     */
    public String getCollate() {
        return annotation.collate();
    }

    /**
     * 自定义sql语句
     * 
     * @return
     */
    public String getSql() {
        return annotation.sql();
    }
}
