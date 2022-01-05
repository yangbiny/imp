package com.impassive.invocation;

import com.impassive.imp.common.Url;
import com.impassive.rpc.invocation.Invocation;
import com.impassive.rpc.invoker.Invoker;
import com.impassive.rpc.result.Result;

public class InvokerLimiterWrapper<T> implements Invoker<T> {

  private final Invoker<T> invoker;

  private final Url url;

  public InvokerLimiterWrapper(Invoker<T> invoker) {
    this.invoker = invoker;
    this.url = invoker.getUrl();
  }

  @Override
  public void destroy() {
    invoker.destroy();
  }

  @Override
  public Class<T> getInterfaceClass() {
    return invoker.getInterfaceClass();
  }

  @Override
  public Result invoke(Invocation invocation) throws Throwable {
    return invoker.invoke(invocation);
  }

  @Override
  public Url getUrl() {
    return url;
  }
}
