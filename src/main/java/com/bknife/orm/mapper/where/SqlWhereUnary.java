package com.bknife.orm.mapper.where;

public interface SqlWhereUnary extends SqlWhere {
    public static enum UnaryType {
        IS_NULL
    }

    /**
     * 获取一元运算符类型
     * 
     * @return
     */
    public UnaryType getUnaryType();

    /**
     * 获取列名
     * 
     * @return
     */
    public String getColumn();
}
