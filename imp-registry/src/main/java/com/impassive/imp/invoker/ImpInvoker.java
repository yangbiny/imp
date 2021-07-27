package com.impassive.imp.invoker;

import com.impassive.imp.common.Url;
import com.impassive.imp.remoting.ExchangeClient;
import com.impassive.imp.remoting.Invocation;
import com.impassive.rpc.RpcResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.BiConsumer;

/** @author impassivey */
public class ImpInvoker<T> extends AbstractInvoker<T> {

  private ExchangeClient[] exchangeClients;

  private Url url;

  public ImpInvoker(Class<T> interfaceClass, ExchangeClient[] clients, Url url) {
    super(interfaceClass);
    if (clients == null || clients.length == 0) {
      throw new IllegalArgumentException("client is empty");
    }
    this.exchangeClients = clients;
    this.url = url;
  }

  @Override
  protected Object doInvoke(Invocation invocation) {
    ExchangeClient exchangeClient = exchangeClients[0];
    CompletableFuture<RpcResponse> future =
        exchangeClient.request(invocation).thenApply(obj -> (RpcResponse) obj);
    RpcResponse rpcResponse = null;
    try {
      rpcResponse = future.get(10000, TimeUnit.SECONDS);
    } catch (InterruptedException | ExecutionException | TimeoutException e) {
      e.printStackTrace();
    }
    return rpcResponse.getResult();
  }
}
