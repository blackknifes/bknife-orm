package com.bknife.orm.assemble.setter;

import java.lang.reflect.Field;

import com.bknife.base.converter.ConverterUtils;
import com.bknife.orm.assemble.SqlSetter;

public class SqlSetterField implements SqlSetter {
    private Field field;

    public SqlSetterField(Field field) {
        this.field = field;
        field.setAccessible(true);
    }

    @Override
    public void setValue(Object object, Object value) throws Exception {
        field.set(object, ConverterUtils.convert(value, field.getType()));
    }

}
