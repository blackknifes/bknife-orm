package com.bknife.orm.util;

public class ClassFilterVisitor implements FilterVisitor<Object> {
    private Class<?> superClass;

    public ClassFilterVisitor(Class<?> superClass) {
        this.superClass = superClass;
    }

    @Override
    public boolean filter(Object object) {
        return object != null && superClass.isAssignableFrom(object.getClass());
    }

}
