package com.impassive.consumer;

import com.impassive.TestRpc;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * @author impassivey
 */
@Component
public class Consumer implements InitializingBean {

  private ThreadPoolExecutor threadPoolExecutor;

  @Resource
  private TestRpc defaultTestRpc;

  public void start() {
    final String test = defaultTestRpc.test("test : ");
    System.out.println(test);
    for (int i = 0; i < 100; i++) {
      threadPoolExecutor.submit(() -> {
      });
    }
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    threadPoolExecutor = new ThreadPoolExecutor(10,
        10,
        1,
        TimeUnit.SECONDS,
        new ArrayBlockingQueue<>(100));
  }
}
