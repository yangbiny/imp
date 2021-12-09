package com.impassive.invocation;

import com.impassive.imp.common.Url;
import com.impassive.imp.remoting.ExchangeClient;
import com.impassive.rpc.invocation.Invocation;
import com.impassive.rpc.RpcResponse;
import com.impassive.result.AbstractInvoker;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

/**
 * @author impassivey
 */
@Slf4j
public class ImpInvoker<T> extends AbstractInvoker<T> {

  private static final ThreadPoolExecutor THREAD_POOL_EXECUTOR =
      new ThreadPoolExecutor(
          10, 10, 10, TimeUnit.MINUTES, new ArrayBlockingQueue<>(1000), r -> new Thread(r));

  private final ExchangeClient[] exchangeClients;

  private Url url;

  public ImpInvoker(Class<T> interfaceClass, ExchangeClient[] clients, Url url) {
    super(interfaceClass);
    if (clients == null || clients.length == 0) {
      log.error("client is empty");
      throw new IllegalArgumentException("client is empty");
    }
    this.exchangeClients = clients;
    this.url = url;
  }

  @Override
  protected Object doInvoke(Invocation invocation) {
    ExchangeClient exchangeClient = exchangeClients[0];
    CompletableFuture<Object> request = exchangeClient.request(invocation);
    Future<Object> submit = THREAD_POOL_EXECUTOR.submit((Callable<Object>) request::get);
    try {
      RpcResponse o = (RpcResponse) submit.get();
      return o.getResult();
    } catch (InterruptedException | ExecutionException exception) {
      exception.printStackTrace();
    }
    return null;
  }
}
