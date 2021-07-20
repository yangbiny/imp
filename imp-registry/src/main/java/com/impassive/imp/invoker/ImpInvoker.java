package com.impassive.imp.invoker;

import com.impassive.imp.common.Url;
import com.impassive.imp.remoting.ExchangeClient;
import com.impassive.rpc.Invocation;
import java.util.concurrent.CompletableFuture;

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
  protected Object doInvoke(Invocation invocation) throws Throwable {
    ExchangeClient exchangeClient = exchangeClients[0];
    CompletableFuture<Object> request = exchangeClient.request(invocation);
    return "傻狗";
  }
}
