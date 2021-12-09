package com.impassive.imp.test.provider;

import com.impassive.imp.api.ServiceConfig;
import com.impassive.imp.test.TestRpc;
import com.impassive.registry.config.ApplicationConfig;
import com.impassive.registry.config.ProtocolConfig;
import com.impassive.registry.config.RegistryConfig;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/** @author impassivey */
public class Provider {

  private ServiceConfig<TestRpc> serviceConfig;

  private CountDownLatch countDownLatch;

  public Provider(CountDownLatch countDownLatch) {
    this.countDownLatch = countDownLatch;
  }

  public void setUp() {
    ApplicationConfig applicationConfig = new ApplicationConfig();
    applicationConfig.setApplicationName("test");

    ProtocolConfig protocolConfig = new ProtocolConfig();
    protocolConfig.setExportPort(11222);

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

  public void start() throws IOException {
    setUp();
    export();
  }

  public void export() throws IOException {
    serviceConfig.export();
    countDownLatch.countDown();
    System.out.println(System.in.read());
  }
}
