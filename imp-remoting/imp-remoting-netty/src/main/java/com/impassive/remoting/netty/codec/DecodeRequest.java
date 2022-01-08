package com.impassive.remoting.netty.codec;

import com.impassive.imp.common.extension.ExtensionLoader;
import com.impassive.imp.remoting.codec.Codec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;

/** @author impassivey */
public class DecodeRequest extends ByteToMessageDecoder {

  private final Codec codec = ExtensionLoader.getExtensionLoader(Codec.class).getDefaultValue();

  @Override
  protected void decode(
      ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list)
      throws Exception {
    Object decode = codec.decode(byteBuf);
    list.add(decode);
  }
}
