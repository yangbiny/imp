package com.impassive.imp.invoker;

import com.impassive.imp.protocol.Invocation;

/** @author impassivey */
public abstract class AbstractInvoker<T> implements Invoker<T> {

  private final T reference;

  private final Class<T> classType;

  public AbstractInvoker(T reference, Class<T> classType) {
    if (reference == null) {
      throw new IllegalArgumentException("reference not be null");
    }
    if (!classType.isInstance(reference)) {
      throw new IllegalArgumentException(
          reference.getClass().getName() + " is not implement " + classType.getName());
    }
    this.reference = reference;
    this.classType = classType;
  }

  @Override
  public Class<T> getInterfaceClass() {
    return classType;
  }

  @Override
  public Result invoke(Invocation invocation) throws Throwable {
    // TODO 这里会正式的去调用，并返回
    final Object objectValue =
        doInvoke(
            reference,
            invocation.getMethodName(),
            invocation.getParams(),
            invocation.getParamTypes());
    return null;
  }

  /**
   * 执行方法调用
   *
   * @param reference 执行的对象
   * @param methodName 执行的方法名称
   * @param params 执行的方法的参数
   * @param paramsType 方法的参数类型
   * @return 执行的结果
   * @throws Throwable Throwable
   */
  protected abstract Object doInvoke(
      T reference, String methodName, Object[] params, Class<?>[] paramsType) throws Throwable;
}
