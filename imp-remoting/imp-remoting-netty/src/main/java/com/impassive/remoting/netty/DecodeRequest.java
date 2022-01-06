package com.impassive.remoting.netty;

import com.impassive.imp.codec.Codec;
import com.impassive.imp.common.extension.ExtensionLoader;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;

/** @author impassivey */
public class DecodeRequest extends ByteToMessageDecoder {

  private final Codec codec = (Codec) ExtensionLoader.getExtensionLoader(Codec.class).getInstance();

  @Override
  protected void decode(
      ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list)
      throws Exception {
    Object decode = codec.decode(byteBuf);
    list.add(decode);
  }
}
