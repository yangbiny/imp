package com.impassive.imp.protocol;

import com.impassive.imp.common.Url;
import com.impassive.imp.remoting.ChannelHandler;
import com.impassive.remoting.netty.NettyChannelHandler;

/** @author impassivey */
public class ExchangerServer implements ProtocolServer {

  private final Url url;

  private final ChannelHandler channelHandler;

  public ExchangerServer(Url url, NettyChannelHandler channelHandler) {
    this.url = url;
    this.channelHandler = channelHandler;
  }

  @Override
  public String getAddress() {
    return url.address();
  }
}
