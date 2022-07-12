package com.bknife.orm.assemble.exception;

public class NotSupportedException extends Exception {

    public NotSupportedException() {
        super("not supported");
    }

    public NotSupportedException(String message) {
        super("not supported: " + message);
    }
}
