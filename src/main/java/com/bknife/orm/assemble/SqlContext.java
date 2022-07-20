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
import com.bknife.orm.assemble.impl.mysql.SqlAssembleMysqlFactory;

public class SqlContext implements SqlConstants {
    private Map<String, SqlAssembleFactory> assembleFactories = new HashMap<>();
    private Map<String, SqlAssemble> assembles = new HashMap<>();
    private Map<Class<?>, SqlMapperInfo<?>> mappers = new HashMap<>();

    private boolean verbose = false;

    public SqlContext() {
        // 注册mysql组装器工厂
        addSupport(MYSQL, new SqlAssembleMysqlFactory());
    }

    /**
     * 添加支持的数据库类型
     * 
     * @param factory
     */
    public void addSupport(String sqlType, SqlAssembleFactory factory) {
        assembleFactories.put(sqlType, factory);
    }

    /**
     * 获取assemble
     * 
     * @param sqlType
     * @return
     * @throws Exception
     */
    public synchronized SqlAssemble getAssemble(String sqlType) throws Exception {
        SqlAssemble assemble = (SqlAssemble) assembles.get(sqlType);
        if (assemble != null)
            return assemble;
        assemble = createAssemble(sqlType);
        if (assemble == null)
            return null;
        assembles.put(sqlType, assemble);
        return assemble;
    }

    /**
     * 获取mapper info
     * 
     * @param <T>
     * @param mapperClass
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public synchronized <T> SqlMapperInfo<T> getMapperInfo(Class<T> mapperClass) throws Exception {
        SqlMapperInfo<T> mapper = (SqlMapperInfo<T>) mappers.get(mapperClass);
        if (mapper == null) {
            mapper = createMapperInfo(mapperClass);
            if (mapper == null)
                return null;
            mappers.put(mapperClass, mapper);
        }

        return mapper;
    }

    /**
     * 创建assemble
     * 
     * @param sqlType
     * @return
     * @throws Exception
     */
    private SqlAssemble createAssemble(String sqlType) throws Exception {
        SqlAssembleFactory factory = assembleFactories.get(sqlType);
        if (factory == null)
            throw new NotSupportedException(sqlType);
        return factory.getAssemble(this);
    }

    /**
     * 创建mapper info
     * 
     * @param <T>
     * @param mapperClass
     * @return
     * @throws Exception
     */
    private <T> SqlMapperInfo<T> createMapperInfo(Class<T> mapperClass) throws Exception {
        Table table = mapperClass.getDeclaredAnnotation(Table.class);
        if (table != null) {
            // 创建表映射
            List<SqlTableColumnInfo.Builder> columnBuilders = new ArrayList<SqlTableColumnInfo.Builder>();
            for (Field field : mapperClass.getDeclaredFields()) {
                field.setAccessible(true);
                Column column = field.getDeclaredAnnotation(Column.class);
                if (column == null)
                    continue;
                columnBuilders.add(new SqlTableColumnInfo.Builder().setField(field).setColumn(column));
            }

            return SqlTableInfo.create(mapperClass, table, columnBuilders);
        }

        // 创建视图映射
        SqlTableInfo<?> tableInfo = null;
        Class<?> currentClass = mapperClass;
        List<SqlColumnInfo.Builder> columnBuilders = new ArrayList<SqlColumnInfo.Builder>();
        do {
            table = currentClass.getDeclaredAnnotation(Table.class);

            if (table != null) {
                // 该类所属所有属性均为该表类的表列
                tableInfo = (SqlTableInfo<?>) getMapperInfo(currentClass);
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
                    SqlMapperInfo<?> currentMapperInfo = getMapperInfo(column.tableClass());
                    if (currentMapperInfo == null || !(currentMapperInfo instanceof SqlTableInfo))
                        throw new NotSupportedException("not supported mapper class: " + currentClass);
                    columnBuilders.add(new SqlViewColumnInfo.Builder().setColumn(column).setField(field)
                            .setTable((SqlTableInfo<?>) currentMapperInfo));
                } else if (!column.table().isEmpty()) {
                    columnBuilders.add(new SqlViewColumnInfo.Builder().setColumn(column).setField(field)
                            .setTable(new SqlTableNameInfo(column.table())));
                } else
                    throw new Exception(field + ": column of view has not table or tableClass property");
            }

        } while ((currentClass = currentClass.getSuperclass()) != null);

        if (tableInfo == null)
            throw new NotSupportedException("mapper class must super table class: " + mapperClass);
        return SqlViewInfo.create(tableInfo, mapperClass, columnBuilders);
    }

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }
}
