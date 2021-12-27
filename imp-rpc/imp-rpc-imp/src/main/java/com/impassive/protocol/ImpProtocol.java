package com.impassive.protocol;

import com.impassive.imp.common.DiscoverService;
import com.impassive.imp.common.Url;
import com.impassive.imp.remoting.ExchangeClient;
import com.impassive.imp.remoting.channelHandler.DecodeChannelHandler;
import com.impassive.imp.remoting.channelHandler.HeaderExchangeHandler;
import com.impassive.imp.remoting.channelHandler.ImpExchangeClient;
import com.impassive.invocation.ImpInvoker;
import com.impassive.registry.AbstractRegistryFactory;
import com.impassive.registry.registry.Registry;
import com.impassive.registry.registry.RegistryFactory;
import com.impassive.remoting.netty.NettyChannelHandler;
import com.impassive.remoting.netty.NettyClient;
import com.impassive.rpc.adapter.RoutingDiscoverAdapter;
import com.impassive.rpc.invoker.Invoker;
import com.impassive.rpc.invoker.InvokerWrapper;
import com.impassive.rpc.protocol.Protocol;
import com.impassive.rpc.protocol.ProtocolServer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;

/**
 * @author impassivey
 */
@Slf4j
public class ImpProtocol implements Protocol {

  private static final Map<String, ProtocolServer> PROTOCOL_SERVER_MAP = new ConcurrentHashMap<>();

  private final ExchangeHandlerAdapter exchangeHandler = new ExchangeHandlerAdapter();

  private RegistryFactory registryFactory;

  @Override
  public <T> void export(Invoker<T> invoker) {
    doExport(invoker);
  }

  @Override
  public <T> void unExport(Invoker<T> invoker) {
    doUnExport(invoker);
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
    // 1. 是否是点对点？
    if (!url.useEndPoint()) {
      // 2. 不是 ---> 需要进行服务发现
      DiscoverService discoverService = RoutingDiscoverAdapter.discoverAndRouting(url);
      url.discoverService(discoverService);
    }
    // 是直接使用即可
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

  private <T> void doUnExport(Invoker<T> invoker) {
    InvokerWrapper<T> invokerWrapper = (InvokerWrapper<T>) invoker;
    Url url = invokerWrapper.getUrl();
    final Registry registry = registryFactory.getRegistry(url);
    registry.unRegistry(url);
  }

  private void openService(Url url) {
    final String addressKey = url.address();
    ProtocolServer protocolServer = PROTOCOL_SERVER_MAP.get(addressKey);
    if (protocolServer != null) {
      return;
    }
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
    if (this.registryFactory == null) {
      this.registryFactory = AbstractRegistryFactory.getRegistryFactory(url.getRegistryType());
    }
    final Registry registry = registryFactory.getRegistry(url);
    registry.registry(url);
  }

  @Override
  public void destroy() {
    registryFactory.destroy();
    for (ProtocolServer value : PROTOCOL_SERVER_MAP.values()) {
      value.destroy();
    }
  }
}
