package com.bknife.orm.assemble.assembled;

import java.sql.PreparedStatement;

import com.bknife.orm.assemble.SqlGetter;

public class SqlAssembledImpl<T> implements SqlAssembled<T> {
    private String sql;
    private Iterable<SqlGetter> getters;

    public SqlAssembledImpl(String sql, Iterable<SqlGetter> getters, boolean verbose) {
        this.sql = sql;
        this.getters = getters;
        if(verbose)
            System.out.println(sql);
    }

    @Override
    public String getSql() {
        return sql;
    }

    @Override
    public void setParameter(PreparedStatement preparedStatement, T object) throws Exception {
        if (getters != null) {
            int i = 0;
            for (SqlGetter getter : getters)
                preparedStatement.setObject(++i, getter.getValue(object));
        }
    }

    @Override
    public String toString() {
        return sql;
    }
}
