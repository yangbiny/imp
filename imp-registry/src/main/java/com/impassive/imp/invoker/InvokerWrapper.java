package com.impassive.imp.invoker;

import com.impassive.imp.common.Url;
import com.impassive.imp.remoting.Invocation;
import com.impassive.imp.remoting.Result;
import lombok.Getter;

/** @author impassivey */
@Getter
public class InvokerWrapper<T> implements Invoker<T> {

  private final Invoker<T> invoker;

  private Url url;

  public InvokerWrapper(Invoker<T> invoker, Url url) {
    this.invoker = invoker;
    this.url = url;
  }

  @Override
  public Class<T> getInterfaceClass() {
    return (Class<T>) url.getClassType();
  }

  @Override
  public Result invoke(Invocation invocation) throws Throwable {
    return invoker.invoke(invocation);
  }
}
