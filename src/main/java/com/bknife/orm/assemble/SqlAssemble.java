package com.bknife.orm.assemble;

import com.bknife.orm.assemble.assembled.SqlAssembled;
import com.bknife.orm.assemble.assembled.SqlAssembledQuery;
import com.bknife.orm.mapper.Updater;
import com.bknife.orm.mapper.where.Condition;

/**
 * sql组装器
 * 
 * @author bknife
 * @version 2022-07-20
 */
public interface SqlAssemble {
    /**
     * 组装建表语句
     * 
     * @return
     */
    public <T> SqlAssembled<T> assembleCreateTable(SqlTableInfo<T> tableInfo) throws Exception;

    /**
     * 组装count语句
     * 
     * @param condition
     * @return
     */
    public <T> SqlAssembled<T> assembleCount(SqlMapperInfo<T> mapperInfo, Condition condition) throws Exception;

    /**
     * 组装select语句
     * 
     * @param condition
     * @return
     */
    public <T> SqlAssembledQuery<T> assembleSelect(SqlMapperInfo<T> mapperInfo, Condition condition) throws Exception;

    /**
     * 组装delete语句
     * 
     * @param condition
     * @return
     */
    public <T> SqlAssembled<T> assembleDelete(SqlTableInfo<T> tableInfo, Condition condition) throws Exception;

    /**
     * 组装update语句
     * 
     * @param updater
     * @return
     */
    public <T> SqlAssembled<T> assembleUpdate(SqlTableInfo<T> tableInfo, Updater updater) throws Exception;

    /**
     * 组装insert语句
     * 
     * @param object
     * @return
     */
    public <T> SqlAssembled<T> assembleInsert(SqlTableInfo<T> tableInfo) throws Exception;

    /**
     * 组装replace语句
     * 
     * @param object
     * @return
     */
    public <T> SqlAssembled<T> assembleReplace(SqlTableInfo<T> tableInfo) throws Exception;
}
