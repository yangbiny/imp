package com.impassive.remoting.netty.codec;

import com.impassive.codec.ImpCodec;
import com.impassive.imp.remoting.codec.Codec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;

/** @author impassivey */
public class DecodeRequest extends ByteToMessageDecoder {

  private final Codec codec = new ImpCodec();

  @Override
  protected void decode(
      ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list)
      throws Exception {
    Object decode = codec.decode(byteBuf);
    list.add(decode);
  }
}
