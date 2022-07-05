package com.bknife.orm.annotion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表字段注解
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DBField {
    public static enum Type {
        BYTE,
        INTEGER,
        LONG,
        DOUBLE,
        STRING,
        CHARS,
        DECIMAL,
        DATE,
        DATETIME,
        TIMESTAMP,
        BINARY
    }

    /**
     * 字段名
     */
    public String name();

    /**
     * 类型
     */
    public Type type();

    /**
     * 长度
     */
    public int length() default 0;

    /**
     * 小数点
     */
    public int dot() default 0;

    /**
     * 是否为无符号
     * 
     * @return
     */
    public boolean unsigned() default false;

    /**
     * 唯一性
     * 
     * @return
     */
    public boolean unique() default false;

    /**
     * 是否为主键
     */
    public boolean primaryKey() default false;

    /**
     * 是否可为空
     */
    public boolean nullable() default false;

    /**
     * 注释
     */
    public String comment() default "";

    /**
     * 自动增长
     */
    public boolean increment() default false;

    /**
     * 默认值
     */
    public String defaultValue() default "";

    /**
     * 字符集
     */
    public String charset() default "";

    /**
     * 排序规则
     */
    public String collate() default "";
}
