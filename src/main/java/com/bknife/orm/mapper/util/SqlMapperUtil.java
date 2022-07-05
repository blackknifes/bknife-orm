package com.bknife.orm.mapper.util;

import com.bknife.orm.annotion.DBColumn;
import com.bknife.orm.annotion.DBForeignKey;
import com.bknife.orm.annotion.DBGroupBy;
import com.bknife.orm.annotion.DBIndex;
import com.bknife.orm.annotion.DBJoin;
import com.bknife.orm.annotion.DBTable;
import com.bknife.orm.annotion.DBUnique;

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
            if (clazz.getDeclaredAnnotation(DBTable.class) != null)
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
    public static DBTable getTableAnnotation(Class<?> clazz) {
        return clazz.getDeclaredAnnotation(DBTable.class);
    }

    /**
     * 获取表注解
     * 
     * @param clazz
     * @return
     */
    public static DBTable findTableAnnotation(Class<?> clazz) {
        while (clazz != Object.class) {
            DBTable table = clazz.getDeclaredAnnotation(DBTable.class);
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
    public static String getTableName(DBForeignKey foreignKey) {
        return foreignKey.tableClass() == Object.class ? foreignKey.table() : findTableName(foreignKey.tableClass());
    }

    /**
     * 获取表名
     * 
     * @param groupBy
     * @return
     */
    public static String getTableName(DBGroupBy groupBy) {
        return groupBy.tableClass() == Object.class ? groupBy.table() : findTableName(groupBy.tableClass());
    }

    /**
     * 获取表名
     * 
     * @param join
     * @return
     */
    public static String getTableName(DBJoin join) {
        return join.tableClass() == Object.class ? join.table() : findTableName(join.tableClass());
    }

    /**
     * 获取所有join连接
     * 
     * @param clazz
     * @return
     */
    public static DBJoin[] getJoins(Class<?> clazz) {
        return clazz.getDeclaredAnnotationsByType(DBJoin.class);
    }

    /**
     * 获取外键注解
     * 
     * @param clazz
     * @return
     */
    public static DBForeignKey[] getForeignKeys(Class<?> clazz) {
        return clazz.getDeclaredAnnotationsByType(DBForeignKey.class);
    }

    /**
     * 获取索引注解
     * 
     * @param clazz
     * @return
     */
    public static DBIndex[] getIndexs(Class<?> clazz) {
        return clazz.getDeclaredAnnotationsByType(DBIndex.class);
    }

    /**
     * 获取唯一约束注解
     * 
     * @param clazz
     * @return
     */
    public static DBUnique[] getUniques(Class<?> clazz) {
        return clazz.getDeclaredAnnotationsByType(DBUnique.class);
    }

    /**
     * 获取group by注解
     * 
     * @param clazz
     * @return
     */
    public static DBGroupBy[] getGroupBies(Class<?> clazz) {
        return clazz.getDeclaredAnnotationsByType(DBGroupBy.class);
    }
}
