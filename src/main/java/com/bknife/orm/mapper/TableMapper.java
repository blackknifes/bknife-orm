package com.bknife.orm.mapper;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import com.bknife.orm.PageResult;
import com.bknife.orm.annotion.Column;
import com.bknife.orm.assemble.SqlAssemble;
import com.bknife.orm.mapper.where.Condition;

public class TableMapper<T> implements Mapper<T> {
    private DataSource dataSource;
    private SqlAssemble assemble;
    private ArrayList<String> primaryKeys;
    private boolean showSql = false;

    public TableMapper(SqlAssemble assemble, DataSource dataSource, boolean showSql) {
        this.assemble = assemble;
        this.dataSource = dataSource;
        this.showSql = showSql;

        ArrayList<String> keys = new ArrayList<String>();
        Class<?> clazz = assemble.getResultType();
        while (clazz != Object.class) {
            for (Field field : clazz.getDeclaredFields()) {
                Column column = field.getDeclaredAnnotation(Column.class);
                if (column == null || !column.primaryKey())
                    continue;
                keys.add(field.getName());
            }
            clazz = clazz.getSuperclass();
        }
        this.primaryKeys = keys;
    }

    @Override
    public boolean isSelectable() {
        return true;
    }

    @Override
    public boolean isCreatable() {
        return true;
    }

    @Override
    public boolean isInsertable() {
        return true;
    }

    @Override
    public boolean isUpdatable() {
        return true;
    }

    @Override
    public boolean isDeletable() {
        return true;
    }

    @Override
    public void create() throws Exception {
        try (SqlConnection conn = getConnection(assemble.assembleCreateTable())) {
            if (conn.executeUpdate() != 0)
                throw new Exception("create table fail");
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
        try (SqlConnection conn = getConnection(assemble.assembleCount(condition))) {
            conn.executeQuery();
            ResultSet resultSet = conn.getResultSet();
            if (resultSet.next())
                return resultSet.getInt(1);
            throw new Exception(" total failure");
        } catch (Exception e) {
            throw e;
        }

    }

    @Override
    public T get(Condition condition) throws Exception {
        if (condition == null)
            condition = new Condition();
        condition.limit(0, 1);
        try (SqlConnection conn = getConnection(assemble.assembleSelect(condition))) {
            conn.executeQuery();
            ResultSet resultSet = conn.getResultSet();
            if (resultSet.next())
                return assemble.createFromResultSet(resultSet);
            return null;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public T get(Object... ids) throws Exception {
        if (primaryKeys.size() != ids.length)
            throw new IllegalArgumentException("length of ids is not equal length of primaryKeys");
        Condition condition = new Condition();
        for (int i = 0; i < ids.length; ++i)
            condition.addEqual(primaryKeys.get(i), ids[i]);
        return get(condition);
    }

    @Override
    public List<T> list() throws Exception {
        return list(null);
    }

    @Override
    public List<T> list(Condition condition) throws Exception {
        try (SqlConnection conn = getConnection(assemble.assembleSelect(condition))) {
            conn.executeQuery();
            ResultSet resultSet = conn.getResultSet();
            List<T> list = new ArrayList<T>();
            while (resultSet.next())
                list.add(assemble.createFromResultSet(resultSet));
            return list;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public PageResult<T> page(int current, int pageSize, Condition condition) throws Exception {
        int total = total(condition);
        List<T> list = new ArrayList<T>();
        condition.limit((current - 1) * pageSize, pageSize);
        try (SqlConnection conn = getConnection(assemble.assembleSelect(condition))) {
            conn.executeQuery();
            ResultSet resultSet = conn.getResultSet();
            while (resultSet.next())
                list.add(assemble.createFromResultSet(resultSet));
            return new PageResult<T>(current, pageSize, total, list);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public PageResult<T> page(int current, int pageSize) throws Exception {
        return page(current, pageSize);
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
        try (SqlConnection conn = getConnection(assemble.assembleInsert())) {
            assemble.setPreparedStatementParameters(conn.getPreparedStatement(), object);
            if (conn.executeUpdate() <= 0)
                throw new Exception("insert fail");
        } catch (Exception e) {
            throw e;
        }

    }

    @Override
    public void insert(Collection<T> objects) throws Exception {
        try (SqlConnection conn = getConnection(assemble.assembleInsert())) {
            for (T object : objects) {
                assemble.setPreparedStatementParameters(conn.getPreparedStatement(), object);
                if (conn.executeUpdate() <= 0)
                    throw new Exception("insert fail");
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void update(Updater updater) throws Exception {
        try (SqlConnection conn = getConnection(assemble.assembleUpdate(updater))) {
            if (conn.executeUpdate() <= 0)
                throw new Exception("update fail");
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public int delete(Condition condition) throws Exception {
        return getConnection(assemble.assembleDelete(condition)).executeUpdate();
    }

    @Override
    public void replace(T object) throws Exception {
        try (SqlConnection conn = getConnection(assemble.assembleReplace())) {
            assemble.setPreparedStatementParameters(conn.getPreparedStatement(), object);
            if (conn.executeUpdate() <= 0)
                throw new Exception("insert fail");
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void replace(Collection<T> objects) throws Exception {
        try (SqlConnection conn = getConnection(assemble.assembleReplace())) {
            for (T object : objects) {
                assemble.setPreparedStatementParameters(conn.getPreparedStatement(), object);
                if (conn.executeUpdate() <= 0)
                    throw new Exception("replace fail");
            }
        } catch (Exception e) {
            throw e;
        }
    }

    private SqlConnection getConnection(String sql) throws Exception {
        if (showSql)
            System.out.println(sql);
        return SqlConnection.Create(dataSource, sql);
    }
}
