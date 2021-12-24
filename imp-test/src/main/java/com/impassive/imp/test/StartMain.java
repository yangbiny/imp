package com.impassive.imp.test;

import com.impassive.imp.test.consumer.Consumer;
import com.impassive.imp.test.provider.Provider;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author impassivey
 */
public class StartMain {

  public static void main(String[] args) throws Exception {

    CountDownLatch countDownLatch = new CountDownLatch(1);
    AtomicBoolean finish = new AtomicBoolean(Boolean.FALSE);
    new Thread(
        () -> {
          Provider provider = new Provider(countDownLatch, finish);
          try {
            provider.start();
            System.out.println("xx");
          } catch (Exception e) {
            e.printStackTrace();
          }
        })
        .start();
    countDownLatch.await();
    Consumer consumer = new Consumer(finish);
    consumer.start();
  }
}
