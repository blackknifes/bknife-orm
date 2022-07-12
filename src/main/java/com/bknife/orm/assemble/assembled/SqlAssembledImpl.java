package com.bknife.orm.assemble.assembled;

import java.sql.PreparedStatement;
import java.util.Collection;

import com.bknife.orm.assemble.SqlGetter;

public class SqlAssembledImpl implements SqlAssembled {
    ;
    private String sql;
    private Collection<SqlGetter> getters;

    public SqlAssembledImpl(String sql, Collection<SqlGetter> getters) {
        this.sql = sql;
        this.getters = getters;
    }

    @Override
    public String getSql() {
        return sql;
    }

    @Override
    public Collection<SqlGetter> getParameterGetters() {
        return getters;
    }

    @Override
    public void setParameter(PreparedStatement preparedStatement, Object object) throws Exception {
        if (getters != null) {
            int i = 0;
            for (SqlGetter getter : getters)
                preparedStatement.setObject(++i, getter.getValue(object));
        }
    }
}
