package com.impassive;

import com.impassive.consumer.Consumer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author impassivey
 */
public class SpringMain {

  public static void main(String[] args) throws Exception {
    ApplicationContext applicationContext = new ClassPathXmlApplicationContext("application.xml");

    Consumer bean = applicationContext.getBean(Consumer.class);
    bean.start();
  }
}
