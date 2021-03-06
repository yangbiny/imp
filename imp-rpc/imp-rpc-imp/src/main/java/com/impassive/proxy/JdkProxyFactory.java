package com.impassive.proxy;

import com.impassive.imp.common.Url;
import com.impassive.result.AbstractProxyInvoker;
import com.impassive.rpc.invoker.Invoker;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/** @author impassivey */
public class JdkProxyFactory extends AbstractProxyFactory {

  @Override
  public <T> Invoker<T> doGetInvoker(T ref, Class<T> classType) {
    return new AbstractProxyInvoker<T>(ref, classType) {

      @Override
      public Url getUrl() {
        return null;
      }

      @Override
      protected Object doInvoke(
          T reference, String methodName, Object[] params, Class<?>[] paramsType) throws Throwable {
        final Method method = reference.getClass().getMethod(methodName, paramsType);
        return method.invoke(reference, params);
      }
    };
  }

  @Override
  protected <T> T getProxy(Invoker<T> invoker, Class<?>[] classType) {
    return (T)
        Proxy.newProxyInstance(
            Thread.currentThread().getContextClassLoader(),
            classType,
            new ProxyInvocationHandler(invoker));
  }
}
