package com.bknife.orm.util;

import java.util.Iterator;

public class ArrayIterator<T> implements Iterator<T> {
    private T[] array;
    private int index = 0;

    public ArrayIterator(T[] array) {
        this.array = array;
    }

    @Override
    public boolean hasNext() {
        return array != null && index < array.length;
    }

    @Override
    public T next() {
        return array[index++];
    }
}
