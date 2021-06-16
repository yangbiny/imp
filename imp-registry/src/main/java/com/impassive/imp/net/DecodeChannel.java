package com.impassive.imp.net;

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
    while (byteBuf.isReadable()) {

      int readerIndex = byteBuf.readerIndex();
    }
  }
}
