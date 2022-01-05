package com.impassive.imp.common.extension;

import com.impassive.imp.annotation.Alias;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Extension {

  @Alias("active")
  boolean value() default true;

  @Alias("value")
  boolean active() default true;

  int order() default 1;

}
