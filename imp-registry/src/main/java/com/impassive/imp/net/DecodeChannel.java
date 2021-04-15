package com.impassive.imp.net;

import io.netty.buffer.ByteBuf;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

/** @author impassivey */
public class DecodeChannel implements ChannelHandler {

  @Override
  public void connection(Channel channel) {}

  @Override
  public void receive(Channel channel, Object msg) {
    ByteBuf byteBuf = (ByteBuf) msg;
    final InetSocketAddress removeAddress = channel.getRemoteAddress();
    System.out.println(byteBuf.toString(StandardCharsets.UTF_8));
  }

  @Override
  public void close(Channel channel) {}
}
