package com.bknife.orm.assemble;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bknife.orm.annotion.Column;
import com.bknife.orm.annotion.Table;
import com.bknife.orm.assemble.exception.NotSupportedException;
import com.bknife.orm.assemble.exception.RecurseFoundException;

public class SqlFactoryImpl implements SqlFactory {
    private final Map<String, SqlAssembleFactory> assembleFactories = new HashMap<String, SqlAssembleFactory>();

    @Override
    public SqlAssemble getAssemble(SqlContext context, Class<?> mapperClass, String sqlType) throws Exception {
        SqlAssembleFactory factory = assembleFactories.get(sqlType);
        if (factory == null)
            throw new NotSupportedException(sqlType);
        return factory.getAssemble(context, mapperClass);
    }

    @Override
    public SqlMapperInfo getMapperInfo(SqlContext context, Class<?> mapperClass) throws Exception {
        Table table = mapperClass.getDeclaredAnnotation(Table.class);
        if (table != null) {
            // 创建表映射
            List<SqlTableColumnInfo.Builder> columnBuilders = new ArrayList<SqlTableColumnInfo.Builder>();
            for (Field field : mapperClass.getDeclaredFields()) {
                Column column = field.getDeclaredAnnotation(Column.class);
                if (column == null)
                    continue;
                columnBuilders.add(new SqlTableColumnInfo.Builder().setField(field).setColumn(column));
            }

            return new SqlTableInfo(mapperClass, table, columnBuilders);
        }

        // 创建视图映射
        SqlTableInfo tableInfo = null;
        Class<?> currentClass = mapperClass;
        List<SqlColumnInfo.Builder> columnBuilders = new ArrayList<SqlColumnInfo.Builder>();
        do {
            if (table != null) {
                // 该类所属所有属性均为该表类的表列
                tableInfo = (SqlTableInfo) getMapperInfo(context, currentClass);
                for (SqlColumnInfo columnInfo : tableInfo.getColumns())
                    columnBuilders.add(new SqlColumnInfo.ExistColumnBuilder(columnInfo));
                break;
            }

            for (Field field : currentClass.getDeclaredFields()) {
                Column column = field.getDeclaredAnnotation(Column.class);
                if (column == null)
                    continue;
                if (column.tableClass() != Object.class) {
                    if (mapperClass.isAssignableFrom(column.tableClass())) {
                        throw new RecurseFoundException(
                                "recurse found: " + column.tableClass() + " is chlid of " + mapperClass);
                    }
                    // 该类所有属性都为附表的引用列，查询来源表，并检查是否符合规则
                    SqlMapperInfo currentMapperInfo = getMapperInfo(context, currentClass);
                    if (currentMapperInfo == null || !(currentMapperInfo instanceof SqlTableInfo))
                        throw new NotSupportedException("not supported mapper class: " + currentClass);
                    columnBuilders.add(new SqlViewColumnInfo.Builder().setColumn(column).setField(field)
                            .setTable((SqlTableInfo) currentMapperInfo));
                } else if (!column.table().isEmpty()) {
                    columnBuilders.add(new SqlViewColumnInfo.Builder().setColumn(column).setField(field)
                            .setTable(new SqlTableNameInfo(column.table())));
                }
            }

        } while ((currentClass = currentClass.getSuperclass()) != null);

        if (tableInfo == null)
            throw new NotSupportedException("mapper class must super table class: " + mapperClass);
        return new SqlViewInfo(tableInfo, mapperClass, columnBuilders);
    }
}