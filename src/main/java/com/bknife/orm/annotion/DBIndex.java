package com.bknife.orm.annotion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据库索引
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(DBIndexs.class)
public @interface DBIndex {

    public static enum IndexType {
        NORMAL,
        FULLTEXT,
        SPATIAL,
        UNIQUE
    }

    public static enum IndexMethod {
        BTREE,
        HASH
    }

    /**
     * 索引字段
     * 
     * @return
     */
    public String[] keys();

    /**
     * 索引类型
     * 
     * @return
     */
    public IndexType type() default IndexType.NORMAL;

    /**
     * 索引方法
     * 
     * @return
     */
    public IndexMethod method() default IndexMethod.BTREE;

    /**
     * 注释
     * 
     * @return
     */
    public String comment() default "";
}
