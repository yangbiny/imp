package com.impassive.registry.protocol;

import com.impassive.rpc.invoker.Invoker;
import com.impassive.rpc.invoker.InvokerWrapper;
import com.impassive.imp.remoting.Channel;
import com.impassive.imp.remoting.ExchangeChannel;
import com.impassive.rpc.invocation.Invocation;
import com.impassive.rpc.result.Result;
import com.impassive.imp.remoting.channel.AbstractExchangeHandler;
import com.impassive.imp.remoting.channelHandler.DefaultCompletableFeature;
import com.impassive.imp.remoting.channelHandler.ExchangeChannelHandler;
import com.impassive.rpc.RpcInvocation;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
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
  public void receive(Channel channel, Object msg) throws Exception {
    if (msg instanceof Invocation) {
      try {
        ExchangeChannel exchangeHandler = ExchangeChannelHandler.getOrAddExchangeHandler(channel);
        reply(exchangeHandler, msg);
      } catch (Throwable throwable) {
        throw new RuntimeException("receive has exception : ", throwable);
      }
    } else if (msg instanceof Result) {
      handlerResult(channel, msg);
    }
  }

  private void handlerResult(Channel channel, Object msg) {
    DefaultCompletableFeature.receive(channel,msg);
  }

  @Override
  public void close(Channel channel) {
    super.close(channel);
  }

  @Override
  public CompletableFuture<Object> reply(ExchangeChannel exchangeChannel, Object request)
      throws Throwable {
    RpcInvocation rpcInvocation = (RpcInvocation) request;
    final String serviceName = rpcInvocation.getServiceName();
    InvokerWrapper<?> invokerWrapper = null;
    try {
      final Class<?> className = Class.forName(serviceName);
      invokerWrapper = INVOKER_WRAPPER_MAP.get(className);
      if (invokerWrapper == null) {
        throw new UnsupportedOperationException("can not find invokerWrapper");
      }
    } catch (ClassNotFoundException e) {
      log.error("can not find class : {}", request);
      log.error("can not find class : ", e);
    } catch (Throwable throwable) {
      log.error("invoke has error : ", throwable);
    }
    if (invokerWrapper == null) {
      throw new UnsupportedOperationException("can not find invokerWrapper");
    }
    // 这里添加回复的操作
    Result result = invokerWrapper.invoke(rpcInvocation);
    return result.thenApply(Function.identity());
  }

  public void putInvokerMap(InvokerWrapper<?> invokerWrapper) {
    Class<?> interfaceClass = invokerWrapper.getInterfaceClass();
    INVOKER_WRAPPER_MAP.put(interfaceClass, invokerWrapper);
  }
}
