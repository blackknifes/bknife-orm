package com.bknife.orm.assemble.assembled;

import java.sql.PreparedStatement;
import java.util.Collection;

import com.bknife.base.ObjectPool;
import com.bknife.orm.assemble.SqlAssemblePool;
import com.bknife.orm.assemble.SqlGetter;

public class SqlAssembledHasParameterImpl implements SqlAssembledHasParameter, ObjectPool.LifeSpan {
    private SqlAssemblePool pool;
    private String sql;
    private Collection<SqlGetter> getters;

    @Override
    public String getSql() {
        return sql;
    }

    @Override
    public Collection<SqlGetter> getParameterGetters() {
        return getters;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void init(Object... params) throws Exception {
        pool = (SqlAssemblePool) params[0];
        sql = (String) params[1];
        getters = (Collection<SqlGetter>) params[2];
    }

    @Override
    public void release() {
        for (SqlGetter getter : getters)
            pool.release(getter);
        sql = null;
        getters = null;
    }

    @Override
    public void setParameter(PreparedStatement preparedStatement, Object object) throws Exception {
        int i = 0;
        for (SqlGetter getter : getters)
            preparedStatement.setObject(++i, getter.getValue(object));
    }
}
