package com.lucian.custom.di.infrastructure;

import java.lang.annotation.*;

@Documented
@Target(ElementType.PARAMETER)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomValue {
    public String value() default "";
}
