package com.bknife.orm.assemble;

import java.util.Iterator;

class ArrayIterable<T> implements Iterable<T> {
    private T[] array;

    public ArrayIterable(T[] array) {
        this.array = array;
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayIterator<T>(array);
    }
}
