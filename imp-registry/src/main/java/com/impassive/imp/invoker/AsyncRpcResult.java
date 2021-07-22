package com.impassive.imp.invoker;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

/** @author impassivey */
public class AsyncRpcResult implements Result {

  private CompletableFuture<RpcResponse> responseFuture;

  public AsyncRpcResult(CompletableFuture<RpcResponse> responseFuture) {
    this.responseFuture = responseFuture;
  }

  @Override
  public Object getResult() {
    return getResponse().getResult();
  }

  @Override
  public void setResult(Object object) {
    getResponse().setResult(object);
  }

  @Override
  public <U> CompletableFuture<U> thenApply(Function<Result, ? extends U> object) {
    return responseFuture.thenApply(object);
  }

  private RpcResponse getResponse() {
    try {
      if (responseFuture.isDone()) {
        return responseFuture.get();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return new RpcResponse();
  }
}