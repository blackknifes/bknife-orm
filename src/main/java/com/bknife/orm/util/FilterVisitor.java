package com.bknife.orm.util;

public interface FilterVisitor<T> {
    public boolean filter(T object);
}
