package com.bknife.orm.mapper.util;

import com.bknife.orm.annotion.DBColumn;
import com.bknife.orm.annotion.ForeignKey;
import com.bknife.orm.annotion.GroupBy;
import com.bknife.orm.annotion.Index;
import com.bknife.orm.annotion.Join;
import com.bknife.orm.annotion.Table;
import com.bknife.orm.annotion.Unique;

/**
 * sql映射工具
 */
public interface SqlMapperUtil {

    /**
     * 查询表类
     * 
     * @param clazz
     * @return
     */
    public static Class<?> findTableClass(Class<?> clazz) {
        while (clazz != Object.class) {
            if (clazz.getDeclaredAnnotation(Table.class) != null)
                return clazz;
            clazz = clazz.getSuperclass();
        }
        return null;
    }

    /**
     * 从当前类获取表注解
     * 
     * @param clazz
     * @return
     */
    public static Table getTableAnnotation(Class<?> clazz) {
        return clazz.getDeclaredAnnotation(Table.class);
    }

    /**
     * 获取表注解
     * 
     * @param clazz
     * @return
     */
    public static Table findTableAnnotation(Class<?> clazz) {
        while (clazz != Object.class) {
            Table table = clazz.getDeclaredAnnotation(Table.class);
            if (table != null)
                return table;
            clazz = clazz.getSuperclass();
        }
        return null;
    }

    /**
     * 查询表名
     * 
     * @param clazz
     * @return
     */
    public static String findTableName(Class<?> clazz) {
        return findTableAnnotation(clazz).name();
    }

    /**
     * 获取表名
     * 
     * @param column
     * @return
     */
    public static String getTableName(DBColumn column) {
        return column.tableClass() == Object.class ? column.table() : findTableName(column.tableClass());
    }

    /**
     * 获取表名
     * 
     * @param foreignKey
     * @return
     */
    public static String getTableName(ForeignKey foreignKey) {
        return foreignKey.tableClass() == Object.class ? foreignKey.table() : findTableName(foreignKey.tableClass());
    }

    /**
     * 获取表名
     * 
     * @param groupBy
     * @return
     */
    public static String getTableName(GroupBy groupBy) {
        return groupBy.tableClass() == Object.class ? groupBy.table() : findTableName(groupBy.tableClass());
    }

    /**
     * 获取表名
     * 
     * @param join
     * @return
     */
    public static String getTableName(Join join) {
        return join.tableClass() == Object.class ? join.table() : findTableName(join.tableClass());
    }

    /**
     * 获取所有join连接
     * 
     * @param clazz
     * @return
     */
    public static Join[] getJoins(Class<?> clazz) {
        return clazz.getDeclaredAnnotationsByType(Join.class);
    }

    /**
     * 获取外键注解
     * 
     * @param clazz
     * @return
     */
    public static ForeignKey[] getForeignKeys(Class<?> clazz) {
        return clazz.getDeclaredAnnotationsByType(ForeignKey.class);
    }

    /**
     * 获取索引注解
     * 
     * @param clazz
     * @return
     */
    public static Index[] getIndexs(Class<?> clazz) {
        return clazz.getDeclaredAnnotationsByType(Index.class);
    }

    /**
     * 获取唯一约束注解
     * 
     * @param clazz
     * @return
     */
    public static Unique[] getUniques(Class<?> clazz) {
        return clazz.getDeclaredAnnotationsByType(Unique.class);
    }

    /**
     * 获取group by注解
     * 
     * @param clazz
     * @return
     */
    public static GroupBy[] getGroupBies(Class<?> clazz) {
        return clazz.getDeclaredAnnotationsByType(GroupBy.class);
    }
}
