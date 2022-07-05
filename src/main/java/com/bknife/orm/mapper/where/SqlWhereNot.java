package com.bknife.orm.mapper.where;

public class SqlWhereNot implements SqlWhereLogic {
    private SqlWhere where;

    public SqlWhereNot(SqlWhere where) {
        this.where = where;
    }

    @Override
    public WhereType getWhereType() {
        return WhereType.LOGIC;
    }

    @Override
    public SqlWhere getWhere() {
        return where;
    }

    @Override
    public LogicType getLogicType() {
        return LogicType.NOT;
    }

}
