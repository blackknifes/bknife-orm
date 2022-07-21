package com.bknife.orm.assemble;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SqlBuffer {
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private StringBuffer buffer = new StringBuffer();

    @Override
    public String toString() {
        return buffer.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((buffer == null) ? 0 : hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SqlBuffer other = (SqlBuffer) obj;
        if (buffer == null) {
            if (other.buffer != null)
                return false;
        } else if (!buffer.equals(other.buffer))
            return false;
        return true;
    }

    public SqlBuffer append(Object object) {
        buffer.append(object);
        return this;
    }

    public SqlBuffer space() {
        return append(' ');
    }

    public SqlBuffer select() {
        return append("SELECT");
    }

    public SqlBuffer from() {
        return append("FROM");
    }

    public SqlBuffer where() {
        return append("WHERE");
    }

    public SqlBuffer as() {
        return append("AS");
    }

    public SqlBuffer insert() {
        return append("INSERT");
    }

    public SqlBuffer into() {
        return append("INTO");
    }

    public SqlBuffer replace() {
        return append("REPLACE");
    }

    public SqlBuffer delete() {
        return append("DELETE");
    }

    public SqlBuffer update() {
        return append("UPDATE");
    }

    public SqlBuffer set() {
        return append("SET");
    }

    public SqlBuffer concat() {
        return append("CONCAT");
    }

    public SqlBuffer like() {
        return append("LIKE");
    }

    public SqlBuffer like(Object value) {
        return like().openParentheses().stringWrap().percent().append(value).percent()
                .stringWrap().closeParentheses();
    }

    public SqlBuffer leftLike(Object value) {
        return like().openParentheses().stringWrap().percent().append(value).stringWrap()
                .closeParentheses();
    }

    public SqlBuffer rightLike(Object value) {
        return like().openParentheses().stringWrap().append(value).percent().stringWrap()
                .closeParentheses();
    }

    public SqlBuffer regexp() {
        return append("REGEXP");
    }

    public SqlBuffer regexp(Object str) {
        return regexp().parentheses(str);
    }

    public SqlBuffer equal() {
        return append("=");
    }

    public SqlBuffer notEqual() {
        return append("<>");
    }

    public SqlBuffer less() {
        return append("<");
    }

    public SqlBuffer lessEqual() {
        return append("<=");
    }

    public SqlBuffer greater() {
        return append(">");
    }

    public SqlBuffer greaterEqual() {
        return append(">=");
    }

    public SqlBuffer between()
    {
        return append("BETWEEN");
    }

    public SqlBuffer percent() {
        return append('%');
    }

    public SqlBuffer semicolon() {
        return append(';');
    }

    public SqlBuffer question() {
        return append('?');
    }

    public SqlBuffer in() {
        return append("IN");
    }

    public SqlBuffer and() {
        return append("AND");
    }

    public SqlBuffer or() {
        return append("OR");
    }

    public SqlBuffer not() {
        return append("NOT");
    }

    public SqlBuffer isNull() {
        return append("ISNULL");
    }

    public SqlBuffer isNullName(Object name) {
        return append("ISNULL").openParentheses().name(name.toString()).closeParentheses();
    }

    public SqlBuffer isNull(Object name) {
        return append("ISNULL").parentheses(name);
    }

    public SqlBuffer groupBy() {
        return append("GROUP BY");
    }

    public SqlBuffer distinct() {
        return append("DISTINCT");
    }

    public SqlBuffer count() {
        return append("COUNT");
    }

    public SqlBuffer countName(Object name) {
        return append("COUNT").openParentheses().name(name.toString()).closeParentheses();
    }

    public SqlBuffer count(Object name) {
        return append("COUNT").parentheses(name);
    }

    public SqlBuffer sum() {
        return append("SUM");
    }

    public SqlBuffer sum(Object str) {
        return append("SUM").parentheses(str);
    }

    public SqlBuffer sumName(Object name) {
        return append("SUM").openParentheses().name(name.toString()).closeParentheses();
    }

    public SqlBuffer avg() {
        return append("AVG");
    }

    public SqlBuffer avg(Object str) {
        return append("AVG").parentheses(str);
    }

    public SqlBuffer avgName(Object name) {
        return append("AVG").openParentheses().name(name.toString()).closeParentheses();
    }

    public SqlBuffer comma() {
        return append(',');
    }

    public SqlBuffer nameWrap() {
        return append('`');
    }

    public SqlBuffer name(String name) {
        return nameWrap().append(name).nameWrap();
    }

    public SqlBuffer stringWrap() {
        return append('"');
    }

    public SqlBuffer string(Object str) {
        return stringWrap().append(str).stringWrap();
    }

    public SqlBuffer dot() {
        return append('.');
    }

    public SqlBuffer openParentheses() {
        return append('(');
    }

    public SqlBuffer closeParentheses() {
        return append(')');
    }

    public SqlBuffer parentheses(Object str) {
        return openParentheses().append(str).closeParentheses();
    }

    public SqlBuffer orderBy() {
        return append("ORDER BY");
    }

    public SqlBuffer asc() {
        return append("ASC");
    }

    public SqlBuffer desc() {
        return append("DESC");
    }

    public SqlBuffer join() {
        return append("JOIN");
    }

    public SqlBuffer outerJoin() {
        return append("OUTER JOIN");
    }

    public SqlBuffer innerJoin() {
        return append("INNER JOIN");
    }

    public SqlBuffer leftJoin() {
        return append("LEFT JOIN");
    }

    public SqlBuffer rightJoin() {
        return append("RIGHT JOIN");
    }

    public SqlBuffer createTableIfNotExist(String tableName) {
        return append("CREATE TABLE IF NOT EXISTS").space().name(tableName);
    }

    public SqlBuffer nullToken() {
        return append("NULL");
    }

    public SqlBuffer notNull() {
        return append("NOT NULL");
    }

    public SqlBuffer primaryKey() {
        return append("PRIMARY KEY");
    }

    public SqlBuffer index() {
        return append("INDEX");
    }

    public SqlBuffer using() {
        return append("USING");
    }

    public SqlBuffer constraint() {
        return append("CONSTRAINT");
    }

    public SqlBuffer foreignKey() {
        return append("FOREIGN KEY");
    }

    public SqlBuffer references() {
        return append("REFERENCES");
    }

    public SqlBuffer onDelete() {
        return append("ON DELETE");
    }

    public SqlBuffer onUpdate() {
        return append("ON UPDATE");
    }

    public SqlBuffer unsigned() {
        return append("UNSIGNED");
    }

    public SqlBuffer comment() {
        return append("COMMENT");
    }

    public SqlBuffer autoIncrement() {
        return append("AUTO_INCREMENT");
    }

    public SqlBuffer zeroFill() {
        return append("ZEROFILL");
    }

    public SqlBuffer charset() {
        return append("CHARACTER SET");
    }

    public SqlBuffer collate() {
        return append("COLLATE");
    }

    public SqlBuffer engine() {
        return append("ENGINE");
    }

    public SqlBuffer storage() {
        return append("STORAGE");
    }

    public SqlBuffer compression() {
        return append("COMPRESSION");
    }

    public SqlBuffer unique() {
        return append("UNIQUE");
    }

    public SqlBuffer check() {
        return append("CHECK");
    }

    public SqlBuffer defaultToken() {
        return append("DEFAULT");
    }

    public SqlBuffer values() {
        return append("VALUES");
    }

    public SqlBuffer value(Object object) {
        if (object instanceof String)
            return stringWrap().append(object).stringWrap();
        else if (object instanceof Date)
            return stringWrap().append(dateFormat.format((Date) object)).stringWrap();

        return append(object);
    }

    public SqlBuffer removeLast() {
        buffer.deleteCharAt(buffer.length() - 1);
        return this;
    }

    public SqlBuffer fullName(String tableName, String name) {
        name(tableName).dot();
        return name(name);
    }

    public SqlBuffer columnSql(String sql, String name) {
        return parentheses(sql).space().as().space().name(name);
    }
}
