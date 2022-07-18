package com.bknife.orm.mapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceUtils;

public class SqlConnection implements AutoCloseable {
    private DataSource dataSource;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @Override
    public void close() throws Exception {
        if (resultSet != null)
            resultSet.close();
        if (preparedStatement != null)
            preparedStatement.close();
        if (connection != null && !DataSourceUtils.isConnectionTransactional(connection, dataSource))
            DataSourceUtils.releaseConnection(connection, dataSource);
    }

    public static SqlConnection Create(DataSource dataSource, String sql) throws Exception {
        SqlConnection conn = new SqlConnection(dataSource);
        try {
            conn.preparedStatement(sql);
        } catch (Exception e) {
            if (conn != null)
                conn.close();
            throw e;
        }
        return conn;
    }

    private SqlConnection(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public SqlConnection preparedStatement(String sql) throws SQLException {
        connection = DataSourceUtils.getConnection(dataSource);
        preparedStatement = connection.prepareStatement(sql);
        return this;
    }

    public SqlConnection executeQuery() throws SQLException {
        resultSet = preparedStatement.executeQuery();
        return this;
    }

    public int executeUpdate() throws SQLException {
        return preparedStatement.executeUpdate();
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public Connection getConnection() {
        return connection;
    }

    public PreparedStatement getPreparedStatement() {
        return preparedStatement;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public int getTotal() throws SQLException {
        resultSet.next();
        return resultSet.getInt(1);
    }
}
