package com.impassive.rpc.filter;

import com.impassive.rpc.context.RpcContext;
import com.impassive.rpc.invocation.Invocation;
import com.impassive.rpc.invoker.Invoker;
import com.impassive.rpc.result.Result;

@ImpFilter
public class ContextFilter implements Filter {

  @Override
  public Result filter(Invoker<?> invoker, Invocation invocation) throws Throwable {
    RpcContext rpcContext = RpcContext.rpcContextInstance();
    rpcContext.setInvocation(invocation);
    rpcContext.setInvoker(invoker);
    rpcContext.setParamsTypes(invocation.getParamTypes());
    rpcContext.setParams(invocation.getParams());
    try {
      return invoker.invoke(invocation);
    } finally {
      RpcContext.removeContext();
    }
  }
}
