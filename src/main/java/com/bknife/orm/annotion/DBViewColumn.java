package com.bknife.orm.annotion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 视图列
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DBViewColumn {
    /**
     * 列名
     * 
     * @return
     */
    public String name();

    /**
     * 表名
     * 
     * @return
     */
    public String table();
}
