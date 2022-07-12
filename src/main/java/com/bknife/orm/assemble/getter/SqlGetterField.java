package com.bknife.orm.assemble.getter;

import java.lang.reflect.Field;

import com.bknife.orm.assemble.SqlGetter;

public class SqlGetterField implements SqlGetter {
    private Field field;

    public SqlGetterField(Field field) {
        this.field = field;
    }

    @Override
    public Object getValue(Object object) throws Exception {
        return field.get(object);
    }
}
