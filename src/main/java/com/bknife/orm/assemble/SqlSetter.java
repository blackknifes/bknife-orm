package com.bknife.orm.assemble;

/**
 * sql值setter
 */
public interface SqlSetter {

    /**
     * 设置值
     * 
     * @param object
     * @param value
     */
    public void setValue(Object object, Object value) throws Exception;
}
