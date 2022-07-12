package com.bknife.orm.assemble.getter;

import java.util.Map;

import com.bknife.orm.assemble.SqlGetter;

public class SqlGetterMap implements SqlGetter {
    private String name;

    public SqlGetterMap(String name) {
        this.name = name;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object getValue(Object object) throws Exception {
        Map<String, Object> map = (Map<String, Object>) object;
        return map.get(name);
    }
}
