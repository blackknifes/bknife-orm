package com.bknife.orm.assemble;

public final class SqlNameUtils {
    /**
     * 类名转表名
     * 
     * @param className 类名
     * @return
     */
    public static String classToTableName(String className) {
        if (className.isEmpty())
            return "";

        StringBuffer buffer = new StringBuffer();
        buffer.append(Character.toLowerCase(className.charAt(0)));
        for (int i = 1; i < className.length(); i++) {
            char ch = className.charAt(i);
            if (Character.isUpperCase(ch))
                buffer.append("_").append(Character.toLowerCase(ch));
            else
                buffer.append(ch);
        }
        return buffer.toString();
    }

    /**
     * 表名转类名
     * 
     * @param tableName 表名
     * @return
     */
    public static String tableToClassName(String tableName) {
        if (tableName.isEmpty())
            return "";
        StringBuffer buffer = new StringBuffer();
        boolean nextUpper = true;
        for (int i = 0; i < tableName.length(); i++) {
            char ch = tableName.charAt(i);
            if (nextUpper) {
                buffer.append(Character.toUpperCase(ch));
                nextUpper = false;
            } else if (ch == '_')
                nextUpper = true;
            else
                buffer.append(ch);
        }
        return buffer.toString();
    }

    /**
     * 字段名转列名
     * 
     * @param fieldName 字段名
     * @return
     */
    public static String fieldToColumnName(String fieldName) {
        if (fieldName.isEmpty())
            return "";

        StringBuffer buffer = new StringBuffer();
        buffer.append(fieldName.charAt(0));
        for (int i = 1; i < fieldName.length(); i++) {
            char ch = fieldName.charAt(i);
            if (Character.isUpperCase(ch))
                buffer.append("_").append(Character.toLowerCase(ch));
            else
                buffer.append(ch);
        }
        return buffer.toString();
    }

    /**
     * 列名转字段名
     * 
     * @param columnName 列名
     * @return
     */
    public static String columnToFieldName(String columnName) {
        if (columnName.isEmpty())
            return "";
        StringBuffer buffer = new StringBuffer();
        buffer.append(Character.toLowerCase(columnName.charAt(0)));

        boolean nextUpper = false;
        for (int i = 1; i < columnName.length(); i++) {
            char ch = columnName.charAt(i);
            if (nextUpper) {
                buffer.append(Character.toUpperCase(ch));
                nextUpper = false;
            } else if (ch == '_')
                nextUpper = true;
            else
                buffer.append(ch);
        }
        return buffer.toString();
    }
}
