package com.bknife.orm.assemble;

import com.bknife.orm.annotion.Column.Type;
import com.bknife.orm.assemble.exception.NotSupportedException;
import com.bknife.orm.assemble.exception.SqlIllegalNameException;

/**
 * sql配置
 * 
 * @author bknife
 * @version 2022-07-20
 */
public interface SqlConfig {
    /**
     * 获取sql类型
     * 
     * @param type
     * @return
     */
    public String getSqlType(Type type) throws NotSupportedException;

    /**
     * 获取默认类到列类型的映射
     * 
     * @param clazz
     * @return
     * @throws NotSupportedException
     */
    public Type getColumnType(Class<?> clazz) throws NotSupportedException;

    /**
     * 获取从类名解析的表名
     * 
     * @param className
     * @throws SqlIllegalNameException
     * @return
     */
    public String getTableName(String className) throws SqlIllegalNameException;

    /**
     * 获取从表名解析的类名
     * 
     * @param tableName
     * @throws SqlIllegalNameException
     * @return
     */
    public String getClassName(String tableName) throws SqlIllegalNameException;

    /**
     * 获取从字段名解析的列名
     * 
     * @param fieldName
     * @throws SqlIllegalNameException
     * @return
     */
    public String getColumnName(String fieldName) throws SqlIllegalNameException;

    /**
     * 获取从列名解析的字段名
     * 
     * @param columnName
     * @throws SqlIllegalNameException
     * @return
     */
    public String getFieldName(String columnName) throws SqlIllegalNameException;

    /**
     * 获取表默认字符集
     * 
     * @return
     */
    public String getTableCharset();

    /**
     * 获取表默认排序规则
     * 
     * @return
     */
    public String getTableCollate();

    /**
     * 获取type类型默认长度
     * 
     * @param type
     * @return
     */
    public int getLength(Type type);

    /**
     * 获取type类型默认小数点位数
     * 
     * @param type
     * @return
     */
    public int getDot(Type type);
}
