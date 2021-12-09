package com.impassive.result;

import com.impassive.rpc.invocation.Invocation;
import com.impassive.rpc.invoker.Invoker;
import com.impassive.rpc.result.Result;
import com.impassive.rpc.RpcResponse;
import java.util.concurrent.CompletableFuture;

/** @author impassivey */
public abstract class AbstractProxyInvoker<T> implements Invoker<T> {

  protected T proxy;

  protected Class<T> classType;

  public AbstractProxyInvoker(T ref, Class<T> classType) {
    this.proxy = ref;
    this.classType = classType;
  }

  protected abstract Object doInvoke(
      T reference, String methodName, Object[] params, Class<?>[] paramsType) throws Throwable;

  @Override
  public Class<T> getInterfaceClass() {
    return classType;
  }

  @Override
  public Result invoke(Invocation invocation) throws Throwable {
    Object object =
        doInvoke(
            proxy, invocation.getMethodName(), invocation.getParams(), invocation.argumentsType());
    CompletableFuture<Object> future = CompletableFuture.completedFuture(object);
    final CompletableFuture<RpcResponse> handle =
        future.handle(
            (obj, e) -> {
              RpcResponse rpcResponse = new RpcResponse();
              rpcResponse.setResult(obj);
              return rpcResponse;
            });
    final ImpResult impResult = new ImpResult(handle);
    impResult.setResult(object);
    return impResult;
  }
}
