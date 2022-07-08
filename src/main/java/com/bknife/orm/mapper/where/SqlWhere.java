package com.bknife.orm.mapper.where;

public interface SqlWhere {
    public static enum WhereType {
        LOGIC, // 不需要参数化
        UNARY, // 不需要参数化
        BINARY, // 需要参数化
        IN, // 动态参数化
        CONDITION
    }

    public WhereType getWhereType();
}
