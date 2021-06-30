package com.impassive.remoting.netty.codec;

import com.impassive.codec.ImpCodec;
import com.impassive.imp.remoting.Codec;
import com.impassive.rpc.Invocation;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/** @author impassivey */
public class MsgEncoder extends MessageToByteEncoder<Invocation> {

  private final Codec codec = new ImpCodec();

  @Override
  protected void encode(
      ChannelHandlerContext channelHandlerContext, Invocation invocation, ByteBuf byteBuf)
      throws Exception {
    codec.encode(byteBuf,invocation);
  }
}
