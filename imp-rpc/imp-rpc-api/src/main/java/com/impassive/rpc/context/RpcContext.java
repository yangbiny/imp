package com.impassive.rpc.context;

import com.impassive.rpc.invocation.Invocation;
import com.impassive.rpc.invoker.Invoker;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Rpc上下文信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RpcContext {

  private static final ThreadLocal<RpcContext> threadLocal = ThreadLocal.withInitial(
      RpcContext::new);

  private Invoker<?> invoker;

  private Invocation invocation;

  private Object[] params;

  private Class[] paramsTypes;


  public static RpcContext rpcContextInstance() {
    return threadLocal.get();
  }

  public static void removeContext() {
    threadLocal.remove();
  }
}
