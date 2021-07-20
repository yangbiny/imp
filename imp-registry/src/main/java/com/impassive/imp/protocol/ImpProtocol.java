package com.impassive.imp.protocol;

import com.impassive.imp.common.Url;
import com.impassive.imp.invoker.ImpInvoker;
import com.impassive.imp.invoker.Invoker;
import com.impassive.imp.invoker.InvokerWrapper;
import com.impassive.imp.registry.AbstractRegistryFactory;
import com.impassive.imp.registry.Registry;
import com.impassive.imp.registry.RegistryFactory;
import com.impassive.imp.remoting.ExchangeClient;
import com.impassive.imp.remoting.ExchangeHandler;
import com.impassive.imp.remoting.channelHandler.DecodeChannelHandler;
import com.impassive.imp.remoting.channelHandler.HeaderExchangeHandler;
import com.impassive.imp.remoting.channelHandler.ImpExchangeClient;
import com.impassive.remoting.netty.NettyChannelHandler;
import com.impassive.remoting.netty.NettyClient;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** @author impassivey */
public class ImpProtocol implements Protocol {

  private static final Map<String, ProtocolServer> PROTOCOL_SERVER_MAP = new ConcurrentHashMap<>();

  private final ExchangeHandlerAdapter exchangeHandler = new ExchangeHandlerAdapter();

  @Override
  public <T> void export(Invoker<T> invoker) {
    doExport(invoker);
  }

  @Override
  public <T> Invoker<T> refer(Class<T> interfaceClass, Url url) {
    return doRefer(interfaceClass, url);
  }

  private <T> Invoker<T> doRefer(Class<T> interfaceClass, Url url) {
    return new ImpInvoker<>(interfaceClass, getClients(url), url);
  }

  private ExchangeClient[] getClients(Url url) {
    ExchangeClient[] clients = new ExchangeClient[1];
    clients[0] = initClient(url);
    return clients;
  }

  private ExchangeClient initClient(Url url) {
    return new ImpExchangeClient(
        new NettyClient(url, new DecodeChannelHandler(new HeaderExchangeHandler(exchangeHandler))));
  }

  private <T> void doExport(Invoker<T> invoker) {
    if (!(invoker instanceof InvokerWrapper)) {
      return;
    }
    InvokerWrapper<T> invokerWrapper = (InvokerWrapper<T>) invoker;
    exchangeHandler.putInvokerMap(invokerWrapper);
    openService(invokerWrapper.getUrl());
    registry(invokerWrapper);
  }

  private void openService(Url url) {
    final String addressKey = url.address();
    ProtocolServer protocolServer = PROTOCOL_SERVER_MAP.get(addressKey);
    if (protocolServer != null) {
      return;
    }
    Class<?> classType = url.getClassType();
    final NettyChannelHandler channelHandler =
        new NettyChannelHandler(
            url, new DecodeChannelHandler(new HeaderExchangeHandler(exchangeHandler)));
    if (PROTOCOL_SERVER_MAP.get(addressKey) != null) {
      return;
    }
    protocolServer = new ExchangerServer(url, channelHandler);
    PROTOCOL_SERVER_MAP.put(addressKey, protocolServer);
  }

  private <T> void registry(InvokerWrapper<T> invokerWrapper) {
    final Url url = invokerWrapper.getUrl();
    if (!url.getRegister()) {
      return;
    }
    final RegistryFactory registryFactory =
        AbstractRegistryFactory.getRegistryFactory(url.getRegistryType());
    final Registry registry = registryFactory.getRegistry(url);
    registry.registry(url);
  }
}
