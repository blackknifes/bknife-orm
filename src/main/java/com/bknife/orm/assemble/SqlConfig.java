package com.bknife.orm.assemble;

import com.bknife.orm.annotion.Column.Type;
import com.bknife.orm.assemble.exception.NotSupportedException;

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
