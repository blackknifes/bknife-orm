package com.bknife.orm.assemble.setter;

import java.util.Map;

import com.bknife.orm.assemble.SqlSetter;

public class SqlSetterMap implements SqlSetter {
    private String name;

    public SqlSetterMap(String name) {
        this.name = name;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setValue(Object object, Object value) throws Exception {
        Map<String, Object> map = (Map<String, Object>) object;
        map.put(name, value);
    }
}
