package org.prism.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by fushenghua on 2017/1/14.
 */
@Target(ElementType.METHOD)
@Retention(RUNTIME)
public @interface RouterUri {
    String routerUri() default "";
}
