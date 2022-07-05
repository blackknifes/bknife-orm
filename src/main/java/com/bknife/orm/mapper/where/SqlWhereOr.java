package com.bknife.orm.mapper.where;

public class SqlWhereOr implements SqlWhereLogic {
    private SqlWhere where;

    public SqlWhereOr(SqlWhere where) {
        this.where = where;
    }

    @Override
    public WhereType getWhereType() {
        return WhereType.LOGIC;
    }

    public SqlWhere getWhere() {
        return where;
    }

    @Override
    public LogicType getLogicType() {
        return LogicType.OR;
    }
}
