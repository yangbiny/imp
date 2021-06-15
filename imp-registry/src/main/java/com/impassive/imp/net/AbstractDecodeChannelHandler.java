package com.impassive.imp.net;

/** @author impassivey */
public abstract class AbstractDecodeChannelHandler implements ChannelHandler {

  protected final ChannelHandler channelHandler;

  public AbstractDecodeChannelHandler(ChannelHandler channelHandler) {
    this.channelHandler = channelHandler;
  }

  @Override
  public void connection(Channel channel) {
    channelHandler.connection(channel);
  }

  @Override
  public void receive(Channel channel, Object msg) {
    channelHandler.receive(channel, msg);
  }

  @Override
  public void close(Channel channel) {
    channelHandler.close(channel);
  }
}
