package com.impassive.imp.invoker;

/**
 * @author impassivey
 */
public class ImpInvoker<T> extends AbstractInvoker<T> {

  public ImpInvoker(T reference, Class<T> classType) {
    super(reference, classType);
  }

  @Override
  protected Object doInvoke(T reference, String methodName, Object[] params, Class<?>[] paramsType)
      throws Throwable {
    return null;
  }
}
