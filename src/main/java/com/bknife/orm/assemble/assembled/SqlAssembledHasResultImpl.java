package com.bknife.orm.assemble.assembled;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;

import com.bknife.base.ObjectPool;
import com.bknife.orm.assemble.SqlAssemblePool;
import com.bknife.orm.assemble.SqlGetter;
import com.bknife.orm.assemble.SqlSetter;

public class SqlAssembledHasResultImpl implements SqlAssembledHasResult, ObjectPool.LifeSpan {
    private SqlAssemblePool pool;
    private Class<?> type;
    private String sql;
    private Collection<SqlGetter> getters;
    private Collection<SqlSetter> setters;

    @Override
    public Class<?> getType() {
        return type;
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
    public Collection<SqlSetter> getResultSetters() {
        return setters;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void init(Object... params) throws Exception {
        pool = (SqlAssemblePool) params[0];
        type = (Class<?>) params[1];
        sql = (String) params[2];
        getters = (Collection<SqlGetter>) params[3];
        setters = (Collection<SqlSetter>) params[4];
    }

    @Override
    public void release() {
        for (SqlGetter getter : getters)
            pool.release(getter);
        for (SqlSetter setter : setters)
            pool.release(setter);
        type = null;
        sql = null;
        getters = null;
        setters = null;
    }

    @Override
    public Object createFromResultSet(ResultSet resultSet) throws Exception {
        Object object = type.newInstance();
        int i = 0;
        for (SqlSetter setter : setters) {
            if (setter != null)
                setter.setValue(object, resultSet.getObject(++i));
        }
        return object;
    }

    @Override
    public void setParameter(PreparedStatement preparedStatement, Object object) throws Exception {
        int i = 0;
        for (SqlGetter getter : getters)
            preparedStatement.setObject(++i, getter.getValue(object));
    }
}
