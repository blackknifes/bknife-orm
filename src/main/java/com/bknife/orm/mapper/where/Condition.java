package com.bknife.orm.mapper.where;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.bknife.orm.mapper.select.SqlLimit;
import com.bknife.orm.mapper.select.SqlOrderBy;
import com.bknife.orm.mapper.select.SqlOrderByAsc;
import com.bknife.orm.mapper.select.SqlOrderByDesc;
import com.bknife.orm.mapper.where.annotion.ColumnMap;
import com.bknife.orm.mapper.where.annotion.Equal;
import com.bknife.orm.mapper.where.annotion.Greater;
import com.bknife.orm.mapper.where.annotion.GreaterEqual;
import com.bknife.orm.mapper.where.annotion.In;
import com.bknife.orm.mapper.where.annotion.LeftLike;
import com.bknife.orm.mapper.where.annotion.Less;
import com.bknife.orm.mapper.where.annotion.LessEqual;
import com.bknife.orm.mapper.where.annotion.Like;
import com.bknife.orm.mapper.where.annotion.NotEqual;
import com.bknife.orm.mapper.where.annotion.RightLike;

/**
 * 查询条件
 */
public class Condition implements SqlWhere {
    private List<SqlWhere> wheres;
    private List<SqlOrderBy> orderList;
    private SqlLimit limit;

    /**
     * 通过dto类来创建条件对象
     * 
     * @param dto
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static Condition createByDto(Object dto) throws IllegalArgumentException, IllegalAccessException {
        return new Condition().addByDto(dto);
    }

    /**
     * 通过dto类来添加条件
     * 
     * @param dto
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public <T> Condition addByDto(T dto) throws IllegalArgumentException, IllegalAccessException {
        Class<?> clazz = dto.getClass();
        while (clazz != Object.class) {
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                Object val = field.get(dto);
                if (val == null)
                    continue;
                ColumnMap column = field.getDeclaredAnnotation(ColumnMap.class);
                String name = field.getName();
                if (column != null)
                    name = column.value();

                if (field.getDeclaredAnnotation(Equal.class) != null)
                    addEqual(name, val);
                else if (field.getDeclaredAnnotation(Like.class) != null)
                    addLike(name, val);
                else if (field.getDeclaredAnnotation(LeftLike.class) != null)
                    addLeftLike(name, val);
                else if (field.getDeclaredAnnotation(RightLike.class) != null)
                    addRightLike(name, val);
                else if (field.getDeclaredAnnotation(NotEqual.class) != null)
                    addEqualNot(name, val);
                else if (field.getDeclaredAnnotation(Greater.class) != null)
                    addGreater(name, val);
                else if (field.getDeclaredAnnotation(GreaterEqual.class) != null)
                    addGreaterEqual(name, val);
                else if (field.getDeclaredAnnotation(Less.class) != null)
                    addLess(name, val);
                else if (field.getDeclaredAnnotation(LessEqual.class) != null)
                    addLessEqual(name, val);
                else if (field.getDeclaredAnnotation(In.class) != null) {
                    if (!val.getClass().isArray())
                        continue;
                    List<Object> list = new ArrayList<Object>();
                    for (Object object : (Object[]) val)
                        list.add(object);
                    return addIn(name, list);
                }
            }
            clazz = clazz.getSuperclass();
        }
        return this;
    }

    @Override
    public WhereType getWhereType() {
        return WhereType.CONDITION;
    }

    public Condition addCondition(Condition condition) {
        return addSqlWhereAnd(condition);
    }

    public Condition addConditionOr(Condition condition) {
        return addSqlWhereOr(condition);
    }

    public Condition addConditionNot(Condition condition) {
        return addSqlWhereNot(condition);
    }

    public Condition addEqual(String column, Object value) {
        return addSqlWhereAnd(new SqlWhereEqual(column, value));
    }

    public Condition addEqualOr(String column, Object value) {
        return addSqlWhereOr(new SqlWhereEqual(column, value));
    }

    public Condition addEqualNot(String column, Object value) {
        return addSqlWhereAnd(new SqlWhereNotEqual(column, value));
    }

    public Condition addLess(String column, Object value) {
        return addSqlWhereAnd(new SqlWhereLess(column, value));
    }

    public Condition addLessOr(String column, Object value) {
        return addSqlWhereOr(new SqlWhereLess(column, value));
    }

    public Condition addLessNot(String column, Object value) {
        return addSqlWhereNot(new SqlWhereLess(column, value));
    }

    public Condition addLessEqual(String column, Object value) {
        return addSqlWhereAnd(new SqlWhereLessEqual(column, value));
    }

    public Condition addLessEqualOr(String column, Object value) {
        return addSqlWhereOr(new SqlWhereLessEqual(column, value));
    }

    public Condition addLessEqualNot(String column, Object value) {
        return addSqlWhereNot(new SqlWhereLessEqual(column, value));
    }

    public Condition addGreater(String column, Object value) {
        return addSqlWhereAnd(new SqlWhereGreater(column, value));
    }

    public Condition addGreaterOr(String column, Object value) {
        return addSqlWhereOr(new SqlWhereGreater(column, value));
    }

    public Condition addGreaterNot(String column, Object value) {
        return addSqlWhereNot(new SqlWhereGreater(column, value));
    }

    public Condition addGreaterEqual(String column, Object value) {
        return addSqlWhereAnd(new SqlWhereGreaterEqual(column, value));
    }

    public Condition addGreaterEqualOr(String column, Object value) {
        return addSqlWhereOr(new SqlWhereGreaterEqual(column, value));
    }

    public Condition addGreaterEqualNot(String column, Object value) {
        return addSqlWhereNot(new SqlWhereGreaterEqual(column, value));
    }

    public Condition addLike(String column, Object value) {
        return addSqlWhereAnd(new SqlWhereLike(column, value));
    }

    public Condition addLikeOr(String column, Object value) {
        return addSqlWhereOr(new SqlWhereLike(column, value));
    }

    public Condition addLikeNot(String column, Object value) {
        return addSqlWhereNot(new SqlWhereLike(column, value));
    }

    public Condition addLeftLike(String column, Object value) {
        return addSqlWhereAnd(new SqlWhereLeftLike(column, value));
    }

    public Condition addLeftLikeOr(String column, Object value) {
        return addSqlWhereOr(new SqlWhereLeftLike(column, value));
    }

    public Condition addLeftLikeNot(String column, Object value) {
        return addSqlWhereNot(new SqlWhereLeftLike(column, value));
    }

    public Condition addRightLike(String column, Object value) {
        return addSqlWhereAnd(new SqlWhereRightLike(column, value));
    }

    public Condition addRightLikeOr(String column, Object value) {
        return addSqlWhereOr(new SqlWhereRightLike(column, value));
    }

    public Condition addRightLikeNot(String column, Object value) {
        return addSqlWhereNot(new SqlWhereRightLike(column, value));
    }

    public Condition addLikeMatch(String column, Object value) {
        return addSqlWhereAnd(new SqlWhereLikeMatch(column, value));
    }

    public Condition addLikeMatchOr(String column, Object value) {
        return addSqlWhereOr(new SqlWhereLikeMatch(column, value));
    }

    public Condition addLikeMatchNot(String column, Object value) {
        return addSqlWhereNot(new SqlWhereLikeMatch(column, value));
    }

    public Condition addIn(String column, Collection<Object> values) {
        return addSqlWhereAnd(new SqlWhereIn(column, values));
    }

    public Condition addInOr(String column, Collection<Object> values) {
        return addSqlWhereOr(new SqlWhereIn(column, values));
    }

    public Condition addInNot(String column, Collection<Object> values) {
        return addSqlWhereNot(new SqlWhereIn(column, values));
    }

    public Condition addIn(String column, Object... values) {
        return addIn(column, Arrays.asList(values));
    }

    public Condition addInOr(String column, Object... values) {
        return addInOr(column, Arrays.asList(values));
    }

    public Condition addInNot(String column, Object... values) {
        return addInNot(column, Arrays.asList(values));
    }

    public Condition addOrderAsc(String column) {
        if (orderList == null)
            orderList = new ArrayList<SqlOrderBy>();
        orderList.add(new SqlOrderByAsc(column));
        return this;
    }

    public Condition addOrderDesc(String column) {
        if (orderList == null)
            orderList = new ArrayList<SqlOrderBy>();
        orderList.add(new SqlOrderByDesc(column));
        return this;
    }

    public Condition limit(int offset, int total) {
        if (limit == null)
            limit = new SqlLimit(offset, total);
        else {
            limit.setOffset(offset);
            limit.setTotal(total);
        }
        return this;
    }

    private Condition addSqlWhereAnd(SqlWhere where) {
        if (wheres == null) {
            wheres = new ArrayList<SqlWhere>();
            wheres.add(where);
        } else
            wheres.add(new SqlWhereAnd(where));
        return this;
    }

    private Condition addSqlWhereOr(SqlWhere where) {
        if (wheres == null) {
            wheres = new ArrayList<SqlWhere>();
            wheres.add(where);
        } else
            wheres.add(new SqlWhereOr(where));
        return this;
    }

    private Condition addSqlWhereNot(SqlWhere where) {
        wheres.add(new SqlWhereNot(where));
        return this;
    }

    public List<SqlWhere> getWheres() {
        return wheres;
    }

    public List<SqlOrderBy> getOrderList() {
        return orderList;
    }

    public SqlLimit getLimit() {
        return limit;
    }

    public boolean hasWheres() {
        return wheres != null && !wheres.isEmpty();
    }

    public boolean hasOrderList() {
        return orderList != null && !orderList.isEmpty();
    }

    public boolean hasLimit() {
        return limit != null;
    }
}
