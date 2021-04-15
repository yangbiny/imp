package com.impassive.imp.protocol;

import com.impassive.imp.net.ChannelHandler;
import com.impassive.imp.net.NettyChannelHandler;

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
