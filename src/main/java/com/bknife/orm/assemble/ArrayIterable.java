package com.bknife.orm.assemble;

import java.util.Iterator;

/**
 * 数组迭代对象
 * 
 * @author bknife
 * @version 2022-07-20
 */
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
