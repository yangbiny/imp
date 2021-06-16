package com.impassive.imp.net;

import com.impassive.imp.protocol.RpcInvocation;
import io.netty.buffer.ByteBuf;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/** @author impassivey */
public class DecodeChannel implements ChannelHandler {

  @Override
  public void connection(Channel channel) {}

  @Override
  public void receive(Channel channel, Object msg) {
    if (msg instanceof RpcInvocation){
      RpcInvocation rpcInvocation = (RpcInvocation) msg;
      System.out.println(Arrays.toString(rpcInvocation.getParams()));
    }
  }

  @Override
  public void close(Channel channel) {}
}
