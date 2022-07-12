package com.bknife.orm.assemble;

import java.lang.reflect.Field;

import com.bknife.orm.annotion.Column;

public class SqlViewColumnInfo implements SqlColumnInfo {
    private final SqlNamed table;
    private final Field field;
    private final Column columnAnnotation;

    public static class Builder implements SqlColumnInfo.Builder {
        private SqlNamed table;
        private Field field;
        private Column column;

        @Override
        public SqlColumnInfo build() {
            return new SqlViewColumnInfo(table, field, column);
        }

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

    }

    public SqlViewColumnInfo(SqlNamed table, Field field, Column column) {
        this.table = table;
        this.field = field;
        this.columnAnnotation = column;
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

}
