package com.bknife.orm.assemble;

import com.bknife.orm.assemble.assembled.SqlAssembledHasResult;
import com.bknife.orm.assemble.assembled.SqlAssembledHasParameter;
import com.bknife.orm.mapper.Updater;
import com.bknife.orm.mapper.where.Condition;

/**
 * sql组装器
 */
public interface SqlAssemble<T> {
    /**
     * 获取结果类
     * 
     * @return
     */
    public Class<T> getResultType();

    /**
     * 组装建表语句
     * 
     * @return
     */
    public SqlAssembledHasParameter assembleCreateTable() throws Exception;

    /**
     * 组装count语句
     * 
     * @param condition
     * @return
     */
    public SqlAssembledHasParameter assembleCount(Condition condition) throws Exception;

    /**
     * 组装select语句
     * 
     * @param condition
     * @return
     */
    public SqlAssembledHasResult assembleSelect(Condition condition) throws Exception;

    /**
     * 组装delete语句
     * 
     * @param condition
     * @return
     */
    public SqlAssembledHasParameter assembleDelete(Condition condition) throws Exception;

    /**
     * 组装update语句
     * 
     * @param updater
     * @return
     */
    public SqlAssembledHasParameter assembleUpdate(Updater updater) throws Exception;

    /**
     * 组装insert语句
     * 
     * @param object
     * @return
     */
    public SqlAssembledHasParameter assembleInsert() throws Exception;

    /**
     * 组装replace语句
     * 
     * @param object
     * @return
     */
    public SqlAssembledHasParameter assembleReplace() throws Exception;
}
