package com.impassive.imp.invoker;

import com.impassive.imp.common.Url;
import com.impassive.rpc.Invocation;
import lombok.Getter;

/** @author impassivey */
@Getter
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
