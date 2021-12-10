package com.impassive.imp.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
public @interface Alias {

  @Alias("attribute")
  String value() default "";

  @Alias("value")
  String attribute() default "";

  Class<? extends Annotation> annotation() default Annotation.class;


}
