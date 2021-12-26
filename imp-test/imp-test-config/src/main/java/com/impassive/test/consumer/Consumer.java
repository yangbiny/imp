package com.impassive.test.consumer;

import com.impassive.config.client.ConsumerConfig;
import com.impassive.test.TestRpc;
import com.impassive.registry.config.ApplicationConfig;
import com.impassive.registry.config.ProtocolConfig;
import com.impassive.registry.config.RegistryConfig;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author impassivey
 */
public class Consumer {

  private static ConsumerConfig<TestRpc> consumerConfig;

  private final AtomicBoolean atomicBoolean;

  public Consumer(AtomicBoolean finish) {
    atomicBoolean = finish;
  }

  public void start() throws InterruptedException {
    init();
    TestRpc bean = consumerConfig.refer();
    final String test = bean.test("test" + System.currentTimeMillis());
    System.out.println(test);
    atomicBoolean.compareAndSet(false, true);
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
    consumerConfig = new ConsumerConfig<>();

    ApplicationConfig applicationConfig = new ApplicationConfig();
    applicationConfig.setApplicationName("test");

    ProtocolConfig protocolConfig = new ProtocolConfig();
    protocolConfig.setPort(11222);
    protocolConfig.setHost("127.0.0.1");

    RegistryConfig registryConfig = new RegistryConfig();
    registryConfig.setRegistryAddress("impassive.com:2181");
    registryConfig.setRegistryType("zookeeper");
    registryConfig.setRegister(true);

    consumerConfig.setApplicationConfig(applicationConfig);
    consumerConfig.setProtocolConfig(protocolConfig);
    consumerConfig.setRegistryConfig(registryConfig);
    consumerConfig.setGroupName("test-a");
    consumerConfig.setClassType(TestRpc.class);
  }
}
