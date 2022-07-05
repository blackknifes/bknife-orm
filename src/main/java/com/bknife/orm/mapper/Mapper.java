package com.bknife.orm.mapper;

import java.util.Collection;
import java.util.List;

import com.bknife.orm.PageResult;
import com.bknife.orm.mapper.where.Condition;

public interface Mapper<T> {
    /**
     * 是否可查询
     * 
     * @return
     */
    public boolean isSelectable();

    /**
     * 是否可创建
     */
    public boolean isCreatable();

    /**
     * 是否可插入或替换
     * 
     * @return
     */
    public boolean isInsertable();

    /**
     * 是否可更新
     * 
     * @return
     */
    public boolean isUpdatable();

    /**
     * 是否可删除
     * 
     * @return
     */
    public boolean isDeletable();

    /**
     * 创建表
     * 
     * @throws Exception
     */
    public void create() throws Exception;

    /**
     * 获取数据总行数
     * 
     * @param condition
     * @return
     * @throws Exception
     */
    public int total() throws Exception;

    /**
     * 获取数据总行数
     * 
     * @param condition
     * @return
     * @throws Exception
     */
    public int total(Condition condition) throws Exception;

    /**
     * 获取单条数据
     * 
     * @param condition
     * @return
     * @throws Exception
     */
    public T get(Condition condition) throws Exception;

    /**
     * 使用主键获取单条数据
     * 
     * @param values
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
     * @param current   当前页
     * @param pageSize  页尺寸
     * @param condition 条件
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
     * 分页查询，15页尺寸
     * 
     * @param current
     * @param condition
     * @return
     * @throws Exception
     */
    public PageResult<T> page(int current, Condition condition) throws Exception;

    /**
     * 分页查询，15页尺寸
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
