package com.impassive.imp.remoting.channelHandler;

import com.alibaba.fastjson.JSONObject;
import com.impassive.rpc.Invocation;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/** @author impassivey */
public class MsgEncoder extends MessageToByteEncoder<Invocation> {

  @Override
  protected void encode(
      ChannelHandlerContext channelHandlerContext, Invocation invocation, ByteBuf byteBuf)
      throws Exception {
    byte[] bytes = JSONObject.toJSONBytes(invocation);
    byteBuf.writeBytes(bytes);
  }
}
