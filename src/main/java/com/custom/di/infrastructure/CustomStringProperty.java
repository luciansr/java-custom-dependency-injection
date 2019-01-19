package com.custom.di.infrastructure;

import java.lang.annotation.*;

@Documented
@Target(ElementType.PARAMETER)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomStringProperty {
    public String value() default "";
}
