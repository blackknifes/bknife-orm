package com.bknife.orm.assemble;

/**
 * assemble工厂
 */
public interface SqlAssembleFactory {
    /**
     * 获取组装器
     * 
     * @return
     */
    public SqlAssemble getAssemble(SqlContext context) throws Exception;
}
