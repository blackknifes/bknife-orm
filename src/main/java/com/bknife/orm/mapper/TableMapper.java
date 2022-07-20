package com.bknife.orm.mapper;

import java.util.Collection;
import java.util.List;

import com.bknife.orm.PageResult;
import com.bknife.orm.mapper.where.Condition;

public interface TableMapper<T> {
    /**
     * 统计数据行数
     * 
     * @return
     * @throws Exception
     */
    public int total() throws Exception;

    /**
     * 统计数据行数
     * 
     * @param condition 条件
     * @return
     * @throws Exception
     */
    public int total(Condition condition) throws Exception;

    /**
     * 获取一条数据
     * 
     * @param condition 条件
     * @return
     * @throws Exception
     */
    public T get(Condition condition) throws Exception;

    /**
     * 获取一条数据
     * 
     * @param ids 主键
     * @return
     * @throws Exception
     */
    public T get(Object... ids) throws Exception;

    /**
     * 获取列表
     * 
     * @return
     * @throws Exception
     */
    public List<T> list() throws Exception;

    /**
     * 获取列表
     * 
     * @param condition
     * @return
     * @throws Exception
     */
    public List<T> list(Condition condition) throws Exception;

    /**
     * 分页查询
     * 
     * @param current
     * @param pageSize
     * @param condition
     * @return
     * @throws Exception
     */
    public PageResult<T> page(int current, int pageSize, Condition condition) throws Exception;

    /**
     * 分页查询
     * 
     * @param current
     * @param pageSize
     * @return
     * @throws Exception
     */
    public PageResult<T> page(int current, int pageSize) throws Exception;

    /**
     * 分页查询
     * 
     * @param current
     * @param condition
     * @return
     * @throws Exception
     */
    public PageResult<T> page(int current, Condition condition) throws Exception;

    /**
     * 分页查询
     * 
     * @param current
     * @return
     * @throws Exception
     */
    public PageResult<T> page(int current) throws Exception;

    /**
     * 插入数据
     * 
     * @param object
     * @throws Exception
     */
    public void insert(T object) throws Exception;

    /**
     * 批量插入数据
     * 
     * @param objects
     * @throws Exception
     */
    public void insert(Collection<T> objects) throws Exception;

    /**
     * 更新数据
     * 
     * @param updater
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
     * @throws Exception
     */
    public void replace(T object) throws Exception;

    /**
     * 批量替换数据
     * 
     * @param objects
     * @throws Exception
     */
    public void replace(Collection<T> objects) throws Exception;
}
