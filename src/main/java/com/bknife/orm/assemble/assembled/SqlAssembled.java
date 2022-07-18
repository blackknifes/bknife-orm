package com.bknife.orm.assemble.assembled;

import java.sql.PreparedStatement;

public interface SqlAssembled<T> {
    /**
     * 获取sql语句
     * 
     * @return
     */
    public String getSql();

    /**
     * 从对象属性设置参数
     * 
     * @param preparedStatement
     */
    public void setParameter(PreparedStatement preparedStatement, T object) throws Exception;
}
