package com.impassive.imp.common.extension;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SPI {

  /**
   * 需要默认激活的 extensionName
   * @return 默认激活的extensionName
   */
  String name() default "";

  /**
   * 默认激活的 extensionName。
   * @return 默认名称为default。不存在则报错
   */
  String value() default "default";

}
