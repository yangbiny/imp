package com.impassive.rpc.filter;

import com.impassive.imp.annotation.Alias;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.annotation.Resource;

/**
 * filter的注解。
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Resource
public @interface ImpFilter {

  @Alias("active")
  boolean value() default true;

  /**
   * 激活标志。只有为True的时候才会生效
   *
   * @return true:filter有效
   */
  @Alias("value")
  boolean active() default true;

  /**
   * Filter的顺序。顺序越小越靠前
   *
   * @return 执行的顺序
   */
  int order() default -1;
}
