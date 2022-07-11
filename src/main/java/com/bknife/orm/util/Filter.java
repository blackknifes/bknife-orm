package com.bknife.orm.util;

import java.util.Collection;

/**
 * 过滤容器
 */
public class Filter<T> implements Iterable<T> {
    public static class Iterator<T> implements java.util.Iterator<T> {
        private java.util.Iterator<T> iterator;
        private FilterVisitor<T> filter;
        private T next;

        private T moveNext() {
            T oldNext = next;
            while (iterator.hasNext()) {
                T object = iterator.next();
                if (filter.filter(object)) {
                    next = object;
                    break;
                }
            }
            if (!iterator.hasNext())
                next = null;
            return oldNext;
        }

        public Iterator(java.util.Iterator<T> iterator, FilterVisitor<T> filter) {
            this.iterator = iterator;
            this.filter = filter;
            moveNext();
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public T next() {
            if (!hasNext())
                return null;
            return moveNext();
        }

    }

    private Collection<T> collection;
    private FilterVisitor<T> filter;

    public Filter(Collection<T> collection, FilterVisitor<T> filter) {
        this.collection = collection;
        this.filter = filter;
    }

    @Override
    public java.util.Iterator<T> iterator() {
        return new Iterator<T>(collection.iterator(), filter);
    }
}
