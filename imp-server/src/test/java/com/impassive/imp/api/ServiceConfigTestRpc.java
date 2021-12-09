package com.impassive.imp.api;

import com.impassive.imp.api.test.DefaultTestRpc;
import com.impassive.imp.api.test.TestRpc;
import com.impassive.registry.config.ApplicationConfig;
import com.impassive.registry.config.ProtocolConfig;
import com.impassive.registry.config.RegistryConfig;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;

public class ServiceConfigTestRpc {

  private ServiceConfig<TestRpc> serviceConfig;

  @Before
  public void setUp() throws Exception {
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

  @Test
  public void export() throws IOException {
    serviceConfig.export();
    System.out.println(System.in.read());
  }
}
