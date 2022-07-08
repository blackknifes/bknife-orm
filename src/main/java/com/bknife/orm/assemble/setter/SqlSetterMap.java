package com.bknife.orm.assemble.setter;

import java.util.Map;

import com.bknife.base.ObjectPool;
import com.bknife.orm.assemble.SqlSetter;

public class SqlSetterMap implements SqlSetter, ObjectPool.LifeSpan {
    private String name;

    @SuppressWarnings("unchecked")
    @Override
    public void setValue(Object object, Object value) throws Exception {
        Map<String, Object> map = (Map<String, Object>) object;
        map.put(name, value);
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
