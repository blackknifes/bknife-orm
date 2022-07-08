package com.bknife.orm.assemble.getter;

import com.bknife.base.ObjectPool;
import com.bknife.orm.assemble.SqlGetter;

public class SqlGetterValue implements SqlGetter, ObjectPool.LifeSpan {
    private Object value;

    @Override
    public Object getValue(Object object) throws Exception {
        return value;
    }

    @Override
    public void init(Object... params) throws Exception {
        value = params[0];
    }

    @Override
    public void release() {
        value = null;
    }

}
