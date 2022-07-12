package com.bknife.orm.assemble;

import java.lang.reflect.Field;

/**
 * sql工厂
 */
public interface SqlFactory {
    
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
