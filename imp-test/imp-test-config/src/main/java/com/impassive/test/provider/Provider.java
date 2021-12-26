package com.impassive.test.provider;

import com.impassive.config.service.ServiceConfig;
import com.impassive.test.TestRpc;
import com.impassive.registry.config.ApplicationConfig;
import com.impassive.registry.config.ProtocolConfig;
import com.impassive.registry.config.RegistryConfig;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author impassivey
 */
public class Provider {

  private ServiceConfig<TestRpc> serviceConfig;

  private final CountDownLatch countDownLatch;

  private final AtomicBoolean finish;

  public Provider(CountDownLatch countDownLatch, AtomicBoolean finish) {
    this.countDownLatch = countDownLatch;
    this.finish = finish;
  }

  public void setUp() {
    ApplicationConfig applicationConfig = new ApplicationConfig();
    applicationConfig.setApplicationName("test");

    ProtocolConfig protocolConfig = new ProtocolConfig();
    protocolConfig.setPort(11222);

    RegistryConfig registryConfig = new RegistryConfig();
    registryConfig.setRegistryAddress("impassive.com:2181");
    registryConfig.setRegistryType("zookeeper");
    registryConfig.setRegister(true);

    serviceConfig = new ServiceConfig<>();
    serviceConfig.setApplicationConfig(applicationConfig);
    serviceConfig.setProtocolConfig(protocolConfig);
    serviceConfig.setRegistryConfig(registryConfig);
    serviceConfig.setGroupName("test-a");
    serviceConfig.setInterface(TestRpc.class);
    DefaultTestRpc defaultTestRpc = new DefaultTestRpc();
    serviceConfig.setReference(defaultTestRpc);
  }

  public void start() throws Exception {
    setUp();
    export();
    unExport();
  }

  private void unExport() {
    serviceConfig.unExport();
  }

  public void export() throws Exception {
    serviceConfig.export();
    countDownLatch.countDown();
    while (!finish.get()) {
      Thread.sleep(1000);
    }
  }
}
