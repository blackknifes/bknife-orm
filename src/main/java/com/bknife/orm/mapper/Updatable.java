package com.bknife.orm.mapper;

import java.util.Collection;

import com.bknife.orm.mapper.where.Condition;

public interface Updatable<T> extends Selectable<T> {
    /**
     * 插入数据
     * 
     * @param object
     * @return
     * @throws Exception
     */
    public void insert(T object) throws Exception;

    /**
     * 插入数据
     * 
     * @param objects
     * @return
     * @throws Exception
     */
    public void insert(Collection<T> objects) throws Exception;

    /**
     * 更新数据
     * 
     * @param updater
     * @return
     * @throws Exception
     */
    public void update(Updater updater) throws Exception;

    /**
     * 删除数据
     * 
     * @param condition
     * @return
     * @throws Exception
     */
    public int delete(Condition condition) throws Exception;

    /**
     * 替换数据
     * 
     * @param object
     * @return
     * @throws Exception
     */
    public void replace(T object) throws Exception;

    /**
     * 替换数据
     * 
     * @param objects
     * @return
     * @throws Exception
     */
    public void replace(Collection<T> objects) throws Exception;
}
