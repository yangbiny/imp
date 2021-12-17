package com.impassive.imp.test.consumer;

import com.impassive.imp.api.ConsumerBean;
import com.impassive.imp.test.TestRpc;
import com.impassive.registry.config.ApplicationConfig;
import com.impassive.registry.config.ProtocolConfig;
import com.impassive.registry.config.RegistryConfig;

/**
 * @author impassivey
 */
public class Consumer {

  private static ConsumerBean<TestRpc> consumerBean;

  public void start() throws InterruptedException {
    init();
    TestRpc bean = consumerBean.getBean();
    final String test = bean.test("test" + System.currentTimeMillis());
    System.out.println(test);
/*     Param param = new Param();
      param.setDate(System.currentTimeMillis());
      Result result = bean.test(param);
      System.out.println(result.getObj());
      List<Long> longs = Lists.newArrayList(System.currentTimeMillis());
      List<String> tests = bean.test(longs);
      System.out.println(Arrays.toString(tests.toArray()));
      ArrayList<Long> arrayList = Lists.newArrayList(System.currentTimeMillis());
      List<String> test1 = bean.test(arrayList);
      System.out.println(Arrays.toString(test1.toArray()));*/
  }

  private void init() {
    consumerBean = new ConsumerBean<>();

    ApplicationConfig applicationConfig = new ApplicationConfig();
    applicationConfig.setApplicationName("test");

    ProtocolConfig protocolConfig = new ProtocolConfig();
    protocolConfig.setPort(11222);
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
