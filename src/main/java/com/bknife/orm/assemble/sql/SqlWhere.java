package com.bknife.orm.assemble.sql;

import com.bknife.orm.assemble.SqlGetter;

public interface SqlWhere extends SqlFinished {
    public SqlWhere beginSubWhere();

    public SqlWhere endSubWhere();

    public SqlWhere and();

    public SqlWhere or();

    public SqlWhere not();

    public SqlWhere equal(String name, SqlGetter getter);

    public SqlWhere notEqual(String name, SqlGetter getter);

    public SqlWhere less(String name, SqlGetter getter);

    public SqlWhere lessEqual(String name, SqlGetter getter);

    public SqlWhere greater(String name, SqlGetter getter);

    public SqlWhere greaterEqual(String name, SqlGetter getter);

    public SqlWhere between(String name, SqlGetter minGetter, SqlGetter maxGetter);

    public SqlWhere like(String name, SqlGetter getter);

    public SqlWhere leftLike(String name, SqlGetter getter);

    public SqlWhere rightLike(String name, SqlGetter getter);

    public SqlWhere in(String name, Iterable<SqlGetter> getter);

    public SqlWhere in(String name, Iterable<SqlGetter> getter, int length);

    public SqlWhere endWhere();
}
