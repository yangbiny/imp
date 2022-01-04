package com.impassive.config;

import com.impassive.config.service.ServiceConfig;
import com.impassive.config.utils.BeanUtils;
import com.impassive.imp.util.limiter.LimiterConfig;
import com.impassive.registry.config.ApplicationConfig;
import com.impassive.registry.config.ProtocolConfig;
import com.impassive.registry.config.RegistryConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ServiceBeanConfig<T> extends ServiceConfig<T> implements ApplicationContextAware,
    InitializingBean,
    DisposableBean {

  private ApplicationContext applicationContext;

  @Override
  public void afterPropertiesSet() throws Exception {
    this.setApplicationConfig(applicationContext.getBean(ApplicationConfig.class));
    this.setRegistryConfig(applicationContext.getBean(RegistryConfig.class));
    this.setProtocolConfig(applicationContext.getBean(ProtocolConfig.class));
    this.setLimiterConfig(BeanUtils.getBeanWithDefaultValue(applicationContext,LimiterConfig.class,null));
    this.export();
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  @Override
  public void destroy() throws Exception {
    super.doDestroy();
  }
}
