package com.impassive.protocol;

import com.impassive.imp.common.Url;
import com.impassive.imp.exception.common.ImpCommonException;
import com.impassive.imp.remoting.ExchangeClient;
import com.impassive.imp.remoting.channelHandler.DecodeChannelHandler;
import com.impassive.imp.remoting.channelHandler.HeaderExchangeHandler;
import com.impassive.imp.remoting.channelHandler.ImpExchangeClient;
import com.impassive.invocation.ImpInvoker;
import com.impassive.registry.AbstractRegistryFactory;
import com.impassive.rpc.discover.DiscoverService;
import com.impassive.rpc.discover.ServiceDiscover;
import com.impassive.discover.ServiceDiscoverManager;
import com.impassive.rpc.protocol.Protocol;
import com.impassive.rpc.protocol.ProtocolServer;
import com.impassive.registry.registry.Registry;
import com.impassive.registry.registry.RegistryFactory;
import com.impassive.remoting.netty.NettyChannelHandler;
import com.impassive.remoting.netty.NettyClient;
import com.impassive.rpc.invoker.Invoker;
import com.impassive.rpc.invoker.InvokerWrapper;
import java.util.List;
import java.util.Map;
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

  private final ServiceDiscover serviceDiscover = ServiceDiscoverManager.getInstance();

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
    // 1. 是否是点对点？
    if (!url.useEndPoint()) {
      // 2. 不是 ---> 需要进行服务发现
      List<DiscoverService> discover = serviceDiscover.discover(url);
      if (CollectionUtils.isEmpty(discover)) {
        log.error("can not find service : {}, {}", url.getGroupName(), url.getInterfaceName());
        throw new ImpCommonException(
            "can not find service " + url.getGroupName() + " " + url.getInterfaceName());
      }
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
    final RegistryFactory registryFactory =
        AbstractRegistryFactory.getRegistryFactory(url.getRegistryType());
    final Registry registry = registryFactory.getRegistry(url);
    registry.registry(url);
  }
}
