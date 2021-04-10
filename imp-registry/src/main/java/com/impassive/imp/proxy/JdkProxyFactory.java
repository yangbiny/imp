package com.impassive.imp.proxy;

import com.impassive.imp.invoker.AbstractInvoker;
import com.impassive.imp.invoker.Invoker;
import java.lang.reflect.Method;

/** @author impassivey */
public class JdkProxyFactory implements ProxyFactory {

  @Override
  public <T> Invoker<T> getInvoker(T ref, Class<T> classType) {
    return new AbstractInvoker<T>(ref, classType) {

      @Override
      protected Object doInvoke(
          T reference, String methodName, Object[] params, Class<?>[] paramsType)
          throws Throwable {
        final Method method = reference.getClass().getMethod(methodName, paramsType);
        return method.invoke(reference, params);
      }
    };
  }
}
