package com.impassive.imp.invoker;

import com.impassive.imp.net.ExchangeClient;
import com.impassive.imp.protocol.Url;

/** @author impassivey */
public class ImpInvoker<T> extends AbstractInvoker<T> {

  private ExchangeClient[] exchangeClients;

  private Url url;

  public ImpInvoker(Class<T> interfaceClass, ExchangeClient[] clients, Url url) {
    super(interfaceClass);
    this.exchangeClients = clients;
    this.url = url;
  }

  @Override
  protected Object doInvoke(T reference, String methodName, Object[] params, Class<?>[] paramsType)
      throws Throwable {
    return null;
  }
}
