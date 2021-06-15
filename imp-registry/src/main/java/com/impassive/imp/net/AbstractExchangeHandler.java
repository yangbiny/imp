package com.impassive.imp.net;

import java.util.concurrent.CompletableFuture;

/** @author impassivey */
public abstract class AbstractExchangeHandler extends ChannelHandlerAdapter implements ExchangeHandler {

  @Override
  public void connection(Channel channel) {}

  @Override
  public void receive(Channel channel, Object msg) {}

  @Override
  public void close(Channel channel) {}

  @Override
  public CompletableFuture<Object> reply(ExchangeChannel exchangeChannel, Object request) {
    return null;
  }
}
