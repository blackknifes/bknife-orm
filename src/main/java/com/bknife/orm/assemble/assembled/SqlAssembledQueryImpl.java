package com.bknife.orm.assemble.assembled;

import java.lang.reflect.Constructor;
import java.sql.ResultSet;

import com.bknife.orm.assemble.SqlGetter;
import com.bknife.orm.assemble.SqlSetter;

public class SqlAssembledQueryImpl<T> extends SqlAssembledImpl<T> implements SqlAssembledQuery<T> {
    private Class<T> type;
    private Iterable<SqlSetter> setters;

    public SqlAssembledQueryImpl(Class<T> type, String sql, Iterable<SqlGetter> getters,
            Iterable<SqlSetter> setters, boolean verbose) {
        super(sql, getters, verbose);
        this.type = type;
        this.setters = setters;
    }

    @Override
    public Class<T> getType() {
        return type;
    }

    @Override
    public T createFromResultSet(ResultSet resultSet) throws Exception {
        Constructor<T> constructor = type.getDeclaredConstructor();
        constructor.setAccessible(true);
        T object = constructor.newInstance();
        int i = 0;
        for (SqlSetter setter : setters) {
            if (setter != null)
                setter.setValue(object, resultSet.getObject(++i));
        }
        return object;
    }
}
