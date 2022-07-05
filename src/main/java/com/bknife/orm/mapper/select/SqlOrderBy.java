package com.bknife.orm.mapper.select;

public abstract class SqlOrderBy {
    private String column; // 排序列

    public SqlOrderBy(String column) {
        this.column = column;
    }

    /**
     * 是否是降序排列
     * 
     * @return
     */
    public abstract boolean isDesc();

    public boolean isAsc() {
        return !isDesc();
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }
}
