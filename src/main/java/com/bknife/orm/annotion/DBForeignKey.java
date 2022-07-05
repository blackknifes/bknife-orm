package com.bknife.orm.annotion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(DBForeignKeys.class)
public @interface DBForeignKey {

    public static enum ForeignKeyAction {
        CASCADE,
        NO_ACTION,
        RESTRICT,
        SET_NULL
    }

    /**
     * 外键引用表类，此参数设置时将忽略table
     * 
     * @return
     */
    public Class<?> tableClass() default Object.class;

    /**
     * 外键引用表名
     * 
     * @return
     */
    public String table() default "";

    /**
     * 外键字段名
     * 
     * @return
     */
    public String[] sources();

    /**
     * 外键目标引用列名
     * 
     * @return
     */
    public String[] targets();

    /**
     * 更新时动作
     * 
     * @return
     */
    public ForeignKeyAction update() default ForeignKeyAction.CASCADE;

    /**
     * 删除时动作
     * 
     * @return
     */
    public ForeignKeyAction delete() default ForeignKeyAction.CASCADE;
}
