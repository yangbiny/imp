package com.impassive.imp.net;

/** @author impassivey */
public class ChannelHandlerAdapter implements ChannelHandler {

  @Override
  public void connection(Channel channel) {}

  @Override
  public void receive(Channel channel, Object msg) {}

  @Override
  public void close(Channel channel) {}
}
