package com.impassive.config;

import com.impassive.imp.util.limiter.LimiterConfig;
import com.impassive.registry.config.ApplicationConfig;
import com.impassive.registry.config.ProtocolConfig;
import com.impassive.registry.config.RegistryConfig;
import lombok.Getter;

/**
 * @author impassivey
 */
@Getter
public class BaseConfig {

  protected RegistryConfig registryConfig;

  protected ProtocolConfig protocolConfig;

  protected ApplicationConfig applicationConfig;

  protected LimiterConfig limiterConfig;

  public void setProtocolConfig(ProtocolConfig protocol) {
    this.protocolConfig = protocol;
  }

  public void setApplicationConfig(ApplicationConfig applicationConfig) {
    this.applicationConfig = applicationConfig;
  }

  public void setRegistryConfig(RegistryConfig registryConfig) {
    this.registryConfig = registryConfig;
  }

  public void setLimiterConfig(LimiterConfig limiterConfig) {
    this.limiterConfig = limiterConfig;
  }
}
