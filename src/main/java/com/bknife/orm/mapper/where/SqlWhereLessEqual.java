package com.bknife.orm.mapper.where;

public class SqlWhereLessEqual implements SqlWhereBinary {
    private String column;
    private Object value;

    public SqlWhereLessEqual(String column, Object value) {
        this.column = column;
        this.value = value;
    }

    @Override
    public WhereType getWhereType() {
        return WhereType.BINARY;
    }

    @Override
    public String getColumn() {
        return column;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public BinaryType getBinaryType() {
        return BinaryType.LESS_EQUAL;
    }
}
