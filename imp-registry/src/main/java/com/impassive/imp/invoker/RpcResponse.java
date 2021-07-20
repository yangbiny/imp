package com.impassive.imp.invoker;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/** @author impassivey */
public class RpcResponse implements Result {

  private Object object;

  @Override
  public Object getResult() {
    return object;
  }

  @Override
  public void setResult(Object object) {
    this.object = object;
  }

  @Override
  public <U> CompletableFuture<U> thenApply(Function<Result, ? extends U> object) {
    throw new UnsupportedOperationException();
  }
}
