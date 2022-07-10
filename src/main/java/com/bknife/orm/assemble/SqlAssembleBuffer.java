package com.bknife.orm.assemble;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import com.bknife.orm.annotion.Check;
import com.bknife.orm.annotion.Column;
import com.bknife.orm.annotion.ForeignKey;
import com.bknife.orm.annotion.Index;
import com.bknife.orm.annotion.Unique;
import com.bknife.orm.mapper.select.SqlOrderBy;
import com.bknife.orm.mapper.where.Condition;
import com.bknife.orm.mapper.where.SqlWhere;
import com.bknife.orm.mapper.where.SqlWhereBinary;
import com.bknife.orm.mapper.where.SqlWhereIn;
import com.bknife.orm.mapper.where.SqlWhereLogic;
import com.bknife.orm.mapper.where.SqlWhereUnary;

public class SqlAssembleBuffer {
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private StringBuffer buffer = new StringBuffer();

    @Override
    public String toString() {
        return toString();
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
        } else if (!equals(other.buffer))
            return false;
        return true;
    }

    public SqlAssembleBuffer append(Object object) {
        buffer.append(object);
        return this;
    }

    public SqlAssembleBuffer appendSpace() {
        return append(' ');
    }

    public SqlAssembleBuffer appendSelect() {
        return append("SELECT");
    }

    public SqlAssembleBuffer appendFrom() {
        return append("FROM");
    }

    public SqlAssembleBuffer appendWhere() {
        return append("WHERE");
    }

    public SqlAssembleBuffer appendAs() {
        return append("AS");
    }

    public SqlAssembleBuffer appendInsertInto() {
        return append("INSERT INTO");
    }

    public SqlAssembleBuffer appendReplaceInto() {
        return append("REPLACE INTO");
    }

    public SqlAssembleBuffer appendDeleteFrom() {
        return append("DELETE FROM");
    }

    public SqlAssembleBuffer appendUpdate() {
        return append("UPDATE");
    }

    public SqlAssembleBuffer appendSet() {
        return append("SET");
    }

    public SqlAssembleBuffer appendLike() {
        return append("LIKE");
    }

    public SqlAssembleBuffer appendLike(Object value) {
        return appendLike().appendLeftBracket().appendStringWrap().appendPercent().append(value).appendPercent()
                .appendStringWrap().appendRightBracket();
    }

    public SqlAssembleBuffer appendLeftLike(Object value) {
        return appendLike().appendLeftBracket().appendStringWrap().appendPercent().append(value).appendStringWrap()
                .appendRightBracket();
    }

    public SqlAssembleBuffer appendRightLike(Object value) {
        return appendLike().appendLeftBracket().appendStringWrap().append(value).appendPercent().appendStringWrap()
                .appendRightBracket();
    }

    public SqlAssembleBuffer appendRegexp() {
        return append("REGEXP");
    }

    public SqlAssembleBuffer appendRegexp(String str) {
        return appendRegexp().appendLeftBracket().appendString(str).appendRightBracket();
    }

    public SqlAssembleBuffer appendEqual() {
        return append("=");
    }

    public SqlAssembleBuffer appendNotEqual() {
        return append("<>");
    }

    public SqlAssembleBuffer appendLess() {
        return append("<");
    }

    public SqlAssembleBuffer appendLessEqual() {
        return append("<=");
    }

    public SqlAssembleBuffer appendGreater() {
        return append(">");
    }

    public SqlAssembleBuffer appendGreaterEqual() {
        return append(">=");
    }

    public SqlAssembleBuffer appendPercent() {
        return append('%');
    }

    public SqlAssembleBuffer appendIn() {
        return append("IN");
    }

    public SqlAssembleBuffer appendAnd() {
        return append("AND");
    }

    public SqlAssembleBuffer appendOr() {
        return append("OR");
    }

    public SqlAssembleBuffer appendNot() {
        return append("NOT");
    }

    public SqlAssembleBuffer appendIsNull() {
        return append("ISNULL");
    }

    public SqlAssembleBuffer appendGroupBy() {
        return append("GROUP BY");
    }

    public SqlAssembleBuffer appendDistinct() {
        return append("DISTINCT");
    }

    public SqlAssembleBuffer appendCount() {
        return append("COUNT");
    }

    public SqlAssembleBuffer appendSum() {
        return append("SUM");
    }

    public SqlAssembleBuffer appendAvg() {
        return append("AVG");
    }

    public SqlAssembleBuffer appendComma() {
        return append(',');
    }

    public SqlAssembleBuffer appendNameWrap() {
        return append('`');
    }

    public SqlAssembleBuffer appendName(String name) {
        return append('`').append(name).append('`');
    }

    public SqlAssembleBuffer appendStringWrap() {
        return append('"');
    }

    public SqlAssembleBuffer appendString(String str) {
        return appendStringWrap().append(str).appendStringWrap();
    }

    public SqlAssembleBuffer appendDot() {
        return append('.');
    }

    public SqlAssembleBuffer appendLeftBracket() {
        return append('(');
    }

    public SqlAssembleBuffer appendRightBracket() {
        return append(')');
    }

    public SqlAssembleBuffer appendOrderBy() {
        return append("ORDER BY");
    }

    public SqlAssembleBuffer appendAsc() {
        return append("ASC");
    }

    public SqlAssembleBuffer appendDesc() {
        return append("DESC");
    }

    public SqlAssembleBuffer appendJoin() {
        return append("JOIN");
    }

    public SqlAssembleBuffer appendOuterJoin() {
        return append("OUTER JOIN");
    }

    public SqlAssembleBuffer appendInnerJoin() {
        return append("INNER JOIN");
    }

    public SqlAssembleBuffer appendLeftJoin() {
        return append("LEFT JOIN");
    }

    public SqlAssembleBuffer appendRightJoin() {
        return append("RIGHT JOIN");
    }

    public SqlAssembleBuffer appendCreateTableIfNotExist() {
        return append("CREATE TABLE IF NOT EXISTS");
    }

    public SqlAssembleBuffer appendType(Column.Type type, Class<?> fieldType) throws Exception {
        switch (type) {
            case AUTO:
                if (fieldType == null)
                    throw new IllegalArgumentException("field type is not supported: " + fieldType);
                return appendType(SqlTypeMapper.getSqlType(fieldType), null);
            case BYTE:
                return append("TINYINT");
            case INTEGER:
                return append("INT");
            case LONG:
                return append("LONG");
            case DOUBLE:
                return append("DOUBLE");
            case STRING:
                return append("STRING");
            case CHARS:
                return append("CHAR");
            case DECIMAL:
                return append("DECIMAL");
            case DATE:
                return append("DATE");
            case TIME:
                return append("TIME");
            case TIMESTAMP:
                return append("TIMESTAMP");
            case BINARY:
                return append("VARBINARY");
            default:
                throw new IllegalArgumentException("type is not supported: " + type);
        }
    }

    public SqlAssembleBuffer appendPrimaryKey() {
        return append("PRIMARY KEY");
    }

    public SqlAssembleBuffer appendIndex() {
        return append("INDEX");
    }

    public SqlAssembleBuffer appendUsing() {
        return append("USING");
    }

    public SqlAssembleBuffer appendIndexType(Index.IndexType type) {
        switch (type) {
            case NORMAL:
                return this;
            case FULLTEXT:
                return append("FULLTEXT");
            case SPATIAL:
                return append("SPATIAL");
            case UNIQUE:
                return append("UNIQUE");
            default:
                throw new IllegalArgumentException("index type is not supported: " + type);
        }
    }

    public SqlAssembleBuffer appendIndexMethod(Index.IndexMethod method) {
        switch (method) {
            case BTREE:
                return append("BTREE");
            case HASH:
                return append("HASH");
            default:
                throw new IllegalArgumentException("index method is not supported: " + method);
        }
    }

    public SqlAssembleBuffer appendConstraint() {
        return append("CONSTRAINT");
    }

    public SqlAssembleBuffer appendForeignKey() {
        return append("FOREIGN KEY");
    }

    public SqlAssembleBuffer appendReferences() {
        return append("REFERENCES");
    }

    public SqlAssembleBuffer appendOnDelete() {
        return append("ON DELETE");
    }

    public SqlAssembleBuffer appendOnUpdate() {
        return append("ON UPDATE");
    }

    public SqlAssembleBuffer appendForeignKeyAction(ForeignKey.ForeignKeyAction action) {
        switch (action) {
            case CASCADE:
                return append("CASCADE");
            case NO_ACTION:
                return append("NO ACTION");
            case RESTRICT:
                return append("RESTRICT");
            case SET_NULL:
                return append("SET NULL");
            default:
                throw new IllegalArgumentException("Foreign Key Action is not supported: " + action);
        }
    }

    public SqlAssembleBuffer appendUnsigned() {
        return append("UNSIGNED");
    }

    public SqlAssembleBuffer appendComment() {
        return append("COMMENT");
    }

    public SqlAssembleBuffer appendAutoIncrement() {
        return append("AUTO_INCREMENT");
    }

    public SqlAssembleBuffer appendZeroFill() {
        return append("ZEROFILL");
    }

    public SqlAssembleBuffer appendCharset() {
        return append("CHARACTER SET");
    }

    public SqlAssembleBuffer appendCollate() {
        return append("COLLATE");
    }

    public SqlAssembleBuffer appendEngine() {
        return append("ENGINE");
    }

    public SqlAssembleBuffer appendStorage() {
        return append("STORAGE");
    }

    public SqlAssembleBuffer appendCompression() {
        return append("COMPRESSION");
    }

    public SqlAssembleBuffer appendUnique() {
        return append("UNIQUE");
    }

    public SqlAssembleBuffer appendCheck() {
        return append("CHECK");
    }

    public SqlAssembleBuffer appendValue(Object object) {
        if (object instanceof String)
            return appendStringWrap().append(object).appendStringWrap();
        else if (object instanceof Date)
            return appendStringWrap().append(dateFormat.format((Date) object)).appendStringWrap();

        return append(object);
    }

    public SqlAssembleBuffer removeLast() {
        buffer.deleteCharAt(buffer.length() - 1);
        return this;
    }

    public SqlAssembleBuffer appendTableName(String name) {
        return appendNameWrap().append(name).appendNameWrap();
    }

    public SqlAssembleBuffer appendColumnName(String name) {
        return appendNameWrap().append(name).appendNameWrap();
    }

    public SqlAssembleBuffer appendColumnName(SqlMapperInfo mapperInfo, String name) {
        if (mapperInfo instanceof SqlTableInfo) {
            SqlColumnInfo columnInfo = ((SqlTableInfo) mapperInfo).getColumn(name);
            return append(columnInfo.getSqlName());
        }
        return appendColumnName(name);
    }

    public SqlAssembleBuffer appendColumnFullName(String tableName, String name) {
        appendNameWrap().append(tableName).appendNameWrap();
        appendDot();
        return appendNameWrap().append(name).appendNameWrap();
    }

    public SqlAssembleBuffer appendColumnFullName(SqlMapperInfo mapperInfo, String name) {
        return appendColumnFullName(mapperInfo.getName(), name);
    }

    public SqlAssembleBuffer appendColumnSql(String sql, String name) {
        appendLeftBracket().append(sql).appendRightBracket();
        appendSpace().appendAs().appendSpace();
        return appendNameWrap().append(name).appendNameWrap();
    }

    public SqlAssembleBuffer appendWheres(SqlTableInfo tableInfo, Collection<SqlWhere> wheres) throws Exception {
        for (SqlWhere where : wheres)
            return appendWhere(tableInfo, where).appendSpace();
        if (!wheres.isEmpty())
            removeLast();
        return this;
    }

    public SqlAssembleBuffer appendWhere(SqlTableInfo tableInfo, SqlWhere where) throws Exception {
        switch (where.getWhereType()) {
            case LOGIC: {
                SqlWhereLogic logic = (SqlWhereLogic) where;
                switch (logic.getLogicType()) {
                    case AND:
                        appendAnd().appendSpace();
                        break;
                    case OR:
                        appendOr().appendSpace();
                        break;
                    case NOT:
                        appendNot().appendSpace();
                        break;
                    default:
                        throw new IllegalArgumentException("logic where type is error type: " + logic.getLogicType());
                }
                appendWhere(tableInfo, logic.getWhere());
                break;
            }
            case UNARY: {
                SqlWhereUnary unary = (SqlWhereUnary) where;
                switch (unary.getUnaryType()) {
                    case IS_NULL:
                        appendIsNull().appendLeftBracket();
                        appendColumnFullName(tableInfo, unary.getColumn());
                        appendRightBracket();
                        break;
                    default:
                        throw new IllegalArgumentException("unary where type is error type: " + unary.getUnaryType());
                }
                break;
            }
            case BINARY: {
                SqlWhereBinary binary = (SqlWhereBinary) where;
                appendColumnFullName(tableInfo, binary.getColumn());
                switch (binary.getBinaryType()) {
                    case EQUAL:
                        appendEqual();
                        break;
                    case NOT_EQUAL:
                        appendNotEqual();
                        break;
                    case GREATER:
                        appendGreater();
                        break;
                    case GREATER_EQUAL:
                        appendGreaterEqual();
                        break;
                    case LESS:
                        appendLess();
                        break;
                    case LESS_EQUAL:
                        appendLessEqual();
                        break;
                    case LIKE_MATCH:
                        appendRegexp().appendLeftBracket();
                        appendValue(binary.getValue());
                        appendRightBracket();
                        return this;
                    case LIKE:
                        appendSpace().appendLike().appendLeftBracket().appendPercent().appendStringWrap();
                        append(binary.getValue());
                        appendStringWrap().appendPercent().appendRightBracket();
                        return this;
                    case LEFT_LIKE:
                        appendSpace().appendLike().appendLeftBracket().appendPercent().appendStringWrap();
                        append(binary.getValue());
                        appendStringWrap().appendRightBracket();
                        return this;
                    case RIGHT_LIKE:
                        appendSpace().appendLike().appendLeftBracket().appendStringWrap();
                        append(binary.getValue());
                        appendStringWrap().appendPercent().appendRightBracket();
                        return this;
                    default:
                        throw new IllegalArgumentException(
                                "binary where type is error type: " + binary.getBinaryType());
                }
                appendValue(binary.getValue());
                break;
            }
            case IN: {
                SqlWhereIn in = (SqlWhereIn) where;
                appendColumnFullName(tableInfo, in.getColumn());
                appendSpace().appendIn().appendLeftBracket();
                if (in.getValues().isEmpty())
                    throw new IllegalArgumentException("values of sql where in cannot be empty");
                for (Object value : in.getValues())
                    appendValue(value).appendComma();
                removeLast();
                appendRightBracket();
                break;
            }
            case CONDITION:
                appendWhere(tableInfo, (Condition) where);
                break;
            default:
                break;
        }

        return this;
    }

    public SqlAssembleBuffer appendOrderBy(SqlMapperInfo mapperInfo, Collection<SqlOrderBy> orderBys) {
        if (orderBys.isEmpty())
            return this;

        appendSpace().appendOrderBy().appendSpace();
        for (SqlOrderBy orderBy : orderBys) {
            appendColumnFullName(mapperInfo, orderBy.getColumn()).appendSpace();
            if (orderBy.isAsc())
                appendAsc();
            else
                appendDesc();
            appendComma();
        }
        removeLast();
        return this;
    }

    public SqlAssembleBuffer appendPrimaryKeys(SqlMapperInfo mapperInfo, Collection<SqlColumnInfo> primaryKeys) {
        if (primaryKeys.isEmpty())
            return this;

        appendConstraint().appendSpace();
        appendNameWrap();
        append("pk_").append(mapperInfo.getName()).append("_");
        for (SqlColumnInfo primaryKey : primaryKeys)
            append(primaryKey.getName()).append("_");
        removeLast();
        appendNameWrap();

        appendSpace().appendPrimaryKey();
        appendLeftBracket();

        for (SqlColumnInfo primaryKey : primaryKeys) {
            append(primaryKey.getSqlName()).appendComma();
        }
        removeLast();
        appendRightBracket();
        appendComma();
        return this;
    }

    public SqlAssembleBuffer appendForeignKey(SqlAssembleFactory factory, SqlMapperInfo mapperInfo,
            ForeignKey foreignKey) throws Exception {
        appendSpace().appendConstraint().appendSpace().appendNameWrap();
        append("fk_").append(mapperInfo.getName()).append("_");
        for (String source : foreignKey.sources())
            append(source).append("_");
        for (String source : foreignKey.sources())
            append(source).append("_");
        removeLast();
        appendNameWrap();
        appendSpace().appendForeignKey().appendSpace();
        appendLeftBracket();
        for (String source : foreignKey.sources())
            appendName(source).appendComma();
        removeLast();
        appendRightBracket();

        appendSpace().appendReferences().appendSpace();
        if (foreignKey.tableClass() != Object.class)
            appendName(factory.getMapperInfo(foreignKey.tableClass()).getName());
        else
            appendName(foreignKey.table());

        appendSpace().appendLeftBracket();
        for (String target : foreignKey.targets())
            appendName(target).appendComma();
        removeLast();
        appendRightBracket();
        return this;
    }

    public SqlAssembleBuffer appendForeignKeys(SqlAssembleFactory factory, SqlMapperInfo mapperInfo,
            Collection<ForeignKey> foreignKeys) throws Exception {
        if (foreignKeys.isEmpty())
            return this;

        for (ForeignKey foreignKey : foreignKeys)
            appendForeignKey(factory, mapperInfo, foreignKey).appendComma();
        removeLast();

        return this;
    }

    public SqlAssembleBuffer appendIndex(SqlMapperInfo mapperInfo, Index index) {
        appendIndexType(index.type()).appendSpace().appendIndex().appendSpace();
        appendNameWrap();
        append("index_").append(mapperInfo.getName()).append("_");
        for (String name : index.keys())
            append(name).append("_");
        removeLast();
        appendNameWrap();
        appendSpace().appendUsing().appendSpace().appendIndexMethod(index.method());
        if (!index.comment().isEmpty())
            appendSpace().appendComment().appendSpace().appendString(index.comment());
        return this;
    }

    public SqlAssembleBuffer appendUnique(SqlMapperInfo mapperInfo, Unique unique) {
        if (unique.value().length == 0)
            throw new IllegalArgumentException("unique keys cannot be empty");

        appendConstraint().appendSpace();

        appendNameWrap();
        append("unique_");
        for (String key : unique.value())
            append(key).append("_");
        removeLast();
        appendNameWrap();

        appendSpace().appendUnique().appendSpace().appendLeftBracket();
        for (String key : unique.value())
            appendName(key).appendComma();
        removeLast();
        appendRightBracket();
        return this;
    }

    public SqlAssembleBuffer appendCheck(SqlMapperInfo mapperInfo, SqlColumnInfo columnInfo, Check check) {
        appendConstraint().appendSpace();
        appendNameWrap();
        append("check_").append(check.value());
        appendNameWrap();
        appendSpace().appendCheck().appendSpace();
        appendLeftBracket();
        appendName(columnInfo.getName());
        switch (check.op()) {
            case NotNull:
                appendNot().appendSpace();
                appendValue(check.value());
            case IsNull:
                appendIsNull();
                appendValue(check.value());
                break;
            case Equal:
                appendEqual();
                appendValue(check.value());
                break;
            case NotEqual:
                appendNotEqual();
                appendValue(check.value());
                break;
            case Less:
                appendLess();
                appendValue(check.value());
                break;
            case LessEqual:
                appendLessEqual();
                appendValue(check.value());
                break;
            case Greater:
                appendGreater();
                appendValue(check.value());
                break;
            case GreaterEqual:
                appendGreaterEqual();
                appendValue(check.value());
                break;
            case Like:
                appendLike(check.value());
                break;
            case LeftLike:
                appendLeftLike(check.value());
                break;
            case RightLike:
                appendRightLike(check.value());
                break;
            case Regexp:
                appendRegexp(check.value());
                break;
            default:
                break;
        }
        appendRightBracket();
        return this;
    }
}
