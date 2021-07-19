package com.impassive.imp.test;

import com.impassive.imp.test.consumer.Consumer;
import com.impassive.imp.test.provider.Provider;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/** @author impassivey */
public class StartMain {

  public static void main(String[] args) throws Exception {

    CountDownLatch countDownLatch = new CountDownLatch(1);

    new Thread(
            () -> {
              Provider provider = new Provider(countDownLatch);
              try {
                provider.start();
              } catch (IOException e) {
                e.printStackTrace();
              }
            })
        .start();
    countDownLatch.await();
    Consumer consumer = new Consumer();
    consumer.start();
  }
}
