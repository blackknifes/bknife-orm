package com.bknife.orm.assemble.impl.mysql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import com.bknife.orm.annotion.Column;
import com.bknife.orm.annotion.Column.Type;
import com.bknife.orm.annotion.ForeignKey;
import com.bknife.orm.annotion.Index;
import com.bknife.orm.annotion.Join;
import com.bknife.orm.annotion.Unique;
import com.bknife.orm.assemble.SqlAssemble;
import com.bknife.orm.assemble.SqlBuffer;
import com.bknife.orm.assemble.SqlColumnInfo;
import com.bknife.orm.assemble.SqlConfig;
import com.bknife.orm.assemble.SqlContext;
import com.bknife.orm.assemble.SqlGetter;
import com.bknife.orm.assemble.SqlMapperInfo;
import com.bknife.orm.assemble.SqlSetter;
import com.bknife.orm.assemble.SqlTableColumnInfo;
import com.bknife.orm.assemble.SqlTableInfo;
import com.bknife.orm.assemble.SqlViewInfo;
import com.bknife.orm.assemble.assembled.SqlAssembled;
import com.bknife.orm.assemble.assembled.SqlAssembledImpl;
import com.bknife.orm.assemble.assembled.SqlAssembledQuery;
import com.bknife.orm.assemble.assembled.SqlAssembledQueryImpl;
import com.bknife.orm.assemble.exception.NotSupportedException;
import com.bknife.orm.assemble.getter.SqlGetterField;
import com.bknife.orm.assemble.getter.SqlGetterValue;
import com.bknife.orm.assemble.setter.SqlSetterField;
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
    private SqlConfig config = new SqlConfigMysql();
    private SqlContext context;

    public SqlAssembleMysql(SqlContext context) {
        this.context = context;
    }

    private void appendTypeString(SqlBuffer buffer, Type type, int length, int dot) throws NotSupportedException {
        buffer.append(config.getSqlType(type));

        switch (type) {
            case STRING:
                buffer.parentheses(length);
                return;
            case CHARS:
                buffer.parentheses(length);
                return;
            case DECIMAL:
                buffer.openParentheses().append(length).comma().append(dot).closeParentheses();
                return;
            case BINARY:
                buffer.parentheses(length);
                return;
            default:
                break;
        }
    }

    private static void appendForeignKeyAction(SqlBuffer buffer, ForeignKey.ForeignKeyAction action)
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
    public <T> SqlAssembled<T> assembleCreateTable(SqlTableInfo<T> tableInfo) throws Exception {
        SqlBuffer buffer = new SqlBuffer();
        buffer.createTableIfNotExist(tableInfo.getName());
        buffer.openParentheses();

        String charset = tableInfo.getCharset();
        if (charset.isEmpty())
            charset = config.getTableCharset();
        String collate = tableInfo.getCollate();
        if (collate.isEmpty())
            collate = tableInfo.getCollate();

        for (SqlColumnInfo columnInfo : tableInfo.getColumns()) {
            SqlTableColumnInfo tableColumn = (SqlTableColumnInfo) columnInfo;
            Column.Type type = tableColumn.getType();
            if (type == Type.AUTO)
                type = config.getColumnType(tableColumn.getField().getType());

            int length = tableColumn.getLength();
            if (length == 0)
                length = config.getLength(type);

            int dot = tableColumn.getDot();
            if (dot == 0)
                dot = config.getDot(type);

            // 列名
            buffer.name(tableColumn.getName()).space();

            // 类型
            appendTypeString(buffer, type, length, tableColumn.getDot());
            buffer.space();
            if (type == Type.STRING) {
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
        if (tableInfo.getPrimaryKeys().iterator().hasNext()) {
            buffer.primaryKey().space().openParentheses();
            for (SqlTableColumnInfo primaryKey : tableInfo.getPrimaryKeys())
                buffer.name(primaryKey.getName()).comma();
            buffer.removeLast();
            buffer.closeParentheses().comma();
        }

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
            buffer.space().foreignKey().space().openParentheses();
            for (String source : foreignKey.sources())
                buffer.name(source).comma();
            buffer.removeLast();
            buffer.closeParentheses().space();
            buffer.references().space();
            if (foreignKey.tableClass() != Object.class) {
                SqlMapperInfo<?> mapperInfo = context.getMapperInfo(foreignKey.tableClass());
                if (mapperInfo == null || !(mapperInfo instanceof SqlTableInfo))
                    throw new NotSupportedException("not supported foreign key table class " + foreignKey.tableClass());
                buffer.name(((SqlTableInfo<?>) mapperInfo).getName()).space();
            } else if (!foreignKey.table().isEmpty())
                buffer.name(foreignKey.table()).space();
            else
                throw new Exception("foreign key table is not set");
            buffer.space().openParentheses();
            for (String target : foreignKey.targets())
                buffer.name(target).comma();
            buffer.removeLast();
            buffer.closeParentheses().space();
            buffer.onDelete().space();
            appendForeignKeyAction(buffer, foreignKey.delete());
            buffer.space().onUpdate().space();
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
            buffer.space().openParentheses();
            for (String key : index.keys())
                buffer.name(key).comma();
            buffer.removeLast();
            buffer.closeParentheses().space();
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
            buffer.unique().space().openParentheses();
            for (String key : unique.value())
                buffer.name(key).comma();
            buffer.removeLast();
            buffer.closeParentheses().comma();
        }
        buffer.removeLast();
        buffer.closeParentheses();

        if (!tableInfo.getEngine().isEmpty())
            buffer.space().engine().space().equal().space().append(tableInfo.getEngine());
        if (!charset.isEmpty())
            buffer.space().charset().space().equal().space().append(charset);
        if (!collate.isEmpty())
            buffer.space().collate().space().equal().space().append(collate);

        if (!tableInfo.getComment().isEmpty())
            buffer.space().comment().space().equal().space().string(tableInfo.getComment());
        if (tableInfo.getAutoIncrement() != 0)
            buffer.space().autoIncrement().space().equal().space().append(tableInfo.getAutoIncrement());
        buffer.semicolon();

        return new SqlAssembledImpl<T>(buffer.toString(), null, context.isVerbose());
    }

    @Override
    public <T> SqlAssembled<T> assembleCount(SqlMapperInfo<T> mapperInfo, Condition condition) throws Exception {
        SqlBuffer buffer = new SqlBuffer();
        buffer.select().space().count('*').space();
        Collection<SqlGetter> getters = new ArrayList<SqlGetter>();
        appendFromAfter(mapperInfo, buffer, condition, getters);
        return new SqlAssembledImpl<T>(buffer.toString(), getters, context.isVerbose());
    }

    @Override
    public <T> SqlAssembledQuery<T> assembleSelect(SqlMapperInfo<T> mapperInfo, Condition condition) throws Exception {
        SqlBuffer buffer = new SqlBuffer();
        buffer.select().space();

        Collection<SqlSetter> setters = new ArrayList<>();
        for (SqlColumnInfo columnInfo : mapperInfo.getColumns()) {
            buffer.name(columnInfo.getTableName()).dot().name(columnInfo.getName()).comma();
            setters.add(new SqlSetterField(columnInfo.getField()));
        }
        buffer.removeLast();
        buffer.space();
        Collection<SqlGetter> getters = new ArrayList<SqlGetter>();
        appendFromAfter(mapperInfo, buffer, condition, getters);
        return new SqlAssembledQueryImpl<T>(mapperInfo.getMapperClass(), buffer.toString(), getters, setters,
                context.isVerbose());
    }

    @Override
    public <T> SqlAssembled<T> assembleDelete(SqlTableInfo<T> tableInfo, Condition condition) throws Exception {
        if (condition == null || !condition.hasWheres())
            throw new NotSupportedException("delete not supported empty condition");
        SqlBuffer buffer = new SqlBuffer();
        buffer.delete().space().from().space().name(tableInfo.getTableName());

        buffer.space().where();
        Collection<SqlGetter> getters = new ArrayList<SqlGetter>();
        appendWheres(tableInfo, buffer, condition.getWheres(), getters);
        buffer.semicolon();
        return new SqlAssembledImpl<T>(buffer.toString(), getters, context.isVerbose());
    }

    @Override
    public <T> SqlAssembled<T> assembleUpdate(SqlTableInfo<T> tableInfo, Updater updater) throws Exception {
        Condition condition = updater.getCondition();
        if (condition == null || !condition.hasWheres())
            throw new NotSupportedException("update not supported empty condition");
        SqlBuffer buffer = new SqlBuffer();
        buffer.update().space().name(tableInfo.getTableName()).space();
        buffer.set();

        Collection<SqlGetter> getters = new ArrayList<SqlGetter>();
        Map<String, Object> updates = updater.getUpdates();
        for (Map.Entry<String, Object> entry : updates.entrySet()) {
            buffer.space().name(entry.getKey()).equal().question().comma();
            getters.add(new SqlGetterValue(entry.getValue()));
        }
        buffer.removeLast();
        buffer.space().where();
        appendWheres(tableInfo, buffer, condition.getWheres(), getters);
        buffer.semicolon();
        return new SqlAssembledImpl<T>(buffer.toString(), getters, context.isVerbose());
    }

    @Override
    public <T> SqlAssembled<T> assembleInsert(SqlTableInfo<T> tableInfo) throws Exception {
        SqlBuffer buffer = new SqlBuffer();
        buffer.insert().space().into();
        return assembleInsertOrReplaceAfter(tableInfo, buffer);
    }

    @Override
    public <T> SqlAssembled<T> assembleReplace(SqlTableInfo<T> tableInfo) throws Exception {
        SqlBuffer buffer = new SqlBuffer();
        buffer.replace().space().into();
        return assembleInsertOrReplaceAfter(tableInfo, buffer);
    }

    private <T> SqlAssembled<T> assembleInsertOrReplaceAfter(SqlTableInfo<T> tableInfo, SqlBuffer buffer) {
        buffer.space().name(tableInfo.getTableName()).space();
        buffer.openParentheses();
        Collection<SqlGetter> getters = new ArrayList<>();
        for (SqlColumnInfo columnInfo : tableInfo.getColumns()) {
            buffer.name(columnInfo.getName()).comma();
            getters.add(new SqlGetterField(columnInfo.getField()));
        }
        buffer.removeLast().closeParentheses().space();
        buffer.values();
        buffer.openParentheses();
        for (Iterator<SqlColumnInfo> itor = tableInfo.getColumns().iterator(); itor.hasNext(); itor.next())
            buffer.question().comma();
        buffer.removeLast();
        buffer.closeParentheses();
        buffer.semicolon();

        return new SqlAssembledImpl<T>(buffer.toString(), getters, context.isVerbose());
    }

    private <T> void appendFromAfter(SqlMapperInfo<T> mapperInfo, SqlBuffer buffer, Condition condition,
            Collection<SqlGetter> getters)
            throws Exception {
        buffer.from().space();
        buffer.name(mapperInfo.getTableName());
        if (!(mapperInfo instanceof SqlTableInfo)) {
            SqlViewInfo<T> viewInfo = (SqlViewInfo<T>) mapperInfo;
            appendJoins(mapperInfo, buffer, viewInfo.getJoins());
        }
        if (condition != null) {
            if (condition.hasWheres()) {
                buffer.space().where();
                appendWheres(mapperInfo, buffer, condition.getWheres(), getters);
            }
            if (condition.hasOrderList())
                appendOrderByList(mapperInfo, buffer, condition.getOrderList());
            if (condition.hasLimit())
                appendLimit(buffer, condition.getLimit());
        }
        buffer.semicolon();
    }

    private <T> void appendJoins(SqlMapperInfo<T> mapperInfo, SqlBuffer buffer, Iterable<Join> joins)
            throws Exception {
        for (Join join : joins)
            appendJoin(mapperInfo, buffer, join);
    }

    private <T> void appendJoin(SqlMapperInfo<T> mapperInfo, SqlBuffer buffer, Join join) throws Exception {
        buffer.space();
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

    private <T> void appendWheres(SqlMapperInfo<T> mapperInfo, SqlBuffer buffer, Iterable<SqlWhere> wheres,
            Collection<SqlGetter> getters)
            throws Exception {
        for (SqlWhere where : wheres)
            appendWhere(mapperInfo, buffer, where, getters);
    }

    private <T> void appendWhere(SqlMapperInfo<T> mapperInfo, SqlBuffer buffer, SqlWhere where,
            Collection<SqlGetter> getters)
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
                appendWhere(mapperInfo, buffer, logic.getWhere(), getters);
                break;
            }
            case UNARY: {
                SqlWhereUnary unary = (SqlWhereUnary) where;
                switch (unary.getUnaryType()) {
                    case IS_NULL:
                        SqlColumnInfo columnInfo = mapperInfo.getColumn(unary.getColumn());
                        buffer.isNull().openParentheses().name(columnInfo.getTableName()).dot()
                                .name(columnInfo.getName())
                                .closeParentheses();
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
                        buffer.like().openParentheses().question().closeParentheses();
                        getters.add(new SqlGetterValue("%" + binary.getValue() + "%"));
                        return;
                    case LIKE_MATCH:
                        buffer.regexp().openParentheses().question().closeParentheses();
                        getters.add(new SqlGetterValue(binary.getValue()));
                        return;
                    case LEFT_LIKE:
                        buffer.like().openParentheses().question().closeParentheses();
                        getters.add(new SqlGetterValue("%" + binary.getValue()));
                        return;
                    case RIGHT_LIKE:
                        buffer.like().openParentheses().question().closeParentheses();
                        getters.add(new SqlGetterValue(binary.getValue() + "%"));
                        return;
                    default:
                        break;
                }
                buffer.space().question();
                getters.add(new SqlGetterValue(binary.getValue()));
                break;
            }
            case IN: {
                SqlWhereIn in = (SqlWhereIn) where;
                SqlColumnInfo columnInfo = mapperInfo.getColumn(in.getColumn());
                buffer.name(columnInfo.getTableName()).dot().name(columnInfo.getName()).space().in().space();
                buffer.openParentheses();
                for (Object value : in.getValues()) {
                    buffer.question().comma();
                    getters.add(new SqlGetterValue(value));
                }
                buffer.removeLast();
                buffer.closeParentheses();
                break;
            }
            case CONDITION: {
                Condition condition = (Condition) where;
                buffer.openParentheses();
                appendWheres(mapperInfo, buffer, condition.getWheres(), getters);
                buffer.closeParentheses();
                break;
            }
            default:
                throw new NotSupportedException("not supported where type");
        }
    }

    private <T> void appendOrderByList(SqlMapperInfo<T> mapperInfo, SqlBuffer buffer,
            Iterable<SqlOrderBy> orderBies) {
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

    private void appendLimit(SqlBuffer buffer, SqlLimit limit) {
        buffer.space().append("LIMIT ").append(limit.getOffset()).comma().append(limit.getTotal());
    }
}
