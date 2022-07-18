package com.bknife.orm.assemble;

import java.lang.reflect.Field;

import com.bknife.base.converter.ConverterUtils;
import com.bknife.orm.annotion.Column;
import com.bknife.orm.annotion.Column.Type;

public class SqlTableColumnInfo implements SqlColumnInfo {
    private final SqlNamed table;
    private final Field field;
    private final Column columnAnnotation;
    private final Object defaultValue;

    public static class Builder implements SqlColumnInfo.Builder {
        private SqlNamed table;
        private Field field;
        private Column column;

        public SqlNamed getTable() {
            return table;
        }

        public Builder setTable(SqlNamed table) {
            this.table = table;
            return this;
        }

        public Field getField() {
            return field;
        }

        public Builder setField(Field field) {
            this.field = field;
            return this;
        }

        public Column getColumn() {
            return column;
        }

        public Builder setColumn(Column column) {
            this.column = column;
            return this;
        }

        @Override
        public SqlColumnInfo build() {
            return new SqlTableColumnInfo(table, field, column);
        }
    }

    public SqlTableColumnInfo(SqlNamed table, Field field, Column column) {
        this.table = table;
        this.field = field;
        this.columnAnnotation = column;
        Object val = null;
        if (!column.defaultValue().isEmpty()) {
            try {
                val = ConverterUtils.convert(column.defaultValue(), SqlTypeUtil.getClassType(column.type()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            val = null;
        }
        defaultValue = val;
    }

    @Override
    public String getName() {
        return columnAnnotation.name();
    }

    @Override
    public String getTableName() {
        return table.getName();
    }

    @Override
    public Field getField() {
        return field;
    }

    @Override
    public Column getAnnotation() {
        return columnAnnotation;
    }

    /**
     * 获取类型
     * 
     * @return
     */
    public Type getType() {
        return columnAnnotation.type();
    }

    /**
     * 获取长度
     * 
     * @return
     */
    public int getLength() {
        return columnAnnotation.length();
    }

    /**
     * 获取小数点位数
     * 
     * @return
     */
    public int getDot() {
        return columnAnnotation.dot();
    }

    /**
     * 是否为无符号
     * 
     * @return
     */
    public boolean isUnsigned() {
        return columnAnnotation.unsigned();
    }

    /**
     * 是否为主键
     * 
     * @return
     */
    public boolean isPrimaryKey() {
        return columnAnnotation.primaryKey();
    }

    /**
     * 是否允许为空
     * 
     * @return
     */
    public boolean isNullable() {
        return columnAnnotation.nullable();
    }

    /**
     * 获取注释
     * 
     * @return
     */
    public String getComment() {
        return columnAnnotation.comment();
    }

    /**
     * 是否自增长
     * 
     * @return
     */
    public boolean isAutoIncrement() {
        return columnAnnotation.increment();
    }

    /**
     * 获取默认值
     * 
     * @return
     */
    public Object getDefault() {
        return defaultValue;
    }

    /**
     * 获取字符集
     * 
     * @return
     */
    public String getCharset() {
        return columnAnnotation.charset();
    }

    /**
     * 获取排序规则
     * 
     * @return
     */
    public String getCollate() {
        return columnAnnotation.collate();
    }
}
