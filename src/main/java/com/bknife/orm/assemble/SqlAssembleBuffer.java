package com.bknife.orm.assemble;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SqlAssembleBuffer {
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
        SqlAssembleBuffer other = (SqlAssembleBuffer) obj;
        if (buffer == null) {
            if (other.buffer != null)
                return false;
        } else if (!buffer.equals(other.buffer))
            return false;
        return true;
    }

    public SqlAssembleBuffer append(Object object) {
        buffer.append(object);
        return this;
    }

    public SqlAssembleBuffer space() {
        return append(' ');
    }

    public SqlAssembleBuffer select() {
        return append("SELECT");
    }

    public SqlAssembleBuffer from() {
        return append("FROM");
    }

    public SqlAssembleBuffer where() {
        return append("WHERE");
    }

    public SqlAssembleBuffer as() {
        return append("AS");
    }

    public SqlAssembleBuffer insertInto() {
        return append("INSERT INTO");
    }

    public SqlAssembleBuffer into() {
        return append("INTO");
    }

    public SqlAssembleBuffer replaceInto() {
        return append("REPLACE INTO");
    }

    public SqlAssembleBuffer deleteFrom() {
        return append("DELETE FROM");
    }

    public SqlAssembleBuffer update() {
        return append("UPDATE");
    }

    public SqlAssembleBuffer set() {
        return append("SET");
    }

    public SqlAssembleBuffer like() {
        return append("LIKE");
    }

    public SqlAssembleBuffer like(Object value) {
        return like().leftBracket().stringWrap().percent().append(value).percent()
                .stringWrap().rightBracket();
    }

    public SqlAssembleBuffer leftLike(Object value) {
        return like().leftBracket().stringWrap().percent().append(value).stringWrap()
                .rightBracket();
    }

    public SqlAssembleBuffer rightLike(Object value) {
        return like().leftBracket().stringWrap().append(value).percent().stringWrap()
                .rightBracket();
    }

    public SqlAssembleBuffer regexp() {
        return append("REGEXP");
    }

    public SqlAssembleBuffer regexp(Object str) {
        return regexp().bracket(str);
    }

    public SqlAssembleBuffer equal() {
        return append("=");
    }

    public SqlAssembleBuffer notEqual() {
        return append("<>");
    }

    public SqlAssembleBuffer less() {
        return append("<");
    }

    public SqlAssembleBuffer lessEqual() {
        return append("<=");
    }

    public SqlAssembleBuffer greater() {
        return append(">");
    }

    public SqlAssembleBuffer greaterEqual() {
        return append(">=");
    }

    public SqlAssembleBuffer percent() {
        return append('%');
    }

    public SqlAssembleBuffer semicolon() {
        return append(';');
    }

    public SqlAssembleBuffer question() {
        return append('?');
    }

    public SqlAssembleBuffer in() {
        return append("IN");
    }

    public SqlAssembleBuffer and() {
        return append("AND");
    }

    public SqlAssembleBuffer or() {
        return append("OR");
    }

    public SqlAssembleBuffer not() {
        return append("NOT");
    }

    public SqlAssembleBuffer isNull() {
        return append("ISNULL");
    }

    public SqlAssembleBuffer isNullName(Object name) {
        return append("ISNULL").leftBracket().name(name.toString()).rightBracket();
    }

    public SqlAssembleBuffer isNull(Object name) {
        return append("ISNULL").bracket(name);
    }

    public SqlAssembleBuffer groupBy() {
        return append("GROUP BY");
    }

    public SqlAssembleBuffer distinct() {
        return append("DISTINCT");
    }

    public SqlAssembleBuffer count() {
        return append("COUNT");
    }

    public SqlAssembleBuffer countName(Object name) {
        return append("COUNT").leftBracket().name(name.toString()).rightBracket();
    }

    public SqlAssembleBuffer count(Object name) {
        return append("COUNT").bracket(name);
    }

    public SqlAssembleBuffer sum() {
        return append("SUM");
    }

    public SqlAssembleBuffer sum(Object str) {
        return append("SUM").bracket(str);
    }

    public SqlAssembleBuffer sumName(Object name) {
        return append("SUM").leftBracket().name(name.toString()).rightBracket();
    }

    public SqlAssembleBuffer avg() {
        return append("AVG");
    }

    public SqlAssembleBuffer avg(Object str) {
        return append("AVG").bracket(str);
    }

    public SqlAssembleBuffer avgName(Object name) {
        return append("AVG").leftBracket().name(name.toString()).rightBracket();
    }

    public SqlAssembleBuffer comma() {
        return append(',');
    }

    public SqlAssembleBuffer nameWrap() {
        return append('`');
    }

    public SqlAssembleBuffer name(String name) {
        return nameWrap().append(name).nameWrap();
    }

    public SqlAssembleBuffer stringWrap() {
        return append('"');
    }

    public SqlAssembleBuffer string(Object str) {
        return stringWrap().append(str).stringWrap();
    }

    public SqlAssembleBuffer dot() {
        return append('.');
    }

    public SqlAssembleBuffer leftBracket() {
        return append('(');
    }

    public SqlAssembleBuffer rightBracket() {
        return append(')');
    }

    public SqlAssembleBuffer bracket(Object str) {
        return leftBracket().append(str).rightBracket();
    }

    public SqlAssembleBuffer orderBy() {
        return append("ORDER BY");
    }

    public SqlAssembleBuffer asc() {
        return append("ASC");
    }

    public SqlAssembleBuffer desc() {
        return append("DESC");
    }

    public SqlAssembleBuffer join() {
        return append("JOIN");
    }

    public SqlAssembleBuffer outerJoin() {
        return append("OUTER JOIN");
    }

    public SqlAssembleBuffer innerJoin() {
        return append("INNER JOIN");
    }

    public SqlAssembleBuffer leftJoin() {
        return append("LEFT JOIN");
    }

    public SqlAssembleBuffer rightJoin() {
        return append("RIGHT JOIN");
    }

    public SqlAssembleBuffer createTableIfNotExist(String tableName) {
        return append("CREATE TABLE IF NOT EXISTS").name(tableName);
    }

    public SqlAssembleBuffer nullToken() {
        return append("NULL");
    }

    public SqlAssembleBuffer notNull() {
        return append("NOT NULL");
    }

    public SqlAssembleBuffer primaryKey() {
        return append("PRIMARY KEY");
    }

    public SqlAssembleBuffer index() {
        return append("INDEX");
    }

    public SqlAssembleBuffer using() {
        return append("USING");
    }

    public SqlAssembleBuffer constraint() {
        return append("CONSTRAINT");
    }

    public SqlAssembleBuffer foreignKey() {
        return append("FOREIGN KEY");
    }

    public SqlAssembleBuffer references() {
        return append("REFERENCES");
    }

    public SqlAssembleBuffer onDelete() {
        return append("ON DELETE");
    }

    public SqlAssembleBuffer onUpdate() {
        return append("ON UPDATE");
    }

    public SqlAssembleBuffer unsigned() {
        return append("UNSIGNED");
    }

    public SqlAssembleBuffer comment() {
        return append("COMMENT");
    }

    public SqlAssembleBuffer autoIncrement() {
        return append("AUTO_INCREMENT");
    }

    public SqlAssembleBuffer zeroFill() {
        return append("ZEROFILL");
    }

    public SqlAssembleBuffer charset() {
        return append("CHARACTER SET");
    }

    public SqlAssembleBuffer collate() {
        return append("COLLATE");
    }

    public SqlAssembleBuffer engine() {
        return append("ENGINE");
    }

    public SqlAssembleBuffer storage() {
        return append("STORAGE");
    }

    public SqlAssembleBuffer compression() {
        return append("COMPRESSION");
    }

    public SqlAssembleBuffer unique() {
        return append("UNIQUE");
    }

    public SqlAssembleBuffer check() {
        return append("CHECK");
    }

    public SqlAssembleBuffer defaultToken() {
        return append("DEFAULT");
    }

    public SqlAssembleBuffer values() {
        return append("VALUES");
    }

    public SqlAssembleBuffer value(Object object) {
        if (object instanceof String)
            return stringWrap().append(object).stringWrap();
        else if (object instanceof Date)
            return stringWrap().append(dateFormat.format((Date) object)).stringWrap();

        return append(object);
    }

    public SqlAssembleBuffer removeLast() {
        buffer.deleteCharAt(buffer.length() - 1);
        return this;
    }

    public SqlAssembleBuffer fullName(String tableName, String name) {
        name(tableName).dot();
        return name(name);
    }

    public SqlAssembleBuffer columnSql(String sql, String name) {
        return bracket(sql).space().as().space().name(name);
    }
}
