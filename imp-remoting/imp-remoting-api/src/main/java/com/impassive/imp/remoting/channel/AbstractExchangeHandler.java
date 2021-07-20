package com.impassive.imp.remoting.channel;

import com.impassive.imp.remoting.Channel;
import com.impassive.imp.remoting.ExchangeChannel;
import com.impassive.imp.remoting.ExchangeHandler;
import com.impassive.imp.remoting.channelHandler.ChannelHandlerAdapter;
import java.util.concurrent.CompletableFuture;

/** @author impassivey */
public abstract class AbstractExchangeHandler extends ChannelHandlerAdapter implements
    ExchangeHandler {

  @Override
  public void connection(Channel channel) {}

  @Override
  public void receive(Channel channel, Object msg) throws Exception {}

  @Override
  public void close(Channel channel) {}

  @Override
  public CompletableFuture<Object> reply(ExchangeChannel exchangeChannel, Object request)
      throws Throwable {
    return null;
  }
}
