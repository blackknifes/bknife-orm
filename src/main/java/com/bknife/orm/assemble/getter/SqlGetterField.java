package com.bknife.orm.assemble.getter;

import java.lang.reflect.Field;

import com.bknife.base.ObjectPool;
import com.bknife.orm.assemble.SqlGetter;

public class SqlGetterField implements SqlGetter, ObjectPool.LifeSpan {
    private Field field;

    @Override
    public Object getValue(Object object) throws Exception {
        return field.get(object);
    }

    @Override
    public void init(Object... params) throws Exception {
        field = (Field) params[0];
        field.setAccessible(true);
    }

    @Override
    public void release() {
        field = null;
    }
}
