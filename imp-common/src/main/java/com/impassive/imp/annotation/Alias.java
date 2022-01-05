package com.impassive.imp.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface Alias {

  @Alias("attribute")
  String value() default "";

  @Alias("value")
  String attribute() default "";

  Class<? extends Annotation> annotation() default Annotation.class;


}
