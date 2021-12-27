package com.impassive.config;

import com.impassive.config.client.ConsumerConfig;
import com.impassive.registry.config.ApplicationConfig;
import com.impassive.registry.config.ProtocolConfig;
import com.impassive.registry.config.RegistryConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ConsumerBeanConfig<T> extends ConsumerConfig<T> implements
    FactoryBean<T>,
    ApplicationContextAware,
    InitializingBean,
    DisposableBean {

  private ApplicationContext applicationContext;

  @Override
  public T getObject() throws Exception {
    return this.refer();
  }

  @Override
  public Class<?> getObjectType() {
    return getClassType();
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    this.setApplicationConfig(applicationContext.getBean(ApplicationConfig.class));
    this.setProtocolConfig(applicationContext.getBean(ProtocolConfig.class));
    this.setRegistryConfig(applicationContext.getBean(RegistryConfig.class));
  }

  @Override
  public void destroy() throws Exception {

  }
}
