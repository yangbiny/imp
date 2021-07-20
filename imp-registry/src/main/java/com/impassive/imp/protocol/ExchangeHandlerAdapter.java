package com.impassive.imp.protocol;

import com.impassive.imp.invoker.Invoker;
import com.impassive.imp.invoker.InvokerWrapper;
import com.impassive.imp.invoker.Result;
import com.impassive.imp.remoting.Channel;
import com.impassive.imp.remoting.ExchangeChannel;
import com.impassive.imp.remoting.channel.AbstractExchangeHandler;
import com.impassive.rpc.RpcInvocation;
import com.sun.org.apache.xml.internal.utils.NSInfo;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;

/** @author impassivey */
@Slf4j
public class ExchangeHandlerAdapter extends AbstractExchangeHandler {

  private static final Map<Class<?>, InvokerWrapper<?>> INVOKER_WRAPPER_MAP =
      new ConcurrentHashMap<>();

  @Override
  public void connection(Channel channel) {
    super.connection(channel);
  }

  @Override
  public void receive(Channel channel, Object msg) {
    if (msg instanceof RpcInvocation) {
      RpcInvocation rpcInvocation = (RpcInvocation) msg;
      final String serviceName = rpcInvocation.getServiceName();
      try {
        final Class<?> className = Class.forName(serviceName);
        final InvokerWrapper<?> invokerWrapper = INVOKER_WRAPPER_MAP.get(className);
        if (invokerWrapper == null) {
          throw new UnsupportedOperationException("can not find invokerWrapper");
        }
        final Invoker<?> invoker = invokerWrapper.getInvoker();
        try {
          final Result invoke = invoker.invoke(rpcInvocation);
          // 这里添加回复的操作
        } catch (Throwable throwable) {
          throw new RuntimeException("invoke has exception : ", throwable);
        }
      } catch (ClassNotFoundException e) {
        log.error("can not find class : {}", msg);
        log.error("can not find class : ", e);
      }
    }
  }

  @Override
  public void close(Channel channel) {
    super.close(channel);
  }

  @Override
  public CompletableFuture<Object> reply(ExchangeChannel exchangeChannel, Object request) {
    return super.reply(exchangeChannel, request);
  }

  public void putInvokerMap(InvokerWrapper<?> invokerWrapper) {
    Class<?> interfaceClass = invokerWrapper.getInterfaceClass();
    INVOKER_WRAPPER_MAP.put(interfaceClass, invokerWrapper);
  }
}
