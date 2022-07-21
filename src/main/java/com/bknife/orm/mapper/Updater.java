package com.bknife.orm.mapper;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.bknife.base.util.DateUtil;
import com.bknife.orm.mapper.annotation.UpdateField;
import com.bknife.orm.mapper.where.Condition;

public class Updater {
    private Map<String, Object> updates = new HashMap<String, Object>();
    private Condition condition = new Condition();

    public static <T> Updater createByDto(T dto) throws Exception {
        Updater updater = new Updater().update(dto);
        updater.condition.addByDto(dto);
        return updater;
    }

    public Condition getCondition() {
        return condition;
    }

    public Updater update(String columnName, Object value) {
        updates.put(columnName, value);
        return this;
    }

    public <T> Updater update(T dto) throws Exception {
        Class<?> clazz = dto.getClass();
        while (clazz != Object.class) {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.getDeclaredAnnotation(UpdateField.class) == null)
                    continue;
                field.setAccessible(true);
                Object val = field.get(dto);
                if (val != null)
                    update(field.getName(), val);
            }
            clazz = clazz.getSuperclass();
        }
        return this;
    }

    public Updater updateNowDatetime(String column) {
        return update(column, DateUtil.nowDatetime());
    }

    public Updater updateNowDate(String column) {
        return update(column, DateUtil.nowDate());
    }

    public Map<String, Object> getUpdates() {
        return updates;
    }

    public Updater addCondition(Condition condition) {
        condition.addCondition(condition);
        return this;
    }

    public Updater addEqual(String column, Object value) {
        condition.addEqual(column, value);
        return this;
    }

    public Updater addEqualOr(String column, Object value) {
        condition.addEqualOr(column, value);
        return this;
    }

    public Updater addEqualNot(String column, Object value) {
        condition.addEqualNot(column, value);
        return this;
    }

    public Updater addLess(String column, Object value) {
        condition.addLess(column, value);
        return this;
    }

    public Updater addLessOr(String column, Object value) {
        condition.addLessOr(column, value);
        return this;
    }

    public Updater addLessNot(String column, Object value) {
        condition.addLessNot(column, value);
        return this;
    }

    public Updater addLessEqual(String column, Object value) {
        condition.addLessEqual(column, value);
        return this;
    }

    public Updater addLessEqualOr(String column, Object value) {
        condition.addLessEqualOr(column, value);
        return this;
    }

    public Updater addLessEqualNot(String column, Object value) {
        condition.addLessEqualNot(column, value);
        return this;
    }

    public Updater addGreater(String column, Object value) {
        condition.addGreater(column, value);
        return this;
    }

    public Updater addGreaterOr(String column, Object value) {
        condition.addGreaterOr(column, value);
        return this;
    }

    public Updater addGreaterNot(String column, Object value) {
        condition.addGreaterNot(column, value);
        return this;
    }

    public Updater addGreaterEqual(String column, Object value) {
        condition.addGreaterEqual(column, value);
        return this;
    }

    public Updater addGreaterEqualOr(String column, Object value) {
        condition.addGreaterEqualOr(column, value);
        return this;
    }

    public Updater addGreaterEqualNot(String column, Object value) {
        condition.addGreaterEqualNot(column, value);
        return this;
    }

    public Updater addLike(String column, Object value) {
        condition.addLike(column, value);
        return this;
    }

    public Updater addLikeOr(String column, Object value) {
        condition.addLikeOr(column, value);
        return this;
    }

    public Updater addLikeNot(String column, Object value) {
        condition.addLikeNot(column, value);
        return this;
    }

    public Updater addLeftLike(String column, Object value) {
        condition.addLeftLike(column, value);
        return this;
    }

    public Updater addLeftLikeOr(String column, Object value) {
        condition.addLeftLikeOr(column, value);
        return this;
    }

    public Updater addLeftLikeNot(String column, Object value) {
        condition.addLeftLikeNot(column, value);
        return this;
    }

    public Updater addRightLike(String column, Object value) {
        condition.addRightLike(column, value);
        return this;
    }

    public Updater addRightLikeOr(String column, Object value) {
        condition.addRightLikeOr(column, value);
        return this;
    }

    public Updater addRightLikeNot(String column, Object value) {
        condition.addRightLikeNot(column, value);
        return this;
    }

    public Updater addLikeMatch(String column, Object value) {
        condition.addLikeMatch(column, value);
        return this;
    }

    public Updater addLikeMatchOr(String column, Object value) {
        condition.addLikeMatchOr(column, value);
        return this;
    }

    public Updater addLikeMatchNot(String column, Object value) {
        condition.addLikeMatchNot(column, value);
        return this;
    }

    public Updater addIn(String column, Collection<Object> values) {
        condition.addIn(column, values);
        return this;
    }

    public Updater addInOr(String column, Collection<Object> values) {
        condition.addInOr(column, values);
        return this;
    }

    public Updater addInNot(String column, Collection<Object> values) {
        condition.addInNot(column, values);
        return this;
    }

    public Updater addIn(String column, Object... values) {
        return addIn(column, Arrays.asList(values));
    }

    public Updater addInOr(String column, Object... values) {
        return addInOr(column, Arrays.asList(values));
    }

    public Updater addInNot(String column, Object... values) {
        return addInNot(column, Arrays.asList(values));
    }

    public Updater addOrderAsc(String column) {
        condition.addOrderAsc(column);
        return this;
    }

    public Updater addOrderDesc(String column) {
        condition.addOrderDesc(column);
        return this;
    }

    public Updater limit(int offset, int total) {
        condition.limit(offset, total);
        return this;
    }
}
