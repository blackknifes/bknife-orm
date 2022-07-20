package com.bknife.orm.assemble.exception;

public class SqlIllegalNameException extends Exception {

    public static SqlIllegalNameException throwIllegalTableName(String tableName) throws SqlIllegalNameException {
        throw new SqlIllegalNameException("illegal table name: " + tableName);
    }

    public static SqlIllegalNameException throwIllegalClassName(String className) throws SqlIllegalNameException {
        throw new SqlIllegalNameException("illegal class name: " + className);
    }

    public static SqlIllegalNameException throwIllegalFieldName(String fieldName) throws SqlIllegalNameException {
        throw new SqlIllegalNameException("illegal field name: " + fieldName);
    }

    public static SqlIllegalNameException throwIllegalColumnName(String columnName) throws SqlIllegalNameException {
        throw new SqlIllegalNameException("illegal column name: " + columnName);
    }

    public SqlIllegalNameException() {
    }

    public SqlIllegalNameException(String message) {
        super(message);
    }
}
