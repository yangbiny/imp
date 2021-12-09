package com.impassive.remoting.netty.codec;

import com.impassive.codec.ImpCodec;
import com.impassive.rpc.result.Result;
import com.impassive.imp.remoting.codec.Codec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author impassivey
 */
public class EncodeResponse extends MessageToByteEncoder<Result> {

  private final Codec codec = new ImpCodec();

  @Override
  protected void encode(ChannelHandlerContext channelHandlerContext, Result result, ByteBuf byteBuf) {
    codec.encode(byteBuf, result);
  }
}
