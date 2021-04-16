package com.impassive.imp.api;

/** @author impassivey */
public class ConsumerBean<T> extends ConsumerConfig<T> {

  private transient T ref;

  public T getBean() {
    return createProxy();
  }

  private T createProxy() {
    if (ref != null) {
      return ref;
    }
    return null;
  }
}
