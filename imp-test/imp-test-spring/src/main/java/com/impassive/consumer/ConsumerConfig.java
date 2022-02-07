package com.impassive.consumer;


import com.impassive.TestRpc;
import com.impassive.config.ConsumerBeanConfig;
import com.impassive.imp.util.limiter.LimiterConfig;
import com.impassive.registry.config.ApplicationConfig;
import com.impassive.registry.config.ProtocolConfig;
import com.impassive.registry.config.RegistryConfig;
import java.util.concurrent.TimeUnit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConsumerConfig {

  @Bean
  public ConsumerBeanConfig<TestRpc> testRpc() {
    ConsumerBeanConfig<TestRpc> testRpcConsumerBeanConfig = new ConsumerBeanConfig<>();
    testRpcConsumerBeanConfig.setClassType(TestRpc.class);
    testRpcConsumerBeanConfig.setGroupName("test-a");
    return testRpcConsumerBeanConfig;
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
    protocolConfig.setUseEndpoint(true);
    protocolConfig.setHost("127.0.0.1");
    protocolConfig.setPort(11222);
    return protocolConfig;
  }

  @Bean
  public ApplicationConfig applicationConfig() {
    ApplicationConfig applicationConfig = new ApplicationConfig();
    applicationConfig.setApplicationName("test");
    return applicationConfig;
  }

  @Bean
  public LimiterConfig limiterConfig() {
    LimiterConfig limiterConfig = new LimiterConfig();
    limiterConfig.setEnable(true);
    limiterConfig.setPeriod(1);
    limiterConfig.setPeriodUnit(TimeUnit.SECONDS);
    limiterConfig.setPermits(1L);
    return limiterConfig;
  }

}
