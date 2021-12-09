package com.impassive.rpc.invoker;

import com.impassive.imp.common.Url;
import com.impassive.rpc.invocation.Invocation;
import com.impassive.rpc.result.Result;
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
