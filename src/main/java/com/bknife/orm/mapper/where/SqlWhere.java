package com.bknife.orm.mapper.where;

public interface SqlWhere {
    public static enum WhereType
    {
        LOGIC,
        UNARY,
        BINARY,
        IN,
        CONDITION
    }

    public WhereType getWhereType();
}
