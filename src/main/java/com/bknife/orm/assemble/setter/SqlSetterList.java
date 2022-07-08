package com.bknife.orm.assemble.setter;

import java.util.List;

import com.bknife.orm.assemble.SqlSetter;

public class SqlSetterList implements SqlSetter {

    @SuppressWarnings("unchecked")
    @Override
    public void setValue(Object object, Object value) throws Exception {
        List<Object> values = (List<Object>) object;
        values.add(value);
    }
}
