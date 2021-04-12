package com.impassive.imp.net;

import com.impassive.imp.protocol.Url;

/** @author impassivey */
public abstract class AbstractChannelHandler implements ChannelHandler {

  protected final Url url;

  protected final ChannelHandler handler;

  public AbstractChannelHandler(Url url, ChannelHandler handler) {
    this.url = url;
    this.handler = handler;
  }

  @Override
  public void receive(Channel channel, Object msg) {
    handler.receive(channel, msg);
  }

  @Override
  public void connection(Channel channel) {
    handler.connection(channel);
  }

  @Override
  public void close(Channel channel) {
    handler.close(channel);
  }
}
