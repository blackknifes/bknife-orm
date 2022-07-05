package com.bknife.orm.annotion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(DBJoins.class)
public @interface DBJoin {
    /**
     * 连接方式
     */
    public static enum JoinType {
        LEFT_JOIN, // 左连接
        RIGHT_JOIN, // 右连接
        INNER_JOIN, // 内连接
        OUTTER_JOIN // 外连接
    }

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

    /**
     * 连接方式
     * 
     * @return
     */
    public JoinType join() default JoinType.LEFT_JOIN;

    /**
     * 主表字段
     * 
     * @return
     */
    public String[] sources();

    /**
     * 附表字段
     * 
     * @return
     */
    public String[] targets();
}
