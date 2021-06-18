package com.impassive.imp.remoting.channelHandler;

import com.impassive.imp.remoting.Channel;
import com.impassive.imp.remoting.ChannelHandler;
import com.impassive.imp.remoting.channel.AbstractDecodeChannelHandler;

/**
 * 解码器
 *
 * @author impassivey
 */
public class DecodeChannelHandler extends AbstractDecodeChannelHandler {

  public DecodeChannelHandler(ChannelHandler channelHandler) {
    super(channelHandler);
  }

  @Override
  public void receive(Channel channel, Object msg) {
    channelHandler.receive(channel, msg);
  }
}
