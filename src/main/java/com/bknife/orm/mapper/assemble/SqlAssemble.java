package com.bknife.orm.mapper.assemble;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.bknife.orm.mapper.Updater;
import com.bknife.orm.mapper.where.Condition;

public interface SqlAssemble<T> {

    public static <T> SqlAssemble<T> create(String dbType, Class<T> tableClass) throws Exception {
        dbType = dbType.toLowerCase();
        if ("mysql".equals(dbType))
            return SqlAssembleMysql.create(tableClass);
        throw new Exception("count not be found database type: " + dbType);
    }

    /**
     * 获取结果类
     * 
     * @return
     */
    public Class<T> getResultClass();

    /**
     * 从结果集创建对象
     * 
     * @param resultSet
     * @return
     */
    public T createFromResultSet(ResultSet resultSet) throws Exception;

    /**
     * 从对象设置参数
     * 
     * @param ps
     * @param object
     */
    public void setPreparedStatementParameters(PreparedStatement ps, T object) throws Exception;

    /**
     * 组装建表语句
     * 
     * @return
     */
    public String assembleCreateTable() throws Exception;

    /**
     * 组装count语句
     * 
     * @param condition
     * @return
     */
    public String assembleCount(Condition condition) throws Exception;

    /**
     * 组装select语句
     * 
     * @param condition
     * @return
     */
    public String assembleSelect(Condition condition) throws Exception;

    /**
     * 组装delete语句
     * 
     * @param condition
     * @return
     */
    public String assembleDelete(Condition condition) throws Exception;

    /**
     * 组装update语句
     * 
     * @param updater
     * @return
     */
    public String assembleUpdate(Updater updater) throws Exception;

    /**
     * 组装insert语句
     * 
     * @param object
     * @return
     */
    public String assembleInsert() throws Exception;

    /**
     * 组装replace语句
     * 
     * @param object
     * @return
     */
    public String assembleReplace() throws Exception;
}
