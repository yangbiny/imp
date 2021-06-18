package com.impassive.imp.invoker;

import com.impassive.rpc.Invocation;

/** @author impassivey */
public abstract class AbstractInvoker<T> implements Invoker<T> {

  private T reference;

  private final Class<T> classType;

  public AbstractInvoker(Class<T> classType) {
    this.classType = classType;
  }

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
    final Object objectValue = doInvoke(invocation);
    return null;
  }

  /**
   * 执行方法调用
   *
   * @param invocation 执行的对象
   * @return 执行的结果
   * @throws Throwable Throwable
   */
  protected abstract Object doInvoke(Invocation invocation) throws Throwable;
}
