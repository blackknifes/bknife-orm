package com.bknife.orm.mapper;

public interface SqlColumnMapper {
    /**
     * 使用类属性名获取数据库列名
     * 
     * @param fieldName
     * @return
     */
    public String getColumnName(String fieldName);

    /**
     * 使用类数据库列名获取类属性名
     * 
     * @param columnName
     * @return
     */
    public String getFieldName(String columnName);
}
