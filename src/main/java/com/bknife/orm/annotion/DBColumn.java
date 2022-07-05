package com.bknife.orm.annotion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DBColumn {

    /**
     * 字段名
     * 
     * @return
     */
    public String name();

    /**
     * 来源表名
     * 
     * @return
     */
    public String table() default "";

    /**
     * 来源表类
     * 
     * @return
     */
    public Class<?> tableClass() default Object.class;
}
