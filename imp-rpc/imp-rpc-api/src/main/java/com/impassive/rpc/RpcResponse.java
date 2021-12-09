package com.impassive.rpc;

import com.impassive.rpc.request.Request;
import com.impassive.rpc.result.Result;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * @author impassivey
 */
public class RpcResponse implements Result, Request {

  private Long requestId;

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

  @Override
  public void setRequestId(long id) {
    this.requestId = id;
  }

  @Override
  public Long getRequestId() {
    return requestId;
  }
}
