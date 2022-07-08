package com.bknife.orm.assemble;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import com.bknife.base.converter.ConverterUtils;
import com.bknife.base.util.DateUtil;
import com.bknife.orm.annotion.DBColumn;
import com.bknife.orm.annotion.DBField;
import com.bknife.orm.annotion.DBForeignKey;
import com.bknife.orm.annotion.DBGroupBy;
import com.bknife.orm.annotion.DBIndex;
import com.bknife.orm.annotion.DBJoin;
import com.bknife.orm.annotion.DBSelectColumn;
import com.bknife.orm.annotion.DBTable;
import com.bknife.orm.annotion.DBUnique;
import com.bknife.orm.assemble.assembled.SqlAssembledHasResult;
import com.bknife.orm.assemble.assembled.SqlAssembledHasParameter;
import com.bknife.orm.mapper.Updater;
import com.bknife.orm.mapper.select.SqlOrderBy;
import com.bknife.orm.mapper.util.SqlMapperUtil;
import com.bknife.orm.mapper.where.Condition;
import com.bknife.orm.mapper.where.SqlWhere;
import com.bknife.orm.mapper.where.SqlWhereBinary;
import com.bknife.orm.mapper.where.SqlWhereIn;
import com.bknife.orm.mapper.where.SqlWhereLogic;
import com.bknife.orm.mapper.where.SqlWhereUnary;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SqlAssembleMysql<T> implements SqlAssemble<T> {
    private Class<T> mapperClass;
    private DBTable table;

    private List<Field> selectFields = new ArrayList<Field>();
    private List<Field> insertFields = new ArrayList<Field>();

    public static <T> SqlAssemble<T> create(Class<T> mapperClass)
            throws Exception {

        DBTable table = SqlMapperUtil.findTableAnnotation(mapperClass);
        if (table == null)
            throw new Exception(mapperClass + " class is not a table class");
        return new SqlAssembleMysql<T>(mapperClass, table);
    }

    private SqlAssembleMysql(Class<T> mapperClass, DBTable table) {
        this.mapperClass = mapperClass;
        this.table = table;

        Class<?> clazz = mapperClass;
        do {
            for (Field field : clazz.getDeclaredFields()) {
                DBField dbField;
                if ((dbField = field.getAnnotation(DBField.class)) != null ||
                        field.getAnnotation(DBColumn.class) != null ||
                        field.getAnnotation(DBSelectColumn.class) != null) {
                    field.setAccessible(true);
                    selectFields.add(field);
                    if (dbField != null)
                        insertFields.add(field);
                }
            }
        } while ((clazz = clazz.getSuperclass()) != null);
    }

    @Override
    public Class<T> getResultType() {
        return mapperClass;
    }

    @Override
    public SqlAssembledHasParameter assembleCreateTable() throws Exception {
        StringBuffer buffer = new StringBuffer();
        buffer.append("CREATE TABLE IF NOT EXISTS ");

        List<String> primaryKeys = new ArrayList<String>();
        buffer.append("`").append(table.name()).append("`");
        buffer.append("(");
        int count = 0;

        // 列声明
        Class<?> tempClass = mapperClass;
        while (tempClass != Object.class) {
            for (Field field : tempClass.getDeclaredFields()) {
                DBField dbField = field.getDeclaredAnnotation(DBField.class);
                if (dbField == null)
                    continue;
                ++count;
                if (dbField.primaryKey())
                    primaryKeys.add(dbField.name());

                buffer.append("`").append(dbField.name()).append("` ");
                assembleDBType(buffer, dbField.type(), dbField.length(), dbField.dot());
                if (dbField.unsigned())
                    buffer.append(" UNSIGNED");
                if (dbField.unique())
                    buffer.append(" UNIQUE");
                if (!dbField.charset().isEmpty())
                    buffer.append(" CHARACTER SET ").append(dbField.charset());
                if (!dbField.collate().isEmpty())
                    buffer.append(" COLLATE ").append(dbField.collate());

                buffer.append(dbField.nullable() ? " NULL" : " NOT NULL");
                if (dbField.increment())
                    buffer.append(" AUTO_INCREMENT");
                if (!dbField.defaultValue().isEmpty())
                    buffer.append(" DEFAULT ").append(dbField.defaultValue());
                if (!dbField.comment().isEmpty())
                    buffer.append(" COMMENT '").append(dbField.comment()).append("'");
                buffer.append(",");
            }
            tempClass = tempClass.getSuperclass();
        }

        // 主键
        if (count == 0)
            throw new Exception(mapperClass + " class table fields is empty");
        if (!primaryKeys.isEmpty()) {
            buffer.append("PRIMARY KEY(");
            for (String primaryKey : primaryKeys)
                buffer.append("`").append(primaryKey).append("`,");
            buffer.deleteCharAt(buffer.length() - 1);
            buffer.append("),");
        }

        tempClass = mapperClass;
        while (tempClass != Object.class) {
            // 索引
            for (DBIndex index : SqlMapperUtil.getIndexs(mapperClass)) {
                if (index.keys().length == 0)
                    throw new Exception("index keys cannot be empty");
                assembleIndexType(buffer, index.type());
                buffer.append(" INDEX ");
                buffer.append("`index_");
                buffer.append(table.name());
                for (String key : index.keys())
                    buffer.append("_").append(key);
                buffer.append("`");
                buffer.append("(");
                for (String key : index.keys())
                    buffer.append("`").append(key).append("`,");
                buffer.deleteCharAt(buffer.length() - 1);
                buffer.append(")");
                buffer.append(" USING ");
                assembleIndexMethod(buffer, index.method());
                if (!index.comment().isEmpty())
                    buffer.append(" COMMENT='").append(index.comment()).append("'");
                buffer.append(",");
            }

            // 外键
            for (DBForeignKey foreignKey : SqlMapperUtil.getForeignKeys(tempClass)) {
                if (foreignKey.sources().length == 0)
                    throw new Exception(tempClass + " foreign key sources cannot be empty");
                if (foreignKey.sources().length != foreignKey.targets().length)
                    throw new Exception(tempClass + " foreign key sources is mismatch to targets");
                String targetTableName = SqlMapperUtil.getTableName(foreignKey);
                buffer.append("CONSTRAINT ");
                buffer.append("`fk_");
                buffer.append(table.name()).append("_").append(targetTableName);
                buffer.append("` FOREIGN KEY (");
                for (String key : foreignKey.sources())
                    buffer.append("`").append(key).append("`,");
                buffer.deleteCharAt(buffer.length() - 1).append(")");
                buffer.append(" REFERENCES ");
                buffer.append("`").append(targetTableName).append("` (");
                for (String key : foreignKey.targets())
                    buffer.append("`").append(key).append("`,");
                buffer.deleteCharAt(buffer.length() - 1).append(")");
                buffer.append(" ON DELETE ");
                assembleForeignKeyAction(buffer, foreignKey.delete());
                buffer.append(" ON UPDATE ");
                assembleForeignKeyAction(buffer, foreignKey.delete());
                buffer.append(",");
            }

            // 唯一约束
            for (DBUnique unique : SqlMapperUtil.getUniques(tempClass)) {
                if (unique.value().length == 0)
                    throw new Exception(tempClass + " class unique keys cannot be empty");
                buffer.append("CONSTRAINT `unique");
                for (String key : unique.value())
                    buffer.append("_").append(key);
                buffer.append(" UNIQUE (");
                for (String key : unique.value())
                    buffer.append("`").append(key).append("`,");
                buffer.deleteCharAt(buffer.length() - 1);
                buffer.append("),");
            }
            tempClass = tempClass.getSuperclass();
        }

        buffer.deleteCharAt(buffer.length() - 1);
        buffer.append(")");

        // 表设置
        if (!table.engine().isEmpty())
            buffer.append(" ENGINE=").append(table.engine());
        if (table.increment() != 0)
            buffer.append(" AUTO_INCREMENT=").append(table.increment());
        if (!table.charset().isEmpty())
            buffer.append(" CHARACTER SET=").append(table.charset());
        if (!table.collate().isEmpty())
            buffer.append(" COLLATE=").append(table.collate());
        if (!table.comment().isEmpty())
            buffer.append(" COMMENT='").append(table.comment()).append("';");

        return SqlAssemblePool.getPool().getAssembledHasParameter(buffer.toString(), new ArrayList<SqlGetter>());
    }

    private void assembleSelectFrom(List<SqlGetter> params, StringBuffer buffer, Condition condition) throws Exception {
        buffer.append(" FROM ").append("`").append(table.name()).append("`");

        Class<?> viewMapper = mapperClass;
        while (viewMapper != Object.class) {
            for (DBJoin join : SqlMapperUtil.getJoins(viewMapper)) {
                buffer.append(" ");
                assembleJoinType(buffer, join.join());
                String targetTableName = SqlMapperUtil.getTableName(join);
                buffer.append(" `").append(targetTableName).append("` ON ");

                if (join.sources().length == 0)
                    throw new Exception("join sources cannot be empty");
                if (join.sources().length != join.targets().length)
                    throw new Exception("join sources count is mismatch to targets count");
                for (int i = 0; i < join.sources().length; i++) {
                    if (i != 0)
                        buffer.append(" AND ");
                    String source = join.sources()[i];
                    String target = join.targets()[i];
                    buffer.append("`").append(table.name()).append("`.`").append(source).append("`=");
                    buffer.append("`").append(targetTableName).append("`.`").append(target).append("`");
                }
            }
            viewMapper = viewMapper.getSuperclass();
        }

        // where
        if (condition != null && condition.hasWheres()) {
            buffer.append(" WHERE");
            assembleWheres(buffer, condition.getWheres());
        }

        // group by
        boolean hasGroupBy = false;
        viewMapper = mapperClass;
        while (viewMapper != Object.class) {
            DBGroupBy[] groupBies = SqlMapperUtil.getGroupBies(mapperClass);
            for (DBGroupBy dbGroupBy : groupBies) {
                if (!hasGroupBy) {
                    hasGroupBy = true;
                    buffer.append(" GROUP BY ");
                }
                buffer.append("`").append(SqlMapperUtil.getTableName(dbGroupBy)).append("`.`").append(dbGroupBy.name())
                        .append("`,");
            }
            viewMapper = viewMapper.getSuperclass();
        }
        if (hasGroupBy)
            buffer.deleteCharAt(buffer.length() - 1);

        if (condition != null) {
            // order by
            if (condition.hasOrderList()) {
                buffer.append(" ORDER BY ");
                for (SqlOrderBy orderBy : condition.getOrderList()) {
                    assembleFullName(buffer, orderBy.getColumn());
                    buffer.append(" ").append(orderBy.isAsc() ? "ASC" : "DESC").append(",");
                }
                buffer.deleteCharAt(buffer.length() - 1);
            }

            // limit
            if (condition.hasLimit()) {
                buffer.append(" LIMIT ").append(condition.getLimit().getOffset()).append(",")
                        .append(condition.getLimit().getTotal());
            }
        }
        buffer.append(";");
    }

    @Override
    public SqlAssembledHasParameter assembleCount(Condition condition) throws Exception {
        List<SqlGetter> getters = new ArrayList<SqlGetter>();
        StringBuffer buffer = new StringBuffer();
        buffer.append("SELECT COUNT(*)");
        assembleSelectFrom(getters, buffer, condition);

        return SqlAssemblePool.getPool().getAssembledHasParameter(buffer.toString(), getters);
    }

    @Override
    public SqlAssembledHasResult assembleSelect(Condition condition) throws Exception {
        List<SqlGetter> getters = new ArrayList<SqlGetter>();
        List<SqlSetter> setters = new ArrayList<SqlSetter>();

        StringBuffer buffer = new StringBuffer();
        buffer.append("SELECT ");
        Class<?> viewClass = mapperClass;

        int count = 0;
        while (viewClass != Object.class) {
            for (Field field : viewClass.getDeclaredFields()) {
                do {
                    DBField dbField = field.getDeclaredAnnotation(DBField.class);
                    if (dbField != null) {
                        buffer.append("`").append(table.name()).append("`.`").append(dbField.name()).append("`");
                        setters.add(SqlAssemblePool.getPool().getSetterField(field));
                        break;
                    }

                    DBColumn dbColumn = field.getDeclaredAnnotation(DBColumn.class);
                    if (dbColumn != null) {
                        buffer.append("`").append(SqlMapperUtil.getTableName(dbColumn)).append("`.`")
                                .append(dbColumn.name())
                                .append("`");
                        setters.add(SqlAssemblePool.getPool().getSetterField(field));
                        break;
                    }

                    DBSelectColumn selectColumn = field.getDeclaredAnnotation(DBSelectColumn.class);
                    if (selectColumn != null) {
                        buffer.append("(").append(selectColumn.value()).append(")");
                        buffer.append(" AS `").append(field.getName()).append("`");
                        setters.add(SqlAssemblePool.getPool().getSetterField(field));
                        break;
                    }
                    ++count;
                } while (false);
                buffer.append(",");
            }
            viewClass = viewClass.getSuperclass();
        }
        if (count == 0)
            throw new Exception("no field selectable");
        buffer.deleteCharAt(buffer.length() - 1);
        assembleSelectFrom(getters, buffer, condition);

        return SqlAssemblePool.getPool().getAssembledHasResult(mapperClass, buffer.toString(), getters, setters);
    }

    @Override
    public SqlAssembledHasParameter assembleDelete(Condition condition) throws Exception {
        if (condition == null || condition.getWheres().isEmpty())
            throw new Exception("forbit delete with no where");
        StringBuffer buffer = new StringBuffer();
        buffer.append("DELETE FROM `").append(table.name()).append("` WHERE ");
        assembleWheres(buffer, condition.getWheres());

        return buffer.toString();
    }

    @Override
    public String assembleUpdate(Updater updater) throws Exception {
        if (updater == null || updater.getCondition().getWheres().isEmpty())
            throw new Exception("forbit update with no where");
        if (updater.getUpdates().isEmpty())
            throw new Exception("updates is empty");
        StringBuffer buffer = new StringBuffer();
        buffer.append("UPDATE `").append(table.name()).append("` SET ");

        // update set ...
        for (Entry<String, Object> entry : updater.getUpdates().entrySet()) {
            assembleFullName(buffer, entry.getKey());
            buffer.append("=");
            assembleValue(buffer, entry.getValue());
            buffer.append(",");
        }
        buffer.deleteCharAt(buffer.length() - 1).append(" WHERE ");
        assembleWheres(buffer, updater.getCondition().getWheres());

        return buffer.toString();
    }

    private String assembleInsert(boolean isReplace) throws Exception {
        StringBuffer buffer = new StringBuffer();
        buffer.append(isReplace ? "REPLACE" : "INSERT").append(" INTO ");
        buffer.append("`").append(table.name()).append("`");

        // 列
        buffer.append("(");
        int count = 0;
        Class<?> viewClass = mapperClass;
        while (viewClass != Object.class) {
            for (Field field : viewClass.getDeclaredFields()) {
                DBField dbField = field.getDeclaredAnnotation(DBField.class);
                if (dbField == null)
                    continue;
                ++count;
                buffer.append("`").append(dbField.name()).append("`,");
            }
            viewClass = viewClass.getSuperclass();
        }
        if (count == 0)
            throw new Exception("no field insertable");
        buffer.deleteCharAt(buffer.length() - 1).append(")");

        // 参数
        buffer.append(" VALUES(");
        viewClass = mapperClass;
        while (viewClass != Object.class) {
            for (Field field : viewClass.getDeclaredFields()) {
                DBField dbField = field.getDeclaredAnnotation(DBField.class);
                if (dbField == null)
                    continue;
                buffer.append("?,");
            }
            viewClass = viewClass.getSuperclass();
        }
        buffer.deleteCharAt(buffer.length() - 1).append(");");
        return buffer.toString();
    }

    @Override
    public String assembleInsert() throws Exception {
        return assembleInsert(false);
    }

    @Override
    public String assembleReplace() throws Exception {
        return assembleInsert(true);
    }

    private void assembleDBType(StringBuffer buffer, DBField.Type type, int length, int dot) throws Exception {
        switch (type) {
            case BYTE:
                buffer.append("TINYINT");
                if (length != 0)
                    buffer.append("(").append(length).append(")");
                break;
            case INTEGER:
                buffer.append("INT");
                if (length != 0)
                    buffer.append("(").append(length).append(")");
                break;
            case LONG:
                buffer.append("BIGINT");
                if (length != 0)
                    buffer.append("(").append(length).append(")");
                break;
            case DOUBLE:
                buffer.append("DOUBLE");
                if (length != 0) {
                    buffer.append("(").append(length);
                    if (dot != 0)
                        buffer.append(",").append(dot);
                    buffer.append(")");
                }
                break;
            case DECIMAL:
                buffer.append("DECIMAL");
                if (length == 0)
                    throw new Exception("length of decimal field must be greater than 0");
                buffer.append("(").append(length);
                if (dot != 0)
                    buffer.append(",").append(dot);
                buffer.append(")");
                break;
            case STRING:
                buffer.append("VARCHAR");
                if (length == 0)
                    throw new Exception("length of string field must be greater than 0");
                buffer.append("(").append(length).append(")");
                break;
            case CHARS:
                buffer.append("CHAR");
                if (length == 0)
                    throw new Exception("length of char field must be greater than 0");
                buffer.append("(").append(length).append(")");
                break;
            case DATE:
                buffer.append("DATE");
                break;
            case TIME:
                buffer.append("DATETIME");
                break;
            case TIMESTAMP:
                buffer.append("BIGINT");
                break;
            case BINARY:
                buffer.append("VARBINARY");
                if (length == 0)
                    throw new Exception("length of binary field must be greater than 0");
                buffer.append("(").append(length).append(")");
                break;
            default:
                throw new Exception("error database field type : " + type);
        }
    }

    private void assembleForeignKeyAction(StringBuffer buffer, DBForeignKey.ForeignKeyAction action) throws Exception {
        switch (action) {
            case CASCADE:
                buffer.append("CASCADE");
                break;
            case NO_ACTION:
                buffer.append("NO ACTION");
                break;
            case RESTRICT:
                buffer.append("RESTRICT");
                break;
            case SET_NULL:
                buffer.append("SET NULL");
                break;
            default:
                throw new Exception("error foreign key action : " + action);
        }
    }

    private void assembleIndexType(StringBuffer buffer, DBIndex.IndexType type) throws Exception {
        switch (type) {
            case NORMAL:
                buffer.append("NORMAL");
                break;
            case FULLTEXT:
                buffer.append("FULLTEXT");
                break;
            case SPATIAL:
                buffer.append("SPATIAL");
                break;
            case UNIQUE:
                buffer.append("UNIQUE");
                break;
            default:
                throw new Exception("error index type : " + type);
        }
    }

    private void assembleIndexMethod(StringBuffer buffer, DBIndex.IndexMethod method) throws Exception {
        switch (method) {
            case BTREE:
                buffer.append("BTREE");
                break;
            case HASH:
                buffer.append("HASH");
                break;
            default:
                throw new Exception("error index method : " + method);
        }
    }

    private void assembleWheres(StringBuffer buffer, Collection<SqlWhere> wheres) throws Exception {
        for (SqlWhere sqlWhere : wheres)
            assembleWhere(buffer, sqlWhere);
    }

    private void assembleWhere(StringBuffer buffer, SqlWhere where)
            throws Exception {
        buffer.append(" ");
        switch (where.getWhereType()) {
            case CONDITION: {
                Condition condition = (Condition) where;
                buffer.append("(");
                assembleWheres(buffer, condition.getWheres());
                buffer.append(")");
                break;
            }
            case LOGIC: {
                SqlWhereLogic logic = (SqlWhereLogic) where;
                switch (logic.getLogicType()) {
                    case AND:
                        buffer.append("AND");
                        break;
                    case OR:
                        buffer.append("OR");
                        break;
                    case NOT:
                        buffer.append("NOT");
                        break;
                    default:
                        break;
                }
                assembleWhere(buffer, logic.getWhere());
                break;
            }
            case UNARY: {
                SqlWhereUnary unary = (SqlWhereUnary) where;
                switch (unary.getUnaryType()) {
                    case IS_NULL:
                        buffer.append("ISNULL(");
                        assembleFullName(buffer, unary.getColumn());
                        buffer.append(")");
                        break;
                    default:
                        break;
                }
                break;
            }
            case BINARY: {
                SqlWhereBinary binary = (SqlWhereBinary) where;
                assembleFullName(buffer, binary.getColumn());
                switch (binary.getBinaryType()) {
                    case EQUAL:
                        buffer.append("=");
                        break;
                    case NOT_EQUAL:
                        buffer.append("<>");
                        break;
                    case GREATER:
                        buffer.append(">");
                        break;
                    case GREATER_EQUAL:
                        buffer.append(">=");
                        break;
                    case LESS:
                        buffer.append("<");
                        break;
                    case LESS_EQUAL:
                        buffer.append("<=");
                        break;
                    case LIKE:
                        buffer.append(" LIKE(\"%").append(binary.getValue()).append("%\")");
                        return;
                    case LIKE_MATCH:
                        buffer.append(" LIKE(\"").append(binary.getValue()).append("\")");
                        return;
                    case LEFT_LIKE:
                        buffer.append(" LIKE(\"%").append(binary.getValue()).append("\")");
                        return;
                    case RIGHT_LIKE:
                        buffer.append(" LIKE(\"").append(binary.getValue()).append("%\")");
                        return;
                    default:
                        break;
                }
                assembleValue(buffer, binary.getValue());
                break;
            }
            case IN:
                SqlWhereIn whereIn = (SqlWhereIn) where;
                assembleFullName(buffer, whereIn.getColumn());
                buffer.append(" IN(");
                Collection<Object> values = whereIn.getValues();
                if (values.isEmpty())
                    throw new Exception("where in values cannot be empty");
                for (Object object : values) {
                    assembleValue(buffer, object);
                    buffer.append(",");
                }
                buffer.deleteCharAt(buffer.length() - 1);
                buffer.append(")");
                return;
            default:
                break;
        }
    }

    private void assembleJoinType(StringBuffer buffer, DBJoin.JoinType joinType) throws Exception {
        switch (joinType) {
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
                throw new Exception("error join type");
        }
    }

    private String assembleFullName(StringBuffer buffer, String fieldName) {
        Class<?> clazz = mapperClass;
        while (clazz != Object.class) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                DBField dbField = field.getDeclaredAnnotation(DBField.class);
                if (dbField != null) {
                    buffer.append("`").append(table.name()).append("`.`").append(dbField.name()).append("`");
                    return field.getName();
                }

                DBColumn column = field.getDeclaredAnnotation(DBColumn.class);
                if (column != null) {
                    buffer.append("`").append(SqlMapperUtil.getTableName(column)).append("`.`").append(column.name())
                            .append("`");
                    return field.getName();
                }
                DBSelectColumn selectColumn = field.getDeclaredAnnotation(DBSelectColumn.class);
                if (selectColumn != null) {
                    buffer.append(selectColumn.value());
                    return field.getName();
                }
                break;
            } catch (Exception e) {
            }
            clazz = clazz.getSuperclass();
        }

        buffer.append(fieldName);
        return null;
    }

    private void assembleValue(StringBuffer buffer, Object object) {
        if (object instanceof String)
            buffer.append("\"").append(object).append("\"");
        else if (object instanceof Date)
            buffer.append("\"").append(DateUtil.formatDatetime((Date) object)).append("\"");
        else
            buffer.append(object);
    }
}
