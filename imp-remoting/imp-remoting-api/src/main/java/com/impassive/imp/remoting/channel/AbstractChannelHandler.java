package com.impassive.imp.remoting.channel;

import com.impassive.imp.common.Url;
import com.impassive.imp.remoting.Channel;
import com.impassive.imp.remoting.ChannelHandler;
import lombok.Getter;

/** @author impassivey */
@Getter
public abstract class AbstractChannelHandler implements ChannelHandler {

  protected final Url url;

  protected final ChannelHandler handler;

  public AbstractChannelHandler(Url url, ChannelHandler handler) {
    this.url = url;
    this.handler = handler;
  }

  @Override
  public void receive(Channel channel, Object msg) throws Exception{
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
