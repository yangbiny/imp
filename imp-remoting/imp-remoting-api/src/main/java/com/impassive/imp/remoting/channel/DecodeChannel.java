package com.impassive.imp.remoting.channel;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;

/** @author impassivey */
public class DecodeChannel extends ByteToMessageDecoder {

  @Override
  protected void decode(
      ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list)
      throws Exception {
    int length = byteBuf.readableBytes();
    byte[] bytes = new byte[length];
    byteBuf.readBytes(bytes, 0, length);
    System.out.println(new String(bytes));

  }
}
