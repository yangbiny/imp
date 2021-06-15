package com.impassive.imp.protocol;

import com.impassive.imp.invoker.ImpInvoker;
import com.impassive.imp.invoker.Invoker;
import com.impassive.imp.invoker.InvokerWrapper;
import com.impassive.imp.net.AbstractExchangeHandler;
import com.impassive.imp.net.Channel;
import com.impassive.imp.net.DecodeChannel;
import com.impassive.imp.net.DecodeChannelHandler;
import com.impassive.imp.net.ExchangeChannel;
import com.impassive.imp.net.ExchangeClient;
import com.impassive.imp.net.ExchangeHandler;
import com.impassive.imp.net.HeaderExchangeHandler;
import com.impassive.imp.net.ImpExchangeClient;
import com.impassive.imp.net.NettyChannelHandler;
import com.impassive.imp.net.NettyClient;
import com.impassive.imp.registry.AbstractRegistryFactory;
import com.impassive.imp.registry.Registry;
import com.impassive.imp.registry.RegistryFactory;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/** @author impassivey */
public class ImpProtocol implements Protocol {

  private static final Map<String, ProtocolServer> PROTOCOL_SERVER_MAP = new ConcurrentHashMap<>();

  private final ExchangeHandler exchangeHandler =
      new AbstractExchangeHandler() {
        @Override
        public void connection(Channel channel) {
          super.connection(channel);
        }

        @Override
        public void receive(Channel channel, Object msg) {
          super.receive(channel, msg);
        }

        @Override
        public void close(Channel channel) {
          super.close(channel);
        }

        @Override
        public CompletableFuture<Object> reply(ExchangeChannel exchangeChannel, Object request) {
          return super.reply(exchangeChannel, request);
        }
      };

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
    openService(invokerWrapper.getUrl());
    registry(invokerWrapper);
  }

  private void openService(Url url) {
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
