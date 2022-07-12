package com.bknife.orm.assemble;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.bknife.orm.annotion.Column.Type;
import com.bknife.orm.annotion.ForeignKey;
import com.bknife.orm.annotion.Index;
import com.bknife.orm.annotion.Join;
import com.bknife.orm.annotion.Unique;
import com.bknife.orm.assemble.assembled.SqlAssembled;
import com.bknife.orm.assemble.assembled.SqlAssembledImpl;
import com.bknife.orm.assemble.assembled.SqlAssembledQuery;
import com.bknife.orm.assemble.assembled.SqlAssembledQueryImpl;
import com.bknife.orm.assemble.exception.NotSupportedException;
import com.bknife.orm.assemble.getter.SqlGetterField;
import com.bknife.orm.assemble.getter.SqlGetterValue;
import com.bknife.orm.assemble.setter.SqlSetterField;
import com.bknife.orm.assemble.setter.SqlSetterMap;
import com.bknife.orm.mapper.Updater;
import com.bknife.orm.mapper.select.SqlLimit;
import com.bknife.orm.mapper.select.SqlOrderBy;
import com.bknife.orm.mapper.where.Condition;
import com.bknife.orm.mapper.where.SqlWhere;
import com.bknife.orm.mapper.where.SqlWhereBinary;
import com.bknife.orm.mapper.where.SqlWhereIn;
import com.bknife.orm.mapper.where.SqlWhereLogic;
import com.bknife.orm.mapper.where.SqlWhereUnary;

public class SqlAssembleMysql implements SqlAssemble {

    private SqlMapperInfo mapperInfo;
    private SqlContext context;

    private List<SqlSetter> fieldSetters = new ArrayList<SqlSetter>();
    private List<SqlSetter> mapSetters = new ArrayList<SqlSetter>();
    private Collection<SqlGetter> insertGetters = new ArrayList<SqlGetter>();

    public SqlAssembleMysql(SqlContext context, Class<?> clazz) throws Exception {
        this.context = context;
        mapperInfo = context.getMapperInfo(clazz);
        if (mapperInfo == null)
            throw new Exception("class [" + clazz + "] is not a mapper class");
        for (SqlColumnInfo columnInfo : mapperInfo.getColumns()) {
            fieldSetters.add(new SqlSetterField(columnInfo.getField()));
            mapSetters.add(new SqlSetterMap(columnInfo.getField().getName()));
        }

        if (mapperInfo instanceof SqlTableInfo) {
            for (SqlColumnInfo columnInfo : mapperInfo.getColumns())
                insertGetters.add(new SqlGetterField(columnInfo.getField()));
        }
    }

    private static void appendTypeString(SqlAssembleBuffer buffer, Type type, int length, int dot) {
        switch (type) {
            case BYTE:
                buffer.append("TINYINT");
                return;
            case INTEGER:
                buffer.append("INT");
                return;
            case LONG:
                buffer.append("BIGINT");
                return;
            case DOUBLE:
                buffer.append("DOUBLE");
            case STRING:
                if (length == 0)
                    throw new IllegalArgumentException("varchar length is illegal: " + length);
                buffer.append("VARCHAR").bracket(length);
                return;
            case CHARS:
                if (length == 0)
                    throw new IllegalArgumentException("char length is illegal: " + length);
                buffer.append("CHAR").bracket(length);
                return;
            case DECIMAL:
                if (length == 0)
                    throw new IllegalArgumentException("decimal length is illegal: " + length);
                buffer.append("DECIMAL").leftBracket().append(length).comma().append(dot).rightBracket();
                return;
            case DATE:
                buffer.append("DATE");
                return;
            case TIME:
                buffer.append("TIME");
                return;
            case TIMESTAMP:
                buffer.append("TIMESTAMP");
                return;
            case BINARY:
                if (length == 0)
                    throw new IllegalArgumentException("varchar length is illegal: " + length);
                buffer.append("VARBINARY").bracket(length);
                return;
            default:
                break;
        }
        throw new IllegalArgumentException("type is illegal: " + type);
    }

    private static void appendForeignKeyAction(SqlAssembleBuffer buffer, ForeignKey.ForeignKeyAction action)
            throws Exception {
        switch (action) {
            case CASCADE:
                buffer.append("CASCADE");
                break;
            case NO_ACTION:
                buffer.append("NO_ACTION");
                break;
            case RESTRICT:
                buffer.append("RESTRICT");
                break;
            case SET_NULL:
                buffer.append("SET_NULL");
                break;
            default:
                throw new NotSupportedException("not supported foreign key action: " + action);
        }
    }

    @Override
    public Class<?> getResultType() {
        return mapperInfo.getMapperClass();
    }

    @Override
    public SqlAssembled assembleCreateTable() throws Exception {
        if (!(mapperInfo instanceof SqlTableInfo))
            throw new NotSupportedException("only table mapper can call create table");
        SqlAssembleBuffer buffer = new SqlAssembleBuffer();
        SqlTableInfo tableInfo = (SqlTableInfo) mapperInfo;
        buffer.createTableIfNotExist(tableInfo.getName());
        buffer.leftBracket();

        for (SqlColumnInfo columnInfo : tableInfo.getColumns()) {
            SqlTableColumnInfo tableColumn = (SqlTableColumnInfo) columnInfo;
            // 列名
            buffer.name(tableColumn.getName()).space();
            // 类型
            appendTypeString(buffer, tableColumn.getType(), tableColumn.getLength(), tableColumn.getDot());
            buffer.space();
            if (tableColumn.getType() == Type.STRING) {
                // string类型的字符集与排序规则设置
                if (!tableColumn.getCharset().isEmpty())
                    buffer.charset().space().append(tableColumn.getCharset()).space();
                if (!tableColumn.getCollate().isEmpty())
                    buffer.collate().space().append(tableColumn.getCollate()).space();
            }
            // 是否允许为空
            if (tableColumn.isNullable())
                buffer.nullToken().space();
            else
                buffer.notNull().space();
            // 默认值
            if (tableColumn.getDefault() != null)
                buffer.defaultToken().space().value(tableColumn.getDefault());
            // 注释
            if (!tableColumn.getComment().isEmpty())
                buffer.comment().space().string(tableColumn.getComment());
            buffer.comma();
        }

        // 主键
        buffer.primaryKey().space().leftBracket();
        for (SqlTableColumnInfo primaryKey : tableInfo.getPrimaryKeys())
            buffer.name(primaryKey.getName()).comma();
        buffer.removeLast();
        buffer.rightBracket().comma();

        // 外键
        for (ForeignKey foreignKey : tableInfo.getForeignKeys()) {
            if (foreignKey.sources().length != foreignKey.targets().length)
                throw new Exception("foreign key sources length is not equal targets length");
            if (foreignKey.sources().length == 0)
                throw new Exception("foreign key sources is empty");
            buffer.constraint().space();
            buffer.nameWrap();
            buffer.append("fk_");
            for (String source : foreignKey.sources())
                buffer.append(source).append("_");
            for (String target : foreignKey.targets())
                buffer.append(target).append("_");
            buffer.removeLast();
            buffer.nameWrap();
            buffer.space().foreignKey().space().leftBracket();
            for (String source : foreignKey.sources())
                buffer.name(source).comma();
            buffer.removeLast();
            buffer.rightBracket().space();
            buffer.references().space();
            if (foreignKey.tableClass() != Object.class) {
                SqlMapperInfo mapperInfo = context.getMapperInfo(foreignKey.tableClass());
                if (mapperInfo == null || !(mapperInfo instanceof SqlTableInfo))
                    throw new NotSupportedException("not supported foreign key table class " + foreignKey.tableClass());
                buffer.name(((SqlTableInfo) mapperInfo).getName()).space();
            } else if (!foreignKey.table().isEmpty())
                buffer.name(foreignKey.table()).space();
            else
                throw new Exception("foreign key table is not set");
            buffer.space().leftBracket();
            for (String target : foreignKey.targets())
                buffer.name(target).comma();
            buffer.removeLast();
            buffer.rightBracket().space();
            buffer.onDelete().space();
            appendForeignKeyAction(buffer, foreignKey.delete());
            buffer.onUpdate().space();
            appendForeignKeyAction(buffer, foreignKey.update());
            buffer.comma();
        }

        // 索引
        for (Index index : tableInfo.getIndices()) {
            if (index.keys().length == 0)
                throw new Exception("index keys is empty");
            switch (index.type()) {
                case NORMAL:
                    break;
                case FULLTEXT:
                    buffer.append("FULLTEXT").space();
                    break;
                case SPATIAL:
                    buffer.append("SPATIAL").space();
                    break;
                case UNIQUE:
                    buffer.append("UNIQUE").space();
                    break;
                default:
                    throw new NotSupportedException("not supported index type: " + index.type());
            }
            buffer.index().space();
            buffer.nameWrap();
            buffer.append("index_");
            for (String key : index.keys())
                buffer.append(key).append("_");
            buffer.removeLast();
            buffer.nameWrap();
            buffer.space().leftBracket();
            for (String key : index.keys())
                buffer.name(key).comma();
            buffer.removeLast();
            buffer.rightBracket().space();
            buffer.using().space();
            switch (index.method()) {
                case BTREE:
                    buffer.append("BTREE");
                    break;
                case HASH:
                    buffer.append("HASH");
                    break;
                default:
                    break;
            }

            if (!index.comment().isEmpty())
                buffer.space().comment().space().string(index.comment());
            buffer.comma();
        }

        // 唯一约束
        for (Unique unique : tableInfo.getUniques()) {
            if (unique.value().length == 0)
                throw new Exception("unique keys is empty");
            buffer.constraint().space();
            buffer.nameWrap();
            buffer.append("unique_");
            for (String key : unique.value())
                buffer.append(key).append("_");
            buffer.removeLast();
            buffer.nameWrap().space();
            buffer.unique().space().leftBracket();
            for (String key : unique.value())
                buffer.name(key).comma();
            buffer.removeLast();
            buffer.rightBracket().comma();
        }
        buffer.removeLast();
        buffer.rightBracket();

        if (!tableInfo.getEngine().isEmpty())
            buffer.space().engine().space().equal().space().append(tableInfo.getEngine());
        if (!tableInfo.getCharset().isEmpty())
            buffer.space().charset().space().equal().space().append(tableInfo.getCharset());
        if (!tableInfo.getCollate().isEmpty())
            buffer.space().collate().space().equal().space().append(tableInfo.getCollate());

        if (!tableInfo.getComment().isEmpty())
            buffer.space().comment().space().equal().space().string(tableInfo.getComment());
        if (tableInfo.getAutoIncrement() != 0)
            buffer.space().autoIncrement().space().equal().space().append(tableInfo.getAutoIncrement());
        buffer.semicolon();

        return new SqlAssembledImpl(buffer.toString(), null);
    }

    @Override
    public SqlAssembled assembleCount(Condition condition) throws Exception {
        SqlAssembleBuffer buffer = new SqlAssembleBuffer();
        buffer.select().space().count('*').space();
        Collection<SqlGetter> getters = new ArrayList<SqlGetter>();
        appendFromAfter(buffer, condition, getters);
        return new SqlAssembledImpl(buffer.toString(), getters);
    }

    @Override
    public SqlAssembledQuery assembleSelect(Condition condition) throws Exception {
        SqlAssembleBuffer buffer = new SqlAssembleBuffer();
        buffer.select().space();
        for (SqlColumnInfo columnInfo : mapperInfo.getColumns())
            buffer.name(columnInfo.getTableName()).dot().name(columnInfo.getName()).comma();
        buffer.removeLast();
        buffer.space();
        Collection<SqlGetter> getters = new ArrayList<SqlGetter>();
        appendFromAfter(buffer, condition, getters);
        return new SqlAssembledQueryImpl(mapperInfo.getMapperClass(), buffer.toString(), getters, fieldSetters);
    }

    @Override
    public SqlAssembled assembleDelete(Condition condition) throws Exception {
        if (!(mapperInfo instanceof SqlTableInfo))
            throw new NotSupportedException("view not supported delete");
        if (condition == null || !condition.hasWheres())
            throw new NotSupportedException("delete not supported empty condition");
        SqlAssembleBuffer buffer = new SqlAssembleBuffer();
        buffer.deleteFrom().space().name(mapperInfo.getTableName());

        Collection<SqlGetter> getters = new ArrayList<SqlGetter>();
        appendWheres(buffer, condition.getWheres(), getters);
        buffer.semicolon();
        return new SqlAssembledImpl(buffer.toString(), getters);
    }

    @Override
    public SqlAssembled assembleUpdate(Updater updater) throws Exception {
        if (!(mapperInfo instanceof SqlTableInfo))
            throw new NotSupportedException("view not supported update");
        Condition condition = updater.getCondition();
        if (condition == null || !condition.hasWheres())
            throw new NotSupportedException("update not supported empty condition");
        SqlAssembleBuffer buffer = new SqlAssembleBuffer();
        buffer.update().space().name(mapperInfo.getTableName()).space();
        buffer.set();

        Collection<SqlGetter> getters = new ArrayList<SqlGetter>();
        Map<String, Object> updates = updater.getUpdates();
        for (Map.Entry<String, Object> entry : updates.entrySet()) {
            buffer.space().name(entry.getKey()).equal().question().comma();
            getters.add(new SqlGetterValue(entry.getValue()));
        }
        buffer.removeLast();
        buffer.space().where();
        appendWheres(buffer, condition.getWheres(), getters);
        buffer.semicolon();
        return new SqlAssembledImpl(buffer.toString(), getters);
    }

    @Override
    public SqlAssembled assembleInsert() throws Exception {
        if (!(mapperInfo instanceof SqlTableInfo))
            throw new NotSupportedException("view not supported insert");
        SqlAssembleBuffer buffer = new SqlAssembleBuffer();
        buffer.insertInto();
        return assembleInsertOrReplaceAfter(buffer);
    }

    @Override
    public SqlAssembled assembleReplace() throws Exception {
        if (!(mapperInfo instanceof SqlTableInfo))
            throw new NotSupportedException("view not supported insert");
        SqlAssembleBuffer buffer = new SqlAssembleBuffer();
        buffer.replaceInto();
        return assembleInsertOrReplaceAfter(buffer);
    }

    private SqlAssembled assembleInsertOrReplaceAfter(SqlAssembleBuffer buffer) {
        buffer.space().name(mapperInfo.getTableName()).space();
        buffer.leftBracket();
        for (SqlColumnInfo columnInfo : mapperInfo.getColumns())
            buffer.name(columnInfo.getName()).comma();
        buffer.removeLast().rightBracket().space();
        buffer.values();
        buffer.leftBracket();
        for (SqlColumnInfo columnInfo : mapperInfo.getColumns())
            buffer.question().comma();
        buffer.removeLast();
        buffer.rightBracket();
        buffer.semicolon();

        return new SqlAssembledImpl(buffer.toString(), insertGetters);
    }

    private void appendFromAfter(SqlAssembleBuffer buffer, Condition condition, Collection<SqlGetter> getters)
            throws Exception {
        buffer.from().space();
        buffer.name(mapperInfo.getTableName());
        if (!(mapperInfo instanceof SqlTableInfo)) {
            SqlViewInfo viewInfo = (SqlViewInfo) mapperInfo;
            appendJoins(buffer, viewInfo.getJoins());
        }
        if (condition.hasWheres()) {
            buffer.space().where();
            appendWheres(buffer, condition.getWheres(), getters);
        }
        if (condition.hasOrderList())
            appendOrderByList(buffer, condition.getOrderList());
        if (condition.hasLimit())
            appendLimit(buffer, condition.getLimit());
        buffer.semicolon();
    }

    private void appendJoins(SqlAssembleBuffer buffer, Iterable<Join> joins) throws Exception {
        for (Join join : joins)
            appendJoin(buffer, join);
    }

    private void appendJoin(SqlAssembleBuffer buffer, Join join) throws Exception {
        String tableName;
        if (join.tableClass() != Object.class)
            tableName = context.getMapperInfo(join.tableClass()).getTableName();
        else
            tableName = join.table();
        switch (join.join()) {
            case LEFT_JOIN:
                buffer.append("LEFT JOIN");
                break;
            case RIGHT_JOIN:
                buffer.append("RIGHT JOIN");
                break;
            case INNER_JOIN:
                buffer.append("INNER JOIN");
                break;
            case OUTTER_JOIN:
                buffer.append("OUTTER JOIN");
                break;
            default:
                break;
        }
        buffer.space().name(tableName).space().append("ON").space();
        for (int i = 0; i < join.sources().length; i++) {
            String source = join.sources()[i];
            String target = join.targets()[i];

            if (i != 0)
                buffer.and().space();
            buffer.name(mapperInfo.getTableName()).dot().name(source);
            buffer.equal();
            buffer.name(tableName).dot().name(target);
            buffer.comma();
        }
        buffer.removeLast();
    }

    private void appendWheres(SqlAssembleBuffer buffer, Iterable<SqlWhere> wheres, Collection<SqlGetter> getters)
            throws Exception {
        for (SqlWhere where : wheres)
            appendWhere(buffer, where, getters);
    }

    private void appendWhere(SqlAssembleBuffer buffer, SqlWhere where, Collection<SqlGetter> getters)
            throws Exception {
        buffer.space();
        switch (where.getWhereType()) {
            case LOGIC: {
                SqlWhereLogic logic = (SqlWhereLogic) where;
                switch (logic.getLogicType()) {
                    case AND:
                        buffer.and();
                        break;
                    case OR:
                        buffer.or();
                        break;
                    case NOT:
                        buffer.not();
                        break;
                    default:
                        break;
                }
                appendWhere(buffer, logic.getWhere(), getters);
                break;
            }
            case UNARY: {
                SqlWhereUnary unary = (SqlWhereUnary) where;
                switch (unary.getUnaryType()) {
                    case IS_NULL:
                        SqlColumnInfo columnInfo = mapperInfo.getColumn(unary.getColumn());
                        buffer.isNull().leftBracket().name(columnInfo.getTableName()).dot().name(columnInfo.getName())
                                .rightBracket();
                        break;
                    default:
                        break;
                }
                break;
            }
            case BINARY: {
                SqlWhereBinary binary = (SqlWhereBinary) where;
                SqlColumnInfo columnInfo = mapperInfo.getColumn(binary.getColumn());
                buffer.name(columnInfo.getTableName()).dot().name(columnInfo.getName()).space();
                switch (binary.getBinaryType()) {
                    case EQUAL:
                        buffer.equal();
                        break;
                    case NOT_EQUAL:
                        buffer.notEqual();
                        break;
                    case GREATER:
                        buffer.greater();
                        break;
                    case GREATER_EQUAL:
                        buffer.greaterEqual();
                        break;
                    case LESS:
                        buffer.less();
                        break;
                    case LESS_EQUAL:
                        buffer.lessEqual();
                        break;
                    case LIKE:
                        buffer.like(binary.getValue());
                        return;
                    case LIKE_MATCH:
                        buffer.regexp(binary.getValue());
                        return;
                    case LEFT_LIKE:
                        buffer.leftLike(binary.getValue());
                        return;
                    case RIGHT_LIKE:
                        buffer.rightLike(binary.getValue());
                        return;
                    default:
                        break;
                }
                buffer.space().question();
                getters.add(new SqlGetterValue(binary.getValue()));
                break;
            }
            case IN: {
                SqlWhereBinary binary = (SqlWhereBinary) where;
                SqlColumnInfo columnInfo = mapperInfo.getColumn(binary.getColumn());
                buffer.name(columnInfo.getTableName()).dot().name(columnInfo.getName()).space().in().space();
                SqlWhereIn in = (SqlWhereIn) where;
                buffer.leftBracket();
                for (Object value : in.getValues()) {
                    buffer.question().comma();
                    getters.add(new SqlGetterValue(value));
                }
                buffer.removeLast();
                buffer.rightBracket();
                break;
            }
            case CONDITION: {
                Condition condition = (Condition) where;
                buffer.leftBracket();
                appendWheres(buffer, condition.getWheres(), getters);
                buffer.rightBracket();
                break;
            }
            default:
                throw new NotSupportedException("not supported where type");
        }
    }

    private void appendOrderByList(SqlAssembleBuffer buffer, Iterable<SqlOrderBy> orderBies) {
        buffer.space();
        boolean first = true;
        for (SqlOrderBy sqlOrderBy : orderBies) {
            if (first) {
                buffer.orderBy().space();
                first = false;
            }

            SqlColumnInfo columnInfo = mapperInfo.getColumn(sqlOrderBy.getColumn());
            buffer.name(columnInfo.getTableName()).dot().name(columnInfo.getName()).space();
            if (sqlOrderBy.isAsc())
                buffer.asc();
            else
                buffer.desc();
            buffer.comma();
        }
        buffer.removeLast();
    }

    private void appendLimit(SqlAssembleBuffer buffer, SqlLimit limit) {
        buffer.space().append("LIMIT ").append(limit.getOffset()).comma().append(limit.getTotal());
    }
}
