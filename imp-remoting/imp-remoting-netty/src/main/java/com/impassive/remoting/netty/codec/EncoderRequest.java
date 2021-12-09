package com.impassive.remoting.netty.codec;

import com.impassive.codec.ImpCodec;
import com.impassive.rpc.request.Request;
import com.impassive.imp.remoting.codec.Codec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author impassivey
 */
public class EncoderRequest extends MessageToByteEncoder<Request> {

  private final Codec codec = new ImpCodec();

  @Override
  protected void encode(
      ChannelHandlerContext channelHandlerContext, Request request, ByteBuf byteBuf)
      throws Exception {
    codec.encode(byteBuf, request);
  }
}
