package com.bknife.orm.assemble;

/**
 * sql值getter
 */
public interface SqlGetter {
    /**
     * 获取值
     * 
     * @param object
     * @return
     */
    public Object getValue(Object object) throws Exception;
}
