package com.impassive.imp.proxy;

import com.impassive.imp.invoker.AbstractProxyInvoker;
import com.impassive.imp.invoker.Invoker;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/** @author impassivey */
public class JdkProxyFactory extends AbstractProxyFactory {

  @Override
  public <T> Invoker<T> doGetInvoker(T ref, Class<T> classType) {
    return new AbstractProxyInvoker<T>(ref, classType) {

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
