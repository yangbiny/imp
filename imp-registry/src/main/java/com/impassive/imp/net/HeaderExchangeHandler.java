package com.impassive.imp.net;

/** @author impassivey */
public class HeaderExchangeHandler implements ChannelHandler {

  protected ExchangeHandler exchangeHandler;

  public HeaderExchangeHandler(ExchangeHandler exchangeHandler) {
    this.exchangeHandler = exchangeHandler;
  }

  @Override
  public void connection(Channel channel) {
    exchangeHandler.connection(channel);
  }

  @Override
  public void receive(Channel channel, Object msg) {
    exchangeHandler.receive(channel, msg);
  }

  @Override
  public void close(Channel channel) {
    exchangeHandler.close(channel);
  }
}
