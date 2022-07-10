package com.bknife.orm.annotion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 唯一性约束
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Uniques.class)
public @interface Unique {
    public String[] value();
}
