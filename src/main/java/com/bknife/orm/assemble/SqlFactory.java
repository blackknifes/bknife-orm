package com.bknife.orm.assemble;

/**
 * sql工厂
 */
public interface SqlFactory {

    /**
     * 添加支持
     * 
     * @param factory
     */
    public void addSupported(String sqlType, SqlAssembleFactory factory);

    /**
     * 创建组装器
     * 
     * @param mapperClass
     * @return
     * @throws Exception
     */
    public SqlAssemble getAssemble(SqlContext context, Class<?> mapperClass, String sqlType) throws Exception;

    /**
     * 创建mapper info
     * 
     * @param mapperClass
     * @return
     * @throws Exception
     */
    public SqlMapperInfo getMapperInfo(SqlContext context, Class<?> mapperClass) throws Exception;
}
