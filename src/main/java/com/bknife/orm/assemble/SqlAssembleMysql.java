package com.bknife.orm.assemble;

import java.util.ArrayList;

import com.bknife.orm.annotion.Column.Type;
import com.bknife.orm.assemble.assembled.SqlAssembled;
import com.bknife.orm.assemble.assembled.SqlAssembledQuery;
import com.bknife.orm.mapper.Updater;
import com.bknife.orm.mapper.where.Condition;

public class SqlAssembleMysql implements SqlAssemble {
    private static class TypeMapper implements SqlTypeMapper {
        @Override
        public String toString(Type type, int length, int dot) {
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
                    if (length == 0)
                        throw new IllegalArgumentException("varchar length is illegal: " + length);
                    return "VARCHAR(" + length + ")";
                case CHARS:
                    if (length == 0)
                        throw new IllegalArgumentException("char length is illegal: " + length);
                    return "CHAR(" + length + ")";
                case DECIMAL:
                    if (length == 0)
                        throw new IllegalArgumentException("decimal length is illegal: " + length);
                    return "DECIMAL(" + length + "," + dot + ")";
                case DATE:
                    return "DATE";
                case TIME:
                    return "TIME";
                case TIMESTAMP:
                    return "TIMESTAMP";
                case BINARY:
                    if (length == 0)
                        throw new IllegalArgumentException("varchar length is illegal: " + length);
                    return "VARBINARY(" + length + ")";
                default:
                    break;
            }
            throw new IllegalArgumentException("type is illegal: " + type);
        }

    }

    private SqlTableInfo tableInfo;
    private SqlTypeMapper typeMapper = new TypeMapper();

    public SqlAssembleMysql(SqlAssembleFactory factory, Class<T> clazz) throws Exception {
        SqlMapperInfo mapperInfo = factory.getMapperInfo(clazz);
        if (mapperInfo == null || !(mapperInfo instanceof SqlTableInfo))
            throw new Exception("class [" + clazz + "] is not a table class");
        tableInfo = (SqlTableInfo) mapperInfo;
    }

    @Override
    public Class<?> getResultType() {
        return tableInfo.getTableClass();
    }

    @Override
    public SqlAssembled assembleCreateTable() throws Exception {
        SqlAssembleBuffer buffer = new SqlAssembleBuffer();
        buffer.appendCreateTableIfNotExist(tableInfo.getName());
        buffer.appendLeftBracket();

        // 开始组装列定义
        for (SqlColumnInfo column : tableInfo.getColumns()) {
            if (column.isTableColumn())
                buffer.appendColumnDefine(column, typeMapper).appendComma();
        }
        buffer.removeLast();
        buffer.appendRightBracket();

        // 开始组装表设置
        if (!tableInfo.getEngine().isEmpty())
            buffer.appendSpace().appendEngine().appendSpace().appendEqual().appendSpace().append(tableInfo.getEngine());
        if (!tableInfo.getCharset().isEmpty())
            buffer.appendSpace().appendCharset().appendSpace().appendEqual().appendSpace()
                    .append(tableInfo.getCharset());
        if (!tableInfo.getCollate().isEmpty())
            buffer.appendSpace().appendCollate().appendSpace().appendEqual().appendSpace()
                    .append(tableInfo.getCollate());
        if (!tableInfo.getComment().isEmpty())
            buffer.appendSpace().appendComment().appendSpace().appendEqual().appendSpace()
                    .append(tableInfo.getComment());
        if (tableInfo.getIncrement() != 0)
            buffer.appendSpace().appendAutoIncrement().appendSpace().appendEqual().appendSpace()
                    .append(tableInfo.getIncrement());
        buffer.appendSemicolon();

        return SqlAssemblePool.getPool().getAssembled(buffer.toString(), new ArrayList<>());
    }

    @Override
    public SqlAssembled assembleCount(Condition condition) throws Exception {

        return null;
    }

    @Override
    public SqlAssembledQuery assembleSelect(Condition condition) throws Exception {

        return null;
    }

    @Override
    public SqlAssembled assembleDelete(Condition condition) throws Exception {

        return null;
    }

    @Override
    public SqlAssembled assembleUpdate(Updater updater) throws Exception {

        return null;
    }

    @Override
    public SqlAssembled assembleInsert() throws Exception {

        return null;
    }

    @Override
    public SqlAssembled assembleReplace() throws Exception {

        return null;
    }

}
