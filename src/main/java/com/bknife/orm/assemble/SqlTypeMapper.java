package com.bknife.orm.assemble;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.bknife.orm.annotion.Column;

public class SqlTypeMapper {
    private static Map<Column.Type, Class<?>> sqlTypeToClass = new HashMap<Column.Type, Class<?>>();
    private static Map<Class<?>, Column.Type> classToSqlType = new HashMap<Class<?>, Column.Type>();

    static {
        sqlTypeToClass.put(Column.Type.BYTE, Byte.class);
        sqlTypeToClass.put(Column.Type.INTEGER, Integer.class);
        sqlTypeToClass.put(Column.Type.LONG, Long.class);
        sqlTypeToClass.put(Column.Type.DOUBLE, Double.class);
        sqlTypeToClass.put(Column.Type.STRING, String.class);
        sqlTypeToClass.put(Column.Type.CHARS, String.class);
        sqlTypeToClass.put(Column.Type.DECIMAL, BigDecimal.class);
        sqlTypeToClass.put(Column.Type.DATE, java.sql.Date.class);
        sqlTypeToClass.put(Column.Type.TIME, java.sql.Time.class);
        sqlTypeToClass.put(Column.Type.TIMESTAMP, java.sql.Timestamp.class);
        sqlTypeToClass.put(Column.Type.BINARY, java.sql.Blob.class);

        classToSqlType.put(Boolean.class, Column.Type.BYTE);
        classToSqlType.put(Byte.class, Column.Type.BYTE);
        classToSqlType.put(Short.class, Column.Type.INTEGER);
        classToSqlType.put(Integer.class, Column.Type.INTEGER);
        classToSqlType.put(Long.class, Column.Type.LONG);
        classToSqlType.put(Double.class, Column.Type.DOUBLE);
        classToSqlType.put(String.class, Column.Type.STRING);
        classToSqlType.put(BigDecimal.class, Column.Type.DECIMAL);
        classToSqlType.put(Date.class, Column.Type.DATE);
        classToSqlType.put(java.sql.Date.class, Column.Type.DATE);
        classToSqlType.put(java.sql.Time.class, Column.Type.TIME);
        classToSqlType.put(java.sql.Timestamp.class, Column.Type.TIMESTAMP);
        classToSqlType.put(byte[].class, Column.Type.BINARY);
        classToSqlType.put(java.sql.Blob.class, Column.Type.BINARY);
    }

    public static Column.Type getSqlType(Class<?> clazz)
    {
        return classToSqlType.get(clazz);
    }

    public static Class<?> getClassType(Column.Type type)
    {
        return sqlTypeToClass.get(type);
    }
}
