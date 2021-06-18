package com.impassive.imp.proxy;

import com.impassive.imp.invoker.Invoker;
import com.impassive.imp.invoker.Result;
import com.impassive.rpc.RpcInvocation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/** @author impassivey */
public class ProxyInvocationHandler implements InvocationHandler {

  private Invoker<?> invoker;

  public <T> ProxyInvocationHandler(Invoker<?> invoker) {
    this.invoker = invoker;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    String name = invoker.getInterfaceClass().getName();
    RpcInvocation invocation = new RpcInvocation(method,args,name);
    Result invoke = invoker.invoke(invocation);
    if (invoke == null){
      return null;
    }
    return invoke.getResult();
  }
}
