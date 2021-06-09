package com.impassive.imp.protocol;

import com.impassive.imp.invoker.Invoker;
import com.impassive.imp.invoker.InvokerWrapper;
import com.impassive.imp.net.DecodeChannel;
import com.impassive.imp.net.NettyChannelHandler;
import com.impassive.imp.registry.AbstractRegistryFactory;
import com.impassive.imp.registry.Registry;
import com.impassive.imp.registry.RegistryFactory;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** @author impassivey */
public class ImpProtocol implements Protocol {

  private static final Map<String, ProtocolServer> PROTOCOL_SERVER_MAP = new ConcurrentHashMap<>();

  @Override
  public <T> void export(Invoker<T> invoker) {
    doExport(invoker);
  }

  @Override
  public <T> Invoker<T> refer(Class<T> interfaceClass, Url url) {
    return doRefer(interfaceClass, url);
  }

  private <T> Invoker<T> doRefer(Class<T> interfaceClass, Url url) {
    return null;
  }

  private <T> void doExport(Invoker<T> invoker) {
    if (!(invoker instanceof InvokerWrapper)) {
      return;
    }
    InvokerWrapper<T> invokerWrapper = (InvokerWrapper<T>) invoker;
    openService(invokerWrapper.getUrl());
    registry(invokerWrapper);
  }

  private <T> void openService(Url url) {
    final String addressKey = url.address();
    ProtocolServer protocolServer = PROTOCOL_SERVER_MAP.get(addressKey);
    if (protocolServer != null) {
      return;
    }
    final NettyChannelHandler channelHandler = new NettyChannelHandler(url, new DecodeChannel());
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
