package com.impassive.rpc.filter;

import com.impassive.rpc.invocation.Invocation;
import com.impassive.rpc.invoker.Invoker;
import com.impassive.rpc.result.Result;

/**
 * 过滤器接口
 */
public interface Filter {

  /**
   * filter机制。
   */
  Result filter(Invoker<?> invoker, Invocation invocation) throws Throwable;

}
