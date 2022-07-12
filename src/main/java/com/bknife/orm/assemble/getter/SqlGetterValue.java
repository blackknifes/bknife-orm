package com.bknife.orm.assemble.getter;

import com.bknife.orm.assemble.SqlGetter;

public class SqlGetterValue implements SqlGetter {
    private Object value;

    public SqlGetterValue(Object value) {
        this.value = value;
    }

    @Override
    public Object getValue(Object object) throws Exception {
        return value;
    }
}
