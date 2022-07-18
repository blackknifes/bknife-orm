package com.bknife.orm.mapper;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;

import javax.sql.DataSource;

import com.bknife.orm.PageResult;
import com.bknife.orm.assemble.SqlAssemble;
import com.bknife.orm.assemble.SqlColumnInfo;
import com.bknife.orm.assemble.SqlTableInfo;
import com.bknife.orm.assemble.assembled.SqlAssembled;
import com.bknife.orm.assemble.assembled.SqlAssembledQuery;
import com.bknife.orm.mapper.where.Condition;

public class TableMapper<T> implements Updatable<T> {
    private SqlAssemble assemble; // 组装器
    private DataSource dataSource; // 数据源
    private SqlTableInfo<T> tableInfo;

    public static <T> TableMapper<T> create(SqlAssemble assemble, DataSource dataSource, SqlTableInfo<T> tableInfo)
            throws Exception {
        return new TableMapper<>(assemble, dataSource, tableInfo);
    }

    private TableMapper(SqlAssemble assemble, DataSource dataSource, SqlTableInfo<T> tableInfo) throws Exception {
        this.assemble = assemble;
        this.dataSource = dataSource;
        this.tableInfo = tableInfo;

        try (SqlConnection connection = preparedStatement(assemble.assembleCreateTable(tableInfo))) {
            connection.executeUpdate();
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public int total() throws Exception {
        return total(null);
    }

    @Override
    public int total(Condition condition) throws Exception {
        SqlAssembled<T> assembled = assemble.assembleCount(tableInfo, condition);
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
        SqlAssembledQuery<T> assembled = assemble.assembleSelect(tableInfo, condition);
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
        for (SqlColumnInfo columnInfo : tableInfo.getPrimaryKeys())
            condition.addEqual(columnInfo.getName(), ids[i++]);
        return get(condition);
    }

    @Override
    public Collection<T> list() throws Exception {
        return list((Condition) null);
    }

    @Override
    public Collection<T> list(Condition condition) throws Exception {
        SqlAssembledQuery<T> assembled = assemble.assembleSelect(tableInfo, condition);
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
        int total = total();
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

    @Override
    public void insert(T object) throws Exception {
        SqlAssembled<T> assembled = assemble.assembleInsert(tableInfo);
        try (SqlConnection connection = preparedStatement(assembled)) {
            assembled.setParameter(connection.getPreparedStatement(), object);
            if (connection.executeUpdate() <= 0)
                throw new Exception("insert [" + tableInfo.getTableName() + "] failure");
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void insert(Collection<T> objects) throws Exception {
        int insertTotal = 0;
        SqlAssembled<T> assembled = assemble.assembleInsert(tableInfo);
        try (SqlConnection connection = preparedStatement(assembled)) {
            try {
                for (T object : objects) {
                    assembled.setParameter(connection.getPreparedStatement(), object);
                    insertTotal += connection.executeUpdate();
                }
            } catch (Exception e) {
            }
            if (insertTotal != objects.size())
                throw new Exception("batch insert [" + tableInfo.getTableName() + "] failure");
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void update(Updater updater) throws Exception {
        SqlAssembled<T> assembled = assemble.assembleUpdate(tableInfo, updater);
        try (SqlConnection connection = preparedStatement(assembled)) {
            assembled.setParameter(connection.getPreparedStatement(), null);
            if (connection.executeUpdate() <= 0)
                throw new Exception("update [" + tableInfo.getTableName() + "] failure");
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public int delete(Condition condition) throws Exception {
        SqlAssembled<T> assembled = assemble.assembleDelete(tableInfo, condition);
        try (SqlConnection connection = preparedStatement(assembled)) {
            assembled.setParameter(connection.getPreparedStatement(), null);
            return connection.executeUpdate();
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void replace(T object) throws Exception {
        SqlAssembled<T> assembled = assemble.assembleReplace(tableInfo);
        try (SqlConnection connection = preparedStatement(assembled)) {
            assembled.setParameter(connection.getPreparedStatement(), object);
            if (connection.executeUpdate() <= 0)
                throw new Exception("replace [" + tableInfo.getTableName() + "] failure");
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void replace(Collection<T> objects) throws Exception {
        int insertTotal = 0;
        SqlAssembled<T> assembled = assemble.assembleReplace(tableInfo);
        try (SqlConnection connection = preparedStatement(assembled)) {
            try {
                for (T object : objects) {
                    assembled.setParameter(connection.getPreparedStatement(), object);
                    insertTotal += connection.executeUpdate();
                }
            } catch (Exception e) {
            }
            if (insertTotal != objects.size())
                throw new Exception("batch replace [" + tableInfo.getTableName() + "] failure");
        } catch (Exception e) {
            throw e;
        }
    }

    private SqlConnection preparedStatement(SqlAssembled<T> assembled) throws Exception {
        return SqlConnection.Create(dataSource, assembled.getSql());
    }
}
