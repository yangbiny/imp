package com.impassive.imp.protocol;

/**
 * @author impassivey
 */
public class InvokerWrapper<T> implements Invoker<T> {

  private Invoker<T> invoker;

  private Url url;

  public InvokerWrapper(Invoker<T> invoker, Url url) {
    this.invoker = invoker;
    this.url = url;
  }

  @Override
  public Class<T> getInterfaceClass() {
    return null;
  }

  @Override
  public Result invoke(Invocation invocation) throws Throwable {
    return null;
  }
}
