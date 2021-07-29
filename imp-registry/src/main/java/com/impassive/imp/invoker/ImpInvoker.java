package com.impassive.imp.invoker;

import com.impassive.imp.common.Url;
import com.impassive.imp.remoting.ExchangeClient;
import com.impassive.imp.remoting.Invocation;
import com.impassive.rpc.RpcResponse;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.BiConsumer;

/** @author impassivey */
public class ImpInvoker<T> extends AbstractInvoker<T> {

  private static final ThreadPoolExecutor THREAD_POOL_EXECUTOR =
      new ThreadPoolExecutor(
          10, 10, 10, TimeUnit.MINUTES, new ArrayBlockingQueue<>(1000), r -> new Thread(r));

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
    CompletableFuture<Object> request = exchangeClient.request(invocation);
    Future<Object> submit = THREAD_POOL_EXECUTOR.submit((Callable<Object>) request::get);
    try {
      RpcResponse o = (RpcResponse) submit.get(10, TimeUnit.SECONDS);
      return o.getResult();
    } catch (InterruptedException | ExecutionException | TimeoutException e) {
      e.printStackTrace();
    }
    return null;
  }
}
