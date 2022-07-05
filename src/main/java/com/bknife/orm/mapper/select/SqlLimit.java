package com.bknife.orm.mapper.select;

public class SqlLimit {
    private int offset; // 偏移
    private int total; // 查询数

    public SqlLimit(int offset, int total) {
        this.offset = offset;
        this.total = total;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
