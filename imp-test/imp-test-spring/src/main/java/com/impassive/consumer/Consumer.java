package com.impassive.consumer;

import com.impassive.TestRpc;
import javax.annotation.Resource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * @author impassivey
 */
@Component
public class Consumer implements InitializingBean {

  @Resource
  private TestRpc defaultTestRpc;

  public void start() {
    long timeMillis = System.currentTimeMillis();
    System.out.println(timeMillis);
    final String test = defaultTestRpc.test("test : " + timeMillis);
    System.out.println(test);
  }

  @Override
  public void afterPropertiesSet() throws Exception {

  }
}
