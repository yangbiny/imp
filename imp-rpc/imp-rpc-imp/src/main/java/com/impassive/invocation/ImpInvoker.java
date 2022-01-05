package com.impassive.invocation;

import com.impassive.imp.common.Url;
import com.impassive.imp.exception.common.ImpCommonException;
import com.impassive.imp.remoting.ExchangeClient;
import com.impassive.result.AbstractInvoker;
import com.impassive.rpc.RpcResponse;
import com.impassive.rpc.invocation.Invocation;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

/**
 * @author impassivey
 */
@Slf4j
public class ImpInvoker<T> extends AbstractInvoker<T> {

  private static final ThreadPoolExecutor THREAD_POOL_EXECUTOR =
      new ThreadPoolExecutor(
          10, 10, 10, TimeUnit.MINUTES, new ArrayBlockingQueue<>(1000), r -> new Thread(r));

  private final List<ExchangeClient> exchangeClients;

  private final Url url;

  public ImpInvoker(Class<T> interfaceClass, List<ExchangeClient> clients, Url url) {
    super(interfaceClass);
    if (CollectionUtils.isEmpty(clients)) {
      log.error("client is empty");
      throw new IllegalArgumentException("client is empty");
    }
    this.exchangeClients = clients;
    this.url = url;
  }

  @Override
  protected Object doInvoke(Invocation invocation) {
    ExchangeClient exchangeClient = exchangeClients.get(0);
    CompletableFuture<Object> request = exchangeClient.request(invocation);
    Future<Object> submit = THREAD_POOL_EXECUTOR.submit((Callable<Object>) request::get);
    try {
      RpcResponse o = (RpcResponse) submit.get();
      return o.getResult();
    } catch (InterruptedException | ExecutionException exception) {
      log.error("get result has error ", exception);
      throw new ImpCommonException("get result has error : ", exception);
    }
  }

  @Override
  public void destroy() {
    super.destroy();
    for (ExchangeClient exchangeClient : exchangeClients) {
      exchangeClient.destroy();
    }
    THREAD_POOL_EXECUTOR.shutdown();
  }

  @Override
  public Url getUrl() {
    return url;
  }
}
