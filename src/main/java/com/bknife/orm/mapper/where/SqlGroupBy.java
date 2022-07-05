package com.bknife.orm.mapper.where;

public class SqlGroupBy {
    private String name;

    /**
     * @param name
     */
    public SqlGroupBy(String name) {
        this.name = name;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
}
