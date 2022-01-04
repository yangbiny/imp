package com.impassive.config.utils;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;

public class BeanUtils {

  public static <T> T getBean(ApplicationContext applicationContext, Class<T> tClass) {
    return applicationContext.getBean(tClass);
  }

  public static <T> T getBeanWithDefaultValue(ApplicationContext applicationContext,
      Class<T> tClass, T objectValue) {
    try {
      return applicationContext.getBean(tClass);
    } catch (NoSuchBeanDefinitionException noSuchBeanDefinitionException) {
      return objectValue;
    }
  }

}
