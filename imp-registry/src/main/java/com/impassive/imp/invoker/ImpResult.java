package com.impassive.imp.invoker;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

/** @author impassivey */
public class ImpResult implements Result {

  private Object result;

  private CompletableFuture<RpcResponse> completableFuture;

  public ImpResult(CompletableFuture<RpcResponse> completableFuture) {
    this.completableFuture = completableFuture;
  }

  @Override
  public Object getResult() {
    return getRpcResponse().getResult();
  }

  private Result getRpcResponse() {
    try {
      if (completableFuture.isDone()) {
        return completableFuture.get();
      }
    } catch (Exception e) {
      throw new RuntimeException("get value has error :", e);
    }
    return new RpcResponse();
  }

  @Override
  public void setResult(Object object) {
    result = object;
  }

  @Override
  public <U> CompletableFuture<U> thenApply(Function<Result, ? extends U> object) {
    return this.completableFuture.thenApply(object);
  }
}
