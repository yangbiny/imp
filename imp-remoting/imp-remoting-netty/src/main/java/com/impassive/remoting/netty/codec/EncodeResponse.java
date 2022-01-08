package com.impassive.remoting.netty.codec;

import com.impassive.imp.common.extension.ExtensionLoader;
import com.impassive.imp.remoting.codec.Codec;
import com.impassive.rpc.result.Result;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author impassivey
 */
public class EncodeResponse extends MessageToByteEncoder<Result> {

  private final Codec codec = ExtensionLoader.getExtensionLoader(Codec.class).getDefaultValue();

  @Override
  protected void encode(ChannelHandlerContext channelHandlerContext, Result result,
      ByteBuf byteBuf) {
    codec.encode(byteBuf, result);
  }
}
