package com.lucian.custom.di;

import java.lang.annotation.*;

@Documented
@Target(ElementType.PARAMETER)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomValue {
    public String value() default "";
}
