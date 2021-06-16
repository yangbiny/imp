package com.impassive.imp.net;

import com.impassive.imp.protocol.Invocation;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import java.nio.charset.StandardCharsets;

/** @author impassivey */
public class MsgEncoder extends MessageToByteEncoder<Invocation> {

  @Override
  protected void encode(ChannelHandlerContext channelHandlerContext, Invocation invocation,
      ByteBuf byteBuf) throws Exception {
    String s = invocation.toString();
    byteBuf.writeBytes(s.getBytes(StandardCharsets.UTF_8));
  }
}
