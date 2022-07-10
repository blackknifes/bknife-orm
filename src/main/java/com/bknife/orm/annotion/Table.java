package com.bknife.orm.annotion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
    /**
     * 表名
     * 
     * @return
     */
    public String name();

    /**
     * 表编码，默认utf8mb4
     * 
     * @return
     */
    public String charset() default "";

    /**
     * 表排序
     * 
     * @return
     */
    public String collate() default "";

    /**
     * 表引擎，默认InnoDB
     * 
     * @return
     */
    public String engine() default "";

    /**
     * 自动增长数值
     * 
     * @return
     */
    public int increment() default 0;

    /**
     * 注释
     * 
     * @return
     */
    public String comment() default "";
}
