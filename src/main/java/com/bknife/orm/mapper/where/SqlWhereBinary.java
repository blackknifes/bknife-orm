package com.bknife.orm.mapper.where;

public interface SqlWhereBinary extends SqlWhere {
    public static enum BinaryType {
        EQUAL,
        NOT_EQUAL,
        GREATER,
        GREATER_EQUAL,
        LESS,
        LESS_EQUAL,
        LIKE,
        LIKE_MATCH,
        LEFT_LIKE,
        RIGHT_LIKE
    }

    /**
     * 获取运算符类型
     * 
     * @return
     */
    public BinaryType getBinaryType();

    /**
     * 获取列名
     * 
     * @return
     */
    public String getColumn();

    /**
     * 获取值
     * 
     * @return
     */
    public Object getValue();
}
