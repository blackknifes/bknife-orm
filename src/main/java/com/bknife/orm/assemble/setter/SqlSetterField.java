package com.bknife.orm.assemble.setter;

import java.lang.reflect.Field;

import com.bknife.base.ObjectPool;
import com.bknife.orm.assemble.SqlSetter;

public class SqlSetterField implements SqlSetter, ObjectPool.LifeSpan {
    private Field field;

    @Override
    public void setValue(Object object, Object value) throws Exception {
        field.set(object, value);
    }

    @Override
    public void init(Object... params) throws Exception {
        field = (Field) params[0];
    }

    @Override
    public void release() {
        field = null;
    }

}
