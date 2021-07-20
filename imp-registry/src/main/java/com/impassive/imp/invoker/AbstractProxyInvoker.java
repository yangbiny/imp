package com.impassive.imp.invoker;

import com.impassive.rpc.Invocation;

/** @author impassivey */
public abstract class AbstractProxyInvoker<T> implements Invoker<T> {

  protected T proxy;

  protected Class<T> classType;

  public AbstractProxyInvoker(T ref, Class<T> classType) {
    this.proxy = ref;
    this.classType = classType;
  }

  protected abstract Object doInvoke(
      T reference, String methodName, Object[] params, Class<?>[] paramsType) throws Throwable;

  @Override
  public Class<T> getInterfaceClass() {
    return classType;
  }

  @Override
  public Result invoke(Invocation invocation) throws Throwable {
    Object object =
        doInvoke(
            proxy, invocation.getMethodName(), invocation.getParams(), invocation.getParamTypes());
    System.out.println(object);
    final ImpResult impResult = new ImpResult();
    impResult.setResult(object);
    return impResult;
  }
}
