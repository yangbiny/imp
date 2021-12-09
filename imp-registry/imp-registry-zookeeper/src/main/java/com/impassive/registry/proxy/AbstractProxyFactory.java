package com.impassive.registry.proxy;

import com.impassive.rpc.invoker.Invoker;

/** @author impassivey */
public abstract class AbstractProxyFactory  implements ProxyFactory {

  protected abstract <T> Invoker<T> doGetInvoker(T ref, Class<T> classType);

  protected abstract <T> T getProxy(Invoker<T> invoker, Class<?>[] classType);

  @Override
  public <T> Invoker<T> getInvoker(T ref, Class<T> classType) {
    return doGetInvoker(ref, classType);
  }

  @Override
  public <T> T proxy(Invoker<T> invoker) {
    Class<T> interfaceClass = invoker.getInterfaceClass();
    Class<T>[] interfaces = new Class[1];
    interfaces[0] = interfaceClass;
    return getProxy(invoker, interfaces);
  }
}
