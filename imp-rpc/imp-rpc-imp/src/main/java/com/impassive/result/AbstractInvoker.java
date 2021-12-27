package com.impassive.result;

import com.impassive.rpc.RpcResponse;
import com.impassive.rpc.invocation.Invocation;
import com.impassive.rpc.invoker.Invoker;
import com.impassive.rpc.request.Request;
import com.impassive.rpc.result.Result;
import java.util.concurrent.CompletableFuture;

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
    final Object objectValue = doInvoke(invocation);
    Request request = (Request) invocation;
    final CompletableFuture<Object> future = CompletableFuture.completedFuture(objectValue);
    final CompletableFuture<RpcResponse> handle =
        future.handle(
            (obj, t) -> {
              RpcResponse rpcResponse = new RpcResponse();
              rpcResponse.setResult(obj);
              rpcResponse.setRequestId(request.getRequestId());
              return rpcResponse;
            });
    final ImpResult impResult = new ImpResult(handle);
    impResult.setResult(future);
    return impResult;
  }

  @Override
  public void destroy() {

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
