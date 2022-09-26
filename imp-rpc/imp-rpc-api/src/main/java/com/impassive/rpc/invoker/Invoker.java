package com.impassive.rpc.invoker;

import com.impassive.imp.Node;
import com.impassive.rpc.invocation.Invocation;
import com.impassive.rpc.result.Result;

/**
 * @author impassivey
 */
public interface Invoker<T> extends Node {

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
   * @throws Throwable 异常信息
   */
  Result invoke(Invocation invocation) throws Throwable;
}
