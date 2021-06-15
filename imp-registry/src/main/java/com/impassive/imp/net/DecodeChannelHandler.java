package com.impassive.imp.net;

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
