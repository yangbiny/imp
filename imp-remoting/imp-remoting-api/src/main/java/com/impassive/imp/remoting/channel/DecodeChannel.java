package com.impassive.imp.remoting.channel;

import com.impassive.imp.remoting.Codec;
import com.impassive.imp.remoting.codec.ImpCodec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;

/** @author impassivey */
public class DecodeChannel extends ByteToMessageDecoder {

  private final Codec codec = new ImpCodec();

  @Override
  protected void decode(
      ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list)
      throws Exception {
      codec.decode(byteBuf);
  }
}
