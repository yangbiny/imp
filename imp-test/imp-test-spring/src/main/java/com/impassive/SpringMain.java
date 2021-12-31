package com.impassive;

import com.impassive.consumer.Consumer;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author impassivey
 */
public class SpringMain {

  public static void main(String[] args) throws Exception {
    ClassPathXmlApplicationContext provider = new ClassPathXmlApplicationContext(
        "application_provider.xml");
    ClassPathXmlApplicationContext consumer = new ClassPathXmlApplicationContext(
        "application_consumer.xml");
    provider.registerShutdownHook();
    consumer.registerShutdownHook();
    Consumer bean = consumer.getBean(Consumer.class);
    bean.start();

    provider.close();
    consumer.close();
  }
}
