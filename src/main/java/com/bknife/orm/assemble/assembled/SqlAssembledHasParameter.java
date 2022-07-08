package com.bknife.orm.assemble.assembled;

import java.sql.PreparedStatement;
import java.util.Collection;

import com.bknife.orm.assemble.SqlGetter;

public interface SqlAssembledHasParameter {
    /**
     * 获取sql语句
     * 
     * @return
     */
    public String getSql();

    /**
     * 获取参数获取器列表
     * 
     * @return
     */
    public Collection<SqlGetter> getParameterGetters();

    /**
     * 设置参数
     * 
     * @param preparedStatement
     */
    public void setParameter(PreparedStatement preparedStatement, Object object) throws Exception;
}
