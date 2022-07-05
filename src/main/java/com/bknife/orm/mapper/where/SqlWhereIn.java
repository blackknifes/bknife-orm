package com.bknife.orm.mapper.where;

import java.util.Collection;

public class SqlWhereIn implements SqlWhere {
    private String column;
    private Collection<Object> values;

    /**
     * @param column
     * @param values
     */
    public SqlWhereIn(String column, Collection<Object> values) {
        this.column = column;
        this.values = values;
    }

    @Override
    public WhereType getWhereType() {
        return WhereType.IN;
    }

    public String getColumn() {
        return column;
    }

    public Collection<Object> getValues() {
        return values;
    }
}
