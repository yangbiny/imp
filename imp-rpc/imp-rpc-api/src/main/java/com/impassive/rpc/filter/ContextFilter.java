package com.impassive.rpc.filter;

import com.impassive.rpc.invocation.Invocation;
import com.impassive.rpc.invoker.Invoker;
import com.impassive.rpc.result.Result;

@ImpFilter(value = false)
public class ContextFilter implements Filter {

  @Override
  public Result filter(Invoker<?> invoker, Invocation invocation) throws Throwable {
    return invoker.invoke(invocation);
  }
}
