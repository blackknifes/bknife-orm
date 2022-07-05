package com.bknife.orm.mapper.where;

public class SqlWhereGreaterEqual implements SqlWhereBinary {
    private String column;
    private Object value;

    public SqlWhereGreaterEqual(String column, Object value) {
        this.column = column;
        this.value = value;
    }

    @Override
    public WhereType getWhereType() {
        return WhereType.BINARY;
    }

    public String getColumn() {
        return column;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public BinaryType getBinaryType() {
        return BinaryType.GREATER_EQUAL;
    }
}
