package com.bknife.orm.mapper;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;

import javax.sql.DataSource;

import com.bknife.orm.PageResult;
import com.bknife.orm.assemble.SqlAssemble;
import com.bknife.orm.assemble.SqlColumnInfo;
import com.bknife.orm.assemble.SqlViewInfo;
import com.bknife.orm.assemble.assembled.SqlAssembled;
import com.bknife.orm.assemble.assembled.SqlAssembledQuery;
import com.bknife.orm.mapper.where.Condition;

public class ViewMapper<T> implements Selectable<T> {
    private SqlAssemble assemble; // 组装器
    private DataSource dataSource; // 数据源
    private SqlViewInfo<T> viewInfo;

    public static <T> ViewMapper<T> create(SqlAssemble assemble, DataSource dataSource, SqlViewInfo<T> viewInfo) {
        return new ViewMapper<>(assemble, dataSource, viewInfo);
    }

    private ViewMapper(SqlAssemble assemble, DataSource dataSource, SqlViewInfo<T> viewInfo) {
        this.assemble = assemble;
        this.dataSource = dataSource;
        this.viewInfo = viewInfo;
    }

    @Override
    public int total() throws Exception {
        return total(null);
    }

    @Override
    public int total(Condition condition) throws Exception {
        SqlAssembled<T> assembled = assemble.assembleCount(viewInfo, condition);
        try (SqlConnection connection = preparedStatement(assembled)) {
            assembled.setParameter(connection.getPreparedStatement(), null);
            return connection.executeQuery().getTotal();
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public T get(Condition condition) throws Exception {
        condition.limit(0, 1);
        SqlAssembledQuery<T> assembled = assemble.assembleSelect(viewInfo, condition);
        try (SqlConnection connection = preparedStatement(assembled)) {
            assembled.setParameter(connection.getPreparedStatement(), null);
            ResultSet resultSet = connection.executeQuery().getResultSet();
            if (!resultSet.next())
                return null;
            return assembled.createFromResultSet(resultSet);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public T get(Object... ids) throws Exception {
        Condition condition = new Condition();
        int i = 0;
        for (SqlColumnInfo columnInfo : viewInfo.getPrimaryKeys())
            condition.addEqual(columnInfo.getName(), ids[i++]);
        return get(condition);
    }

    @Override
    public Collection<T> list() throws Exception {
        return list((Condition) null);
    }

    @Override
    public Collection<T> list(Condition condition) throws Exception {
        SqlAssembledQuery<T> assembled = assemble.assembleSelect(viewInfo, condition);
        try (SqlConnection connection = preparedStatement(assembled)) {
            assembled.setParameter(connection.getPreparedStatement(), null);
            ResultSet resultSet = connection.executeQuery().getResultSet();
            Collection<T> list = new ArrayList<>();
            while (resultSet.next())
                list.add(assembled.createFromResultSet(resultSet));
            return list;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public PageResult<T> page(int current, int pageSize, Condition condition) throws Exception {
        if (current < 1)
            throw new IllegalArgumentException("current page is illegal: " + current);
        if (condition == null)
            condition = new Condition();
        int total = total(condition);
        condition.limit((current - 1) * pageSize, pageSize);
        return new PageResult<>(current, pageSize, total, list(condition));
    }

    @Override
    public PageResult<T> page(int current, int pageSize) throws Exception {
        return page(current, pageSize, null);
    }

    @Override
    public PageResult<T> page(int current, Condition condition) throws Exception {
        return page(current, 15, condition);
    }

    @Override
    public PageResult<T> page(int current) throws Exception {
        return page(current, 15, null);
    }

    private SqlConnection preparedStatement(SqlAssembled<T> assembled) throws Exception {
        return SqlConnection.Create(dataSource, assembled.getSql());
    }
}
