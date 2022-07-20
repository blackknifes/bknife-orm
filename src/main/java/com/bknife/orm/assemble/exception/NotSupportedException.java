package com.bknife.orm.assemble.exception;

import com.bknife.orm.annotion.Column.Type;

public class NotSupportedException extends Exception {

    public static void throwNotSupportedClass(Class<?> clazz) throws NotSupportedException {
        throw new NotSupportedException("not supported class: " + clazz);
    }

    public static void throwNotSupportedColumnType(Type type) throws NotSupportedException {
        throw new NotSupportedException("not supported column type: " + type);
    }

    public NotSupportedException() {
        super("not supported");
    }

    public NotSupportedException(String message) {
        super("not supported: " + message);
    }
}
