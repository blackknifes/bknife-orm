package com.bknife.orm.assemble.impl.mysql;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.bknife.orm.annotion.Column;
import com.bknife.orm.annotion.Column.Type;
import com.bknife.orm.assemble.SqlConfig;
import com.bknife.orm.assemble.exception.NotSupportedException;

public class SqlConfigMysql implements SqlConfig {
    private static final Map<Class<?>, Column.Type> typeMap = new HashMap<>();

    static {
        typeMap.put(boolean.class, Type.BYTE);
        typeMap.put(Boolean.class, Type.BYTE);
        typeMap.put(byte.class, Type.BYTE);
        typeMap.put(Byte.class, Type.BYTE);
        typeMap.put(short.class, Type.INTEGER);
        typeMap.put(Short.class, Type.INTEGER);
        typeMap.put(int.class, Type.INTEGER);
        typeMap.put(Integer.class, Type.INTEGER);
        typeMap.put(long.class, Type.LONG);
        typeMap.put(Long.class, Type.LONG);
        typeMap.put(float.class, Type.DOUBLE);
        typeMap.put(Float.class, Type.DOUBLE);
        typeMap.put(double.class, Type.DOUBLE);
        typeMap.put(Double.class, Type.DOUBLE);
        typeMap.put(String.class, Type.STRING);
        typeMap.put(BigDecimal.class, Type.DECIMAL);
        typeMap.put(java.util.Date.class, Type.DATE);
        typeMap.put(java.sql.Date.class, Type.DATE);
        typeMap.put(java.sql.Time.class, Type.TIME);
        typeMap.put(java.sql.Timestamp.class, Type.TIMESTAMP);
        typeMap.put(byte[].class, Type.BINARY);
    }

    @Override
    public String getSqlType(Type type) throws NotSupportedException {
        switch (type) {
            case BYTE:
                return "TINYINT";
            case INTEGER:
                return "INT";
            case LONG:
                return "BIGINT";
            case DOUBLE:
                return "DOUBLE";
            case STRING:
                return "VARCHAR";
            case CHARS:
                return "CHAR";
            case DECIMAL:
                return "DECIMAL";
            case DATE:
                return "DATE";
            case TIME:
                return "TIME";
            case TIMESTAMP:
                return "TIMESTAMP";
            case BINARY:
                return "VARBINARY";
            default:
                break;
        }
        throw new NotSupportedException("not supported column type: " + type);
    }

    @Override
    public Type getColumnType(Class<?> clazz) throws NotSupportedException {
        Type type = typeMap.get(clazz);
        if (type == null)
            NotSupportedException.throwNotSupportedClass(clazz);
        return type;
    }

    @Override
    public String getTableCharset() {
        return "utf8mb4";
    }

    @Override
    public String getTableCollate() {
        return "utf8mb4_unicode_ci";
    }

    @Override
    public int getLength(Type type) {
        switch (type) {
            case CHARS:
                return 32;
            case STRING:
                return 32;
            case BINARY:
                return 4096;
            case DECIMAL:
                return 16;
            default:
                break;
        }
        return 0;
    }

    @Override
    public int getDot(Type type) {
        switch (type) {
            case DECIMAL:
                return 6;
            default:
                break;
        }
        return 0;
    }
}
