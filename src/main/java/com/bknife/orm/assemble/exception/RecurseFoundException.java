package com.bknife.orm.assemble.exception;

public class RecurseFoundException extends Exception {

    public RecurseFoundException() {
        super("recurse found");
    }

    public RecurseFoundException(String message) {
        super(message);
    }

}
