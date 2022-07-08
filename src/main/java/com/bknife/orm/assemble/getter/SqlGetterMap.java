package com.bknife.orm.assemble.getter;

import java.util.Map;

import com.bknife.base.ObjectPool;
import com.bknife.orm.assemble.SqlGetter;

public class SqlGetterMap implements SqlGetter, ObjectPool.LifeSpan {
    private String name;

    @SuppressWarnings("unchecked")
    @Override
    public Object getValue(Object object) throws Exception {
        Map<String, Object> map = (Map<String, Object>) object;
        return map.get(name);
    }

    @Override
    public void init(Object... params) throws Exception {
        name = (String) params[0];
    }

    @Override
    public void release() {
        name = null;
    }

}
