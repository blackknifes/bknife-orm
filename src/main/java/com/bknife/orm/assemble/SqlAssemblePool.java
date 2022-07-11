package com.bknife.orm.assemble;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import com.bknife.base.ObjectPool;
import com.bknife.orm.assemble.assembled.SqlAssembled;
import com.bknife.orm.assemble.assembled.SqlAssembledImpl;
import com.bknife.orm.assemble.assembled.SqlAssembledQuery;
import com.bknife.orm.assemble.assembled.SqlAssembledQueryImpl;
import com.bknife.orm.assemble.getter.SqlGetterField;
import com.bknife.orm.assemble.getter.SqlGetterMap;
import com.bknife.orm.assemble.getter.SqlGetterValue;
import com.bknife.orm.assemble.setter.SqlSetterField;
import com.bknife.orm.assemble.setter.SqlSetterList;
import com.bknife.orm.assemble.setter.SqlSetterMap;

public class SqlAssemblePool {

    public static final Collection<SqlGetter> EMPTY_GETTER = new ArrayList<SqlGetter>();

    private ObjectPool<SqlAssembledQueryImpl> assembledQueryPool = new ObjectPool<SqlAssembledQueryImpl>(
            SqlAssembledQueryImpl.class);
    private ObjectPool<SqlAssembledImpl> assembledPool = new ObjectPool<SqlAssembledImpl>(
            SqlAssembledImpl.class);
    private ObjectPool<SqlGetterField> getterFieldPool = new ObjectPool<SqlGetterField>(SqlGetterField.class);
    private ObjectPool<SqlGetterMap> getterMapPool = new ObjectPool<SqlGetterMap>(SqlGetterMap.class);
    private ObjectPool<SqlGetterValue> getterValuePool = new ObjectPool<SqlGetterValue>(SqlGetterValue.class);
    private ObjectPool<SqlSetterField> setterFieldPool = new ObjectPool<SqlSetterField>(SqlSetterField.class);
    private ObjectPool<SqlSetterList> setterListPool = new ObjectPool<SqlSetterList>(SqlSetterList.class);
    private ObjectPool<SqlSetterMap> setterMapPool = new ObjectPool<SqlSetterMap>(SqlSetterMap.class);

    private static final SqlAssemblePool instance = new SqlAssemblePool();

    public static SqlAssemblePool getPool() {
        return instance;
    }

    public SqlAssembledQuery getAssembledQuery(Class<?> clazz, String sql, Collection<SqlGetter> getters,
            Collection<SqlSetter> setters) {
        return assembledQueryPool.get(this, clazz, sql, getters, setters);
    }

    public SqlAssembled getAssembled(String sql, Collection<SqlGetter> getters) {
        return assembledPool.get(this, sql, getters);
    }

    public SqlGetter getGetterField(Field field) {
        return getterFieldPool.get(field);
    }

    public SqlGetter getGetterMap(String name) {
        return getterMapPool.get(name);
    }

    public SqlGetter getGetterValue(Object value) {
        return getterValuePool.get(value);
    }

    public SqlSetter getSetterField(Field field) {
        return setterFieldPool.get(field);
    }

    public SqlSetter getSqlSetterList() {
        return setterListPool.get();
    }

    public SqlSetter getSqlSetterMap(String name) {
        return setterMapPool.get(name);
    }

    public void release(SqlAssembled assembled) {
        if (assembled instanceof SqlAssembledQueryImpl)
            assembledQueryPool.release((SqlAssembledQueryImpl) assembled);
        else
            assembledPool.release((SqlAssembledImpl) assembled);
    }

    public void release(SqlGetter getter) {
        if (getter instanceof SqlGetterField)
            getterFieldPool.release((SqlGetterField) getter);
        else if (getter instanceof SqlGetterMap)
            getterMapPool.release((SqlGetterMap) getter);
        else if (getter instanceof SqlGetterValue)
            getterValuePool.release((SqlGetterValue) getter);
    }

    public void release(SqlSetter setter) {
        if (setter instanceof SqlSetterField)
            setterFieldPool.release((SqlSetterField) setter);
        else if (setter instanceof SqlSetterMap)
            setterMapPool.release((SqlSetterMap) setter);
        else if (setter instanceof SqlSetterList)
            setterListPool.release((SqlSetterList) setter);
    }
}
