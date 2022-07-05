package com.bknife.orm.mapper.where;

public interface SqlWhereLogic extends SqlWhere {
    public static enum LogicType {
        AND,
        OR,
        NOT
    }

    /**
     * 获取条件
     * 
     * @return
     */
    public SqlWhere getWhere();

    /**
     * 获取逻辑类型
     * 
     * @return
     */
    public LogicType getLogicType();
}
