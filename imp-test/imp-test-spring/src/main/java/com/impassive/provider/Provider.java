package com.impassive.provider;

import com.impassive.TestRpc;
import com.impassive.config.service.ServiceConfig;
import com.impassive.registry.config.ApplicationConfig;
import com.impassive.registry.config.ProtocolConfig;
import com.impassive.registry.config.RegistryConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author impassivey
 */
@Configuration
public class Provider {


  @Bean
  public ServiceConfig<TestRpc> setUp(TestRpc testRpc) {
    ServiceConfig<TestRpc> serviceConfig = new ServiceConfig<>();
    serviceConfig.setGroupName("test-a");
    serviceConfig.setInterface(TestRpc.class);
    serviceConfig.setReference(testRpc);
    return serviceConfig;
  }

  @Bean
  public RegistryConfig registryConfig() {
    RegistryConfig registryConfig = new RegistryConfig();
    registryConfig.setRegistryAddress("impassive.com:2181");
    registryConfig.setRegistryType("zookeeper");
    registryConfig.setRegister(true);
    return registryConfig;
  }

  @Bean
  public ProtocolConfig protocolConfig() {
    ProtocolConfig protocolConfig = new ProtocolConfig();
    protocolConfig.setPort(11222);
    return protocolConfig;
  }

  @Bean
  public ApplicationConfig applicationConfig() {
    ApplicationConfig applicationConfig = new ApplicationConfig();
    applicationConfig.setApplicationName("test");
    return applicationConfig;
  }

}
