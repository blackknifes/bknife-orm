package com.bknife.orm.mapper.where;

public class SqlWhereIsNull implements SqlWhereUnary {
    private String column;

    public SqlWhereIsNull(String column) {
        this.column = column;
    }

    @Override
    public WhereType getWhereType() {
        return WhereType.UNARY;
    }

    @Override
    public String getColumn() {
        return column;
    }

    @Override
    public UnaryType getUnaryType() {
        return UnaryType.IS_NULL;
    }

}
