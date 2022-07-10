package com.bknife.orm.assemble;

import com.bknife.orm.assemble.assembled.SqlAssembled;
import com.bknife.orm.assemble.assembled.SqlAssembledQuery;
import com.bknife.orm.mapper.Updater;
import com.bknife.orm.mapper.where.Condition;

/**
 * sql组装器
 */
public interface SqlAssemble {
    /**
     * 获取结果类
     * 
     * @return
     */
    public Class<?> getResultType();

    /**
     * 组装建表语句
     * 
     * @return
     */
    public SqlAssembled assembleCreateTable() throws Exception;

    /**
     * 组装count语句
     * 
     * @param condition
     * @return
     */
    public SqlAssembled assembleCount(Condition condition) throws Exception;

    /**
     * 组装select语句
     * 
     * @param condition
     * @return
     */
    public SqlAssembledQuery assembleSelect(Condition condition) throws Exception;

    /**
     * 组装delete语句
     * 
     * @param condition
     * @return
     */
    public SqlAssembled assembleDelete(Condition condition) throws Exception;

    /**
     * 组装update语句
     * 
     * @param updater
     * @return
     */
    public SqlAssembled assembleUpdate(Updater updater) throws Exception;

    /**
     * 组装insert语句
     * 
     * @param object
     * @return
     */
    public SqlAssembled assembleInsert() throws Exception;

    /**
     * 组装replace语句
     * 
     * @param object
     * @return
     */
    public SqlAssembled assembleReplace() throws Exception;
}
