package com.impassive;

import com.impassive.consumer.Consumer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author impassivey
 */
public class SpringMain {

  public static void main(String[] args) throws Exception {
    ApplicationContext provider = new ClassPathXmlApplicationContext("application_provider.xml");
    ApplicationContext consumer = new ClassPathXmlApplicationContext("application_consumer.xml");

    Consumer bean = consumer.getBean(Consumer.class);
    bean.start();
  }
}
