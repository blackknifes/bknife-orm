package com.bknife.orm.mapper;

import java.util.Collection;
import java.util.List;

import com.bknife.orm.PageResult;
import com.bknife.orm.mapper.where.Condition;

public class MapperServiceBase<T> implements MapperService<T> {

    private Mapper<T> mapper;

    public MapperServiceBase(Mapper<T> mapper) {
        this.mapper = mapper;
    }

    @Override
    public int total() throws Exception {
        return mapper.total();
    }

    @Override
    public int total(Condition condition) throws Exception {
        return mapper.total(condition);
    }

    @Override
    public T get(Condition condition) throws Exception {
        return mapper.get(condition);
    }

    @Override
    public T get(Object... ids) throws Exception {
        return mapper.get(ids);
    }

    @Override
    public List<T> list() throws Exception {
        return mapper.list();
    }

    @Override
    public List<T> list(Condition condition) throws Exception {
        return mapper.list(condition);
    }

    @Override
    public PageResult<T> page(int current, int pageSize, Condition condition) throws Exception {
        return mapper.page(current, pageSize, condition);
    }

    @Override
    public PageResult<T> page(int current, int pageSize) throws Exception {
        return mapper.page(current, pageSize);
    }

    @Override
    public PageResult<T> page(int current, Condition condition) throws Exception {
        return mapper.page(current, condition);
    }

    @Override
    public PageResult<T> page(int current) throws Exception {
        return mapper.page(current);
    }

    @Override
    public void insert(T object) throws Exception {
        mapper.insert(object);
    }

    @Override
    public void insert(Collection<T> objects) throws Exception {
        mapper.insert(objects);
    }

    @Override
    public void update(Updater updater) throws Exception {
        mapper.update(updater);
    }

    @Override
    public int delete(Condition condition) throws Exception {
        return mapper.delete(condition);
    }

    @Override
    public void replace(T object) throws Exception {
        mapper.replace(object);
    }

    @Override
    public void replace(Collection<T> objects) throws Exception {
        mapper.replace(objects);
    }

}
