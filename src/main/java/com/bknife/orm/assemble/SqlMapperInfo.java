package com.bknife.orm.assemble;

public interface SqlMapperInfo<T> {
    /**
     * 获取映射类
     * 
     * @return
     */
    public Class<T> getMapperClass();

    /**
     * 获取主表名
     * 
     * @return
     */
    public String getTableName();

    /**
     * 获取所有列
     * 
     * @return
     */
    public Iterable<SqlColumnInfo> getColumns();

    /**
     * 按名称获取列信息
     * 
     * @param name
     * @return
     */
    public SqlColumnInfo getColumn(String name);

    /**
     * 获取所有列
     * 
     * @return
     */
    public Iterable<SqlTableColumnInfo> getPrimaryKeys();
}
