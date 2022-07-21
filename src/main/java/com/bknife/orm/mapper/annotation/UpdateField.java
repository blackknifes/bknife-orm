package com.bknife.orm.mapper.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 指示是否参与update
 */
@Target({ ElementType.FIELD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface UpdateField {
    /**
     * 该字段是否参与update
     * 
     * @return
     */
    public boolean value() default true;
}
