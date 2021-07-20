package com.impassive.imp.remoting.channel;

import com.impassive.imp.remoting.Channel;
import com.impassive.imp.remoting.ChannelHandler;
import com.impassive.imp.remoting.Endpoint;

/** @author impassivey */
public abstract class AbstractEndpoint implements Endpoint, ChannelHandler {

  protected ChannelHandler channelHandler;

  public AbstractEndpoint(ChannelHandler channelHandler) {
    this.channelHandler = channelHandler;
  }

  @Override
  public void connection(Channel channel) {
    channelHandler.connection(channel);
  }

  @Override
  public void receive(Channel channel, Object msg) throws Exception {
    channelHandler.receive(channel, msg);
  }

  @Override
  public void close(Channel channel) {
    channelHandler.close(channel);
  }
}
