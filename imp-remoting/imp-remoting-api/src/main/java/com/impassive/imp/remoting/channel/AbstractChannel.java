package com.impassive.imp.remoting.channel;

import com.impassive.imp.common.Url;
import com.impassive.imp.remoting.Channel;
import com.impassive.imp.remoting.ChannelHandler;
import com.impassive.imp.remoting.ExchangeChannel;

/** @author impassivey */
public abstract class AbstractChannel extends AbstractChannelHandler implements Channel {


  public AbstractChannel(Url url, ChannelHandler channelHandler) {
    super(url,channelHandler);
  }


}
