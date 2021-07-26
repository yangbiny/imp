package com.impassive.remoting.netty.codec;

import com.impassive.codec.ImpCodec;
import com.impassive.imp.remoting.codec.Codec;
import com.impassive.imp.remoting.Invocation;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/** @author impassivey */
public class EncoderRequest extends MessageToByteEncoder<Invocation> {

  private final Codec codec = new ImpCodec();

  @Override
  protected void encode(
      ChannelHandlerContext channelHandlerContext, Invocation invocation, ByteBuf byteBuf)
      throws Exception {
    codec.encode(byteBuf,invocation);
  }
}
