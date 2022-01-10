package com.impassive.protocol;

import com.google.common.collect.Lists;
import com.impassive.imp.common.DiscoverService;
import com.impassive.imp.common.Url;
import com.impassive.imp.common.extension.ExtensionLoader;
import com.impassive.imp.remoting.ExchangeClient;
import com.impassive.imp.remoting.channelHandler.DecodeChannelHandler;
import com.impassive.imp.remoting.channelHandler.HeaderExchangeHandler;
import com.impassive.imp.remoting.channelHandler.ImpExchangeClient;
import com.impassive.invocation.ImpInvoker;
import com.impassive.registry.registry.Registry;
import com.impassive.registry.registry.RegistryFactory;
import com.impassive.remoting.netty.NettyClient;
import com.impassive.remoting.netty.NettyService;
import com.impassive.rpc.adapter.RoutingDiscoverAdapter;
import com.impassive.rpc.invoker.Invoker;
import com.impassive.rpc.invoker.InvokerWrapper;
import com.impassive.rpc.protocol.Protocol;
import com.impassive.rpc.protocol.ProtocolServer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

/**
 * @author impassivey
 */
@Slf4j
public class ImpProtocol implements Protocol {

  private static final Map<String, ProtocolServer> PROTOCOL_SERVER_MAP = new ConcurrentHashMap<>();

  private final ExchangeHandlerAdapter exchangeHandler = new ExchangeHandlerAdapter();

  private final RegistryFactory registryFactory = ExtensionLoader.getExtensionLoader(
      RegistryFactory.class).getDefaultExtension();

  private static final Map<String, List<ExchangeClient>> clientMap = new HashMap<>();

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

  @Override
  public void unRefer(Url url) {
    Registry registry = registryFactory.getRegistry(url);
    registry.unSubscribe(url);
  }

  private <T> Invoker<T> doRefer(Class<T> interfaceClass, Url url) {
    ImpInvoker<T> impInvoker = new ImpInvoker<>(interfaceClass, getClients(url), url);
    return ExtensionLoader.getExtensionLoader(Invoker.class).wrapper(impInvoker);
  }

  private List<ExchangeClient> getClients(Url url) {
    String address = url.address();
    List<ExchangeClient> exchangeClients = clientMap.get(address);
    if (CollectionUtils.isNotEmpty(exchangeClients)) {
      return exchangeClients;
    }
    List<ExchangeClient> clients = Lists.newArrayList(initClient(url));
    clientMap.put(address, clients);
    return clients;
  }

  private ExchangeClient initClient(Url url) {
    // 1. 是否是点对点？
    if (!url.useEndPoint()) {
      // 2. 不是 ---> 需要进行服务发现
      DiscoverService discoverService = RoutingDiscoverAdapter.discoverAndRouting(url);
      url.discoverService(discoverService);
    }
    Registry registry = registryFactory.getRegistry(url);
    registry.subscribe(url);
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
    final NettyService channelHandler =
        new NettyService(
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
    final Registry registry = registryFactory.getRegistry(url);
    registry.registry(url);
  }

  @Override
  public void destroy() {
    registryFactory.destroy();
    for (ProtocolServer value : PROTOCOL_SERVER_MAP.values()) {
      value.destroy();
    }

    RoutingDiscoverAdapter.close();

    for (Entry<String, List<ExchangeClient>> stringListEntry : clientMap.entrySet()) {
      List<ExchangeClient> value = stringListEntry.getValue();
      if (CollectionUtils.isEmpty(value)) {
        continue;
      }
      for (ExchangeClient exchangeClient : value) {
        exchangeClient.destroy();
      }

    }
  }
}
