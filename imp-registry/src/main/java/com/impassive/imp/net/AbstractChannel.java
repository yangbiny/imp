package com.impassive.imp.net;

import com.impassive.imp.protocol.Url;

/** @author impassivey */
public abstract class AbstractChannel extends AbstractChannelHandler implements Channel {


  public AbstractChannel(Url url, ChannelHandler channelHandler) {
    super(url,channelHandler);
  }


}
