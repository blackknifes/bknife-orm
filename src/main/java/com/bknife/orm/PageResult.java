package com.bknife.orm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 分页结果
 */
public class PageResult<T> {
    private int current = 1;
    private int pageSize = 15;
    private int total = 0;
    private Collection<T> records;

    public static void main(String[] args) throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/test?useSSL=false",
                "root",
                "root");) {

            PreparedStatement ps = connection.prepareStatement("SELECT * from sys_company where id in (?,?)");
            ps.setObject(1, "202206281129466490000001");
            ps.setObject(2, "202206281129466490000002");
            ResultSet rs = ps.executeQuery();
            ResultSetMetaData resultSetMetaData = rs.getMetaData();
            for (int i = 0; i < resultSetMetaData.getColumnCount(); i++) {
                System.out.println(resultSetMetaData.getColumnLabel(i + 1));
            }
            if (rs.next())
                System.out.println(rs.getString(1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PageResult(int current, int pageSize, int total, Collection<T> records) {
        this.current = current;
        this.pageSize = pageSize;
        this.total = total;
        this.records = records;
    }

    /**
     * 获取当前页，从1开始
     * 
     * @return
     */
    public int getCurrent() {
        return current;
    }

    /**
     * 获取分页尺寸
     * 
     * @return
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * 获取总条数
     * 
     * @return
     */
    public int getTotal() {
        return total;
    }

    /**
     * 获取行偏移
     * 
     * @return
     */
    public int getOffset() {
        int currentPage = getCurrent();
        return (currentPage == 0 ? currentPage : currentPage - 1) * getPageSize();
    }

    /**
     * 获取记录列表
     * 
     * @return
     */
    public Collection<T> getRecords() {
        return records;
    }
}
