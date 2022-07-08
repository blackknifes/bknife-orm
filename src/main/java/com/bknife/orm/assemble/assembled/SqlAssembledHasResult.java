package com.bknife.orm.assemble.assembled;

import java.sql.ResultSet;
import java.util.Collection;

import com.bknife.orm.assemble.SqlSetter;

public interface SqlAssembledHasResult extends SqlAssembledHasParameter {

    /**
     * 获取对应类
     * 
     * @return
     */
    public Class<?> getType();

    /**
     * 获取结果设置器列表
     * 
     * @return
     */
    public Collection<SqlSetter> getResultSetters();

    /**
     * 从结果集创建对象
     * 
     * @param resultSet
     * @return
     */
    public Object createFromResultSet(ResultSet resultSet) throws Exception;
}
