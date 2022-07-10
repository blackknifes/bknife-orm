package com.bknife.orm.annotion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(GroupBys.class)
public @interface GroupBy {

    /**
     * 字段名
     * 
     * @return
     */
    public String name();

    /**
     * 表类
     * 
     * @return
     */
    public Class<?> tableClass() default Object.class;

    /**
     * 表名
     * 
     * @return
     */
    public String table() default "";
}
