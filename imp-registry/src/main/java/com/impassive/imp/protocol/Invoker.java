package com.impassive.imp.protocol;

/** @author impassivey */
public interface Invoker<T> {

  /**
   * 返回当前对象的接口的类型
   *
   * @return 接口的类型
   */
  Class<T> getInterfaceClass();

  /**
   * 执行远程的命令
   *
   * @param invocation 需要执行的对象
   * @return 执行结果
   */
  Result invoke(Invocation invocation) throws Throwable;
}
