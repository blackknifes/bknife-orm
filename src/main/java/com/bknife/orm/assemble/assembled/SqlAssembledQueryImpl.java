package com.bknife.orm.assemble.assembled;

import java.lang.reflect.Constructor;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;

import com.bknife.orm.assemble.SqlGetter;
import com.bknife.orm.assemble.SqlSetter;

public class SqlAssembledQueryImpl implements SqlAssembledQuery {
    private Class<?> type;
    private String sql;
    private Collection<SqlGetter> getters;
    private Collection<SqlSetter> setters;

    public SqlAssembledQueryImpl(Class<?> type, String sql, Collection<SqlGetter> getters,
            Collection<SqlSetter> setters) {
        this.type = type;
        this.sql = sql;
        this.getters = getters;
        this.setters = setters;
    }

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

    @Override
    public Object createFromResultSet(ResultSet resultSet) throws Exception {
        Constructor<?> constructor = type.getDeclaredConstructor();
        constructor.setAccessible(true);
        Object object = constructor.newInstance();
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

    @Override
    public String toString() {
        return "SqlAssembledQueryImpl [sql=" + sql + ", type=" + type + "]";
    }
}
