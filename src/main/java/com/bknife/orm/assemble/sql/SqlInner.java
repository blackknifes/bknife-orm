package com.bknife.orm.assemble.sql;

import com.bknife.orm.assemble.SqlBuffer;
import com.bknife.orm.assemble.SqlGetter;

public class SqlInner implements SqlSelect, SqlSelectColumn, SqlSelectFrom, SqlSelectJoin, SqlWhere, SqlOrderBy {
    private SqlBuffer buffer = new SqlBuffer();
    private boolean hasWhere = false;
    private boolean hasOrderBy = false;

    @Override
    public SqlFinished limit(int offset, int total) {
        buffer.append("LIMIT ").append(offset).comma().append(toString());
        return this;
    }

    @Override
    public SqlOrderBy asc(String name) {
        if (!hasOrderBy)
            hasOrderBy = true;
        else
            buffer.comma();
        buffer.name(name).space().asc();
        return this;
    }

    @Override
    public SqlOrderBy asc(String table, String name) {
        if (!hasOrderBy)
            hasOrderBy = true;
        else
            buffer.comma();
        buffer.name(table).dot().name(name).space().asc();
        return null;
    }

    @Override
    public SqlOrderBy desc(String name) {
        if (!hasOrderBy)
            hasOrderBy = true;
        else
            buffer.comma();
        buffer.name(name).space().desc();
        return null;
    }

    @Override
    public SqlOrderBy desc(String table, String name) {
        if (!hasOrderBy)
            hasOrderBy = true;
        else
            buffer.comma();
        buffer.name(table).dot().name(name).space().desc();
        return null;
    }

    @Override
    public SqlLimit endOrderBy() {
        return this;
    }

    @Override
    public SqlWhere beginSubWhere() {
        buffer.openParentheses();
        return this;
    }

    @Override
    public SqlWhere endSubWhere() {
        buffer.closeParentheses();
        return this;
    }

    @Override
    public SqlWhere and() {
        buffer.and().space();
        return this;
    }

    @Override
    public SqlWhere or() {
        buffer.or().space();
        return this;
    }

    @Override
    public SqlWhere not() {
        buffer.not().space();
        return this;
    }

    @Override
    public SqlWhere equal(String name, SqlGetter getter) {
        buffer.name(name).equal().question();
        return this;
    }

    @Override
    public SqlWhere notEqual(String name, SqlGetter getter) {
        buffer.name(name).notEqual().question();
        return this;
    }

    @Override
    public SqlWhere less(String name, SqlGetter getter) {
        buffer.name(name).less().question();
        return this;
    }

    @Override
    public SqlWhere lessEqual(String name, SqlGetter getter) {
        buffer.name(name).lessEqual().question();
        return this;
    }

    @Override
    public SqlWhere greater(String name, SqlGetter getter) {
        buffer.name(name).greater().question();
        return this;
    }

    @Override
    public SqlWhere greaterEqual(String name, SqlGetter getter) {
        buffer.name(name).greaterEqual().question();
        return this;
    }

    @Override
    public SqlWhere between(String name, SqlGetter minGetter, SqlGetter maxGetter) {
        buffer.name(name).between().space().question().space().and().space().question();
        return this;
    }

    @Override
    public SqlWhere like(String name, SqlGetter getter) {
        buffer.name(name).like().question();
        return this;
    }

    @Override
    public SqlWhere leftLike(String name, SqlGetter getter) {

        return null;
    }

    @Override
    public SqlWhere rightLike(String name, SqlGetter getter) {

        return null;
    }

    @Override
    public SqlWhere in(String name, Iterable<SqlGetter> getter) {

        return null;
    }

    @Override
    public SqlWhere in(String name, Iterable<SqlGetter> getter, int length) {

        return null;
    }

    @Override
    public SqlWhere endWhere() {

        return null;
    }

    @Override
    public SqlSelectJoin andOn(String sourceTable, String sourceName, String targetTable, String targetName) {

        return null;
    }

    @Override
    public SqlSelectJoin orOn(String sourceTable, String sourceName, String targetTable, String targetName) {

        return null;
    }

    @Override
    public SqlSelectJoin andNotOn(String sourceTable, String sourceName, String targetTable, String targetName) {

        return null;
    }

    @Override
    public SqlSelectJoin orNotOn(String sourceTable, String sourceName, String targetTable, String targetName) {

        return null;
    }

    @Override
    public SqlSelectFrom endJoin() {

        return null;
    }

    @Override
    public SqlSelectJoin leftJoin(String table) {

        return null;
    }

    @Override
    public SqlSelectJoin rightJoin(String table) {

        return null;
    }

    @Override
    public SqlSelectJoin innerJoin(String table) {

        return null;
    }

    @Override
    public SqlSelectJoin outterJoin(String table) {

        return null;
    }

    @Override
    public SqlWhere where() {

        return null;
    }

    @Override
    public SqlOrderBy orderBy() {

        return null;
    }

    @Override
    public SqlSelectColumn name(String name) {

        return null;
    }

    @Override
    public SqlSelectColumn name(String table, String name) {

        return null;
    }

    @Override
    public SqlSelectColumn distinctName(String name) {

        return null;
    }

    @Override
    public SqlSelectColumn distinctName(String table, String name) {

        return null;
    }

    @Override
    public SqlSelectColumn nameAlias(String name, String alias) {

        return null;
    }

    @Override
    public SqlSelectColumn nameAlias(String table, String name, String alias) {

        return null;
    }

    @Override
    public SqlSelectColumn sqlAlias(String sql, String alias) {

        return null;
    }

    @Override
    public SqlSelectColumn distinctNameAlias(String name, String alias) {

        return null;
    }

    @Override
    public SqlSelectColumn distinctNameAlias(String table, String name, String alias) {

        return null;
    }

    @Override
    public SqlSelectColumn distinctSqlAlias(String sql, String alias) {

        return null;
    }

    @Override
    public SqlSelectFrom from(String table) {

        return null;
    }

    @Override
    public SqlSelectInto into(String table) {

        return null;
    }

}
