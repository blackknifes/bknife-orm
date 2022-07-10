package com.bknife.orm.annotion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Check {
    public static enum CheckType {
        IsNull,
        NotNull,
        Equal,
        NotEqual,
        Less,
        LessEqual,
        Greater,
        GreaterEqual,
        Like,
        LeftLike,
        RightLike,
        Regexp
    }

    /**
     * 操作符
     * 
     * @return
     */
    public CheckType op() default CheckType.Equal;

    /**
     * 值
     * 
     * @return
     */
    public String value();
}
