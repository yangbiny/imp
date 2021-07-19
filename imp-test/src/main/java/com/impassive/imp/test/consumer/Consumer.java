package com.impassive.imp.test.consumer;

import com.impassive.imp.api.ConsumerBean;
import com.impassive.imp.application.ApplicationConfig;
import com.impassive.imp.protocol.ProtocolConfig;
import com.impassive.imp.registry.RegistryConfig;
import com.impassive.imp.test.TestRpc;

/** @author impassivey */
public class Consumer {

  private static ConsumerBean<TestRpc> consumerBean;

  public void start() throws InterruptedException {
    init();
    TestRpc bean = consumerBean.getBean();
    while (true) {
      System.out.println(bean.test("test"));
      Thread.sleep(1000);
    }
  }

  private void init() {
    consumerBean = new ConsumerBean<>();

    ApplicationConfig applicationConfig = new ApplicationConfig();
    applicationConfig.setApplicationName("test");

    ProtocolConfig protocolConfig = new ProtocolConfig();
    protocolConfig.setExportPort(11222);
    protocolConfig.setHost("127.0.0.1");

    RegistryConfig registryConfig = new RegistryConfig();
    registryConfig.setRegistryAddress("impassive.com:2181");
    registryConfig.setRegistryType("zookeeper");
    registryConfig.setRegister(true);

    consumerBean.setApplicationConfig(applicationConfig);
    consumerBean.setProtocolConfig(protocolConfig);
    consumerBean.setRegistryConfig(registryConfig);
    consumerBean.setGroupName("test_group");
    consumerBean.setClassType(TestRpc.class);
  }
}
