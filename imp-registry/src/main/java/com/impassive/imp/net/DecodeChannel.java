package com.impassive.imp.net;

import com.alibaba.fastjson.JSONObject;
import com.impassive.imp.protocol.Invocation;
import com.impassive.imp.protocol.RpcInvocation;
import io.netty.buffer.ByteBuf;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/** @author impassivey */
public class DecodeChannel implements ChannelHandler {

  @Override
  public void connection(Channel channel) {}

  @Override
  public void receive(Channel channel, Object msg) {
    ByteBuf byteBuf = (ByteBuf) msg;
    String s = byteBuf.toString(StandardCharsets.UTF_8);
    Invocation invocation = JSONObject.parseObject(s, RpcInvocation.class);
    System.out.println(invocation);
  }

  @Override
  public void close(Channel channel) {}
}
